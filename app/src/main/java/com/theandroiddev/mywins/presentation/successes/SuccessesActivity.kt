package com.theandroiddev.mywins.presentation.successes

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.mvp.MvpDaggerAppCompatActivity
import com.theandroiddev.mywins.presentation.insert_success.InsertSuccessActivity
import com.theandroiddev.mywins.presentation.success_slider.SuccessSliderActivity
import com.theandroiddev.mywins.utils.Constants.Companion.EXTRA_INSERT_SUCCESS_ITEM
import com.theandroiddev.mywins.utils.Constants.Companion.EXTRA_SUCCESS_CARD_VIEW
import com.theandroiddev.mywins.utils.Constants.Companion.EXTRA_SUCCESS_CATEGORY
import com.theandroiddev.mywins.utils.Constants.Companion.EXTRA_SUCCESS_CATEGORY_IV
import com.theandroiddev.mywins.utils.Constants.Companion.EXTRA_SUCCESS_DATE_ENDED
import com.theandroiddev.mywins.utils.Constants.Companion.EXTRA_SUCCESS_DATE_STARTED
import com.theandroiddev.mywins.utils.Constants.Companion.EXTRA_SUCCESS_IMPORTANCE_IV
import com.theandroiddev.mywins.utils.Constants.Companion.EXTRA_SUCCESS_TITLE
import com.theandroiddev.mywins.utils.Constants.Companion.NOT_ACTIVE
import com.theandroiddev.mywins.utils.Constants.Companion.REQUEST_CODE_INSERT
import com.theandroiddev.mywins.utils.Constants.Companion.REQUEST_CODE_SLIDER
import com.theandroiddev.mywins.utils.DrawableSelector
import com.theandroiddev.mywins.utils.SuccessesConfig
import io.codetail.animation.ViewAnimationUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class SuccessesActivity : MvpDaggerAppCompatActivity<SuccessesView, SuccessesPresenter>(),
        android.view.View.OnClickListener, SuccessAdapter.OnItemClickListener, SuccessesView {
    override fun displaySuccessRemoved(position: Int, backupSuccess: SuccessEntity) {
        successAdapter?.successes?.removeAt(position)
        successRemoved(position)
        successAdapter?.successesToRemove?.add(backupSuccess)
        //TODO handle it
        if (successAdapter?.successes?.isEmpty() == true) {
            //onExtrasLoaded();
            displayNoSuccesses()
        }
    }

    var successAdapter: SuccessAdapter? = null

    var action: ActionBar? = null

    var searchBox: EditText? = null

    var simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            toRemove(viewHolder.adapterPosition)

        }
    }
    private var searchAction: MenuItem? = null
    private var clickedPosition = NOT_ACTIVE

    private val searchText: String
        get() = if (searchBox != null) {
            searchBox?.text.toString()
        } else ""

    override fun onPause() {
        super.onPause()
        presenter?.onPause(successAdapter?.successesToRemove)

    }

    override fun onResume() {
        super.onResume()

        presenter?.onResumeActivity(successAdapter?.successes, clickedPosition)
        if (searchBox != null) {
            showSoftKeyboard()
        }

    }


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        searchAction = menu.findItem(R.id.action_search)
        return super.onPrepareOptionsMenu(menu)
    }


    override fun onBackPressed() {

        if (multiple_actions?.isExpanded == true) {
            multiple_actions?.collapse()
        } else if (searchBox != null) {
            presenter?.toggleSearchBar(true)
        } else {
            super.onBackPressed()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(show_toolbar)
        setUpFABs()
        initCircularReveal()
        setUpRecycler()

    }

    override fun onStart() {
        super.onStart()
        presenter.checkPreferences()
    }

    private fun initCircularReveal() {
        shadow_view.visibility = android.view.View.GONE

        multiple_actions.setOnFloatingActionsMenuUpdateListener(
                object : FloatingActionsMenu.OnFloatingActionsMenuUpdateListener {
                    override fun onMenuExpanded() {

                        if (searchBox != null) {
                            hideSoftKeyboard()
                        }
                        showCircularReveal(shadow_view)
                    }

                    override fun onMenuCollapsed() {
                        hideCircularReveal(shadow_view)
                    }
                })

        shadow_view.setOnClickListener {
            if (multiple_actions.isExpanded) {
                multiple_actions.collapse()
            }
        }

    }

    private fun showCircularReveal(myView: android.view.View) {
        myView.setBackgroundColor(Color.argb(0, 0, 0, 0))
        myView.visibility = android.view.View.VISIBLE
        myView.post {
            myView.setBackgroundColor(Color.argb(127, 0, 0, 0))
            val cx = myView.left + myView.right
            val cy = myView.top + myView.bottom
            val dx = Math.max(cx, myView.width - cx)
            val dy = Math.max(cy, myView.height - cy)
            val finalRadius = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()
            val animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, finalRadius)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.duration = 375

            animator.start()
        }

    }

    private fun hideCircularReveal(myView: android.view.View) {
        myView.visibility = android.view.View.VISIBLE
        myView.post {
            val cx = myView.left + myView.right
            val cy = myView.top + myView.bottom
            val dx = Math.max(cx, myView.width - cx)
            val dy = Math.max(cy, myView.height - cy)
            val finalRadius = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()
            val animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, finalRadius, 0f)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.setDuration(375).addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {

                }

                override fun onAnimationEnd(animator: Animator) {
                    myView.visibility = android.view.View.GONE
                }

                override fun onAnimationCancel(animator: Animator) {

                }

                override fun onAnimationRepeat(animator: Animator) {

                }
            })

            animator.start()
        }


    }

    private fun setUpFABs() {

        val successesConfig = SuccessesConfig()

        successesConfig.configFABs(applicationContext,
                action_learn, action_sport, action_journey, action_money, action_video)

        action_learn.setOnClickListener(this)
        action_sport.setOnClickListener(this)
        action_journey.setOnClickListener(this)
        action_money.setOnClickListener(this)
        action_video.setOnClickListener(this)

    }

    private fun setUpRecycler() {

        successAdapter = SuccessAdapter(R.layout.success_layout, this, DrawableSelector(applicationContext))
        recycler_view.adapter = successAdapter
        val linearLayoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = linearLayoutManager
        recycler_view.setHasFixedSize(true)
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        presenter?.handleOptionsItemSelected(item, searchBox != null)

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(shadowView: android.view.View) {
        val fab = shadowView as FloatingActionButton

        presenter?.selectCategory(fab.id)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_INSERT) {

                onSuccessAdded(data)

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                onSuccessNotAdded()
            }
        } else if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_SLIDER) {
                onSliderResultSuccess(data)
            }
        }

    }

    override fun restoreSuccess(position: Int, backupSuccess: SuccessEntity) {
        successAdapter?.successes?.add(position, backupSuccess)
        successAdapter?.successesToRemove?.remove(backupSuccess)
        recycler_view?.scrollToPosition(position)
        successAdapter?.notifyItemInserted(position)

    }

    private fun onSliderResultSuccess(data: Intent) {
        val position = data.getIntExtra("position", 0)

        recycler_view?.scrollToPosition(position)

    }

    fun onSuccessNotAdded() {
        Snackbar.make(main_constraint, getString(R.string.snack_success_not_added),
                Snackbar.LENGTH_SHORT).show()

    }

    fun onSuccessAdded(data: Intent) {
        val s = data.extras?.getParcelable<SuccessEntity>(EXTRA_INSERT_SUCCESS_ITEM)
        if (s != null) {
            presenter?.addSuccess(s)
        }
    }

    private fun toRemove(position: Int) {
        showUndoSnackbar(position)

    }

    private fun showUndoSnackbar(position: Int) {
        successAdapter?.backupSuccess = successAdapter?.successes?.get(position)

        val snackbar = Snackbar
                .make(main_constraint, getString(R.string.snack_success_removed),
                        Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.snack_undo)) {
                    presenter?.onUndoToRemove(position, successAdapter?.backupSuccess)
                }
        snackbar.show()

        presenter?.sendToRemoveQueue(position, successAdapter?.backupSuccess)
    }

    override fun onItemClick(success: SuccessEntity, position: Int, titleTv: TextView, categoryTv: TextView,
                             dateStartedTv: TextView, dateEndedTv: TextView, categoryIv: ImageView,
                             importanceIv: ImageView, constraintLayout: ConstraintLayout, cardView: CardView) {

        this.clickedPosition = position
        hideSoftKeyboard()
        presenter?.startSlider(success, position, titleTv, categoryTv, dateStartedTv, dateEndedTv, categoryIv,
                importanceIv, constraintLayout, cardView)

    }

    override fun onLongItemClick(position: Int, cardView: CardView) {
        val popupMenu = PopupMenu(this@SuccessesActivity, cardView)

        popupMenu.menuInflater.inflate(R.menu.menu_item, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.remove_item_menu) {

                toRemove(position)

            }

            true
        }
    }

    override fun displayDefaultSuccesses(successList: MutableList<SuccessEntity>) {
        successAdapter?.updateSuccessList(successList)
        presenter?.setSuccessListVisible(recycler_view, empty_list_text)

    }

    override fun displayNoSuccesses() {
        presenter?.setSuccessListInvisible(recycler_view, empty_list_text)
    }

    override fun displaySuccesses(successList: MutableList<SuccessEntity>) {
        successAdapter?.updateSuccessList(successList)
        presenter?.setSuccessListVisible(recycler_view, empty_list_text)
    }

    override fun updateAdapterList(successList: MutableList<SuccessEntity>) {
        successAdapter?.updateSuccessList(successList)
    }

    override fun successRemoved(position: Int) {
        successAdapter?.notifyItemRemoved(position)
    }

    override fun displaySuccessChanged(position: Int, updatedSuccess: SuccessEntity) {
        successAdapter?.successes?.set(position, updatedSuccess)
        successAdapter?.notifyItemChanged(clickedPosition)
    }

    override fun hideSearchBar() {
        action = supportActionBar
        hideSoftKeyboard()


        if (action != null) {
            action?.setDisplayShowCustomEnabled(false)
            action?.setDisplayShowTitleEnabled(true)
        }
        searchAction?.icon = ContextCompat.getDrawable(this, R.drawable.ic_search)
        presenter?.onHideSearchBar()
        searchBox = null
    }

    override fun displaySearchBar() {
        action = supportActionBar

        if (action != null) {
            action?.setDisplayShowCustomEnabled(true)
        }
        val nullParent: ViewGroup? = null
        val view = layoutInflater.inflate(R.layout.search_bar, nullParent)
        val layoutParams = ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT)
        if (action != null) {
            action?.setCustomView(view, layoutParams)
            action?.setDisplayShowTitleEnabled(false)
            searchBox = action?.customView?.findViewById(R.id.edt_search)
            showSoftKeyboard()

        }

        searchBox?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                presenter?.onSearchTextChanged(searchText)
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
        searchBox?.setOnEditorActionListener { v, actionId, event ->
            presenter?.onEditorActionListener(searchText)

            true
        }

        searchBox?.requestFocus()
        searchAction?.icon = ContextCompat.getDrawable(this, R.drawable.ic_close)

    }

    override fun displayUpdatedSuccesses() {
        successAdapter?.notifyDataSetChanged()
    }

    override fun displayCategory(category: String) {

        val intent = Intent(this@SuccessesActivity, InsertSuccessActivity::class.java)
        intent.putExtra("categoryName", category)
        startActivityForResult(intent, REQUEST_CODE_INSERT)
        multiple_actions?.collapse()

    }

    override fun displaySlider(successList: MutableList<SuccessEntity>) {
        val intent = Intent(this@SuccessesActivity, SuccessSliderActivity::class.java)
        intent.putExtra("searchfilter", presenter?.searchFilter)
        intent.putExtra("position", clickedPosition)
        startActivityForResult(intent, REQUEST_CODE_SLIDER)
    }

    override fun displaySliderAnimation(successes: MutableList<SuccessEntity>,
                                        success: SuccessEntity, position: Int,
                                        titleTv: TextView, categoryTv: TextView,
                                        dateStartedTv: TextView, dateEndedTv: TextView,
                                        categoryIv: ImageView, importanceIv: ImageView,
                                        constraintLayout: ConstraintLayout, cardView: CardView) {

        val intent = Intent(this@SuccessesActivity, SuccessSliderActivity::class.java)

        intent.putExtra("searchfilter", presenter?.searchFilter)
        intent.putExtra("position", clickedPosition)

        val p1 = android.support.v4.util.Pair(titleTv as View, EXTRA_SUCCESS_TITLE)
        val p2 = android.support.v4.util.Pair(categoryTv as View, EXTRA_SUCCESS_CATEGORY)
        val p3 = android.support.v4.util.Pair(dateStartedTv as View, EXTRA_SUCCESS_DATE_STARTED)
        val p4 = android.support.v4.util.Pair(dateEndedTv as View, EXTRA_SUCCESS_DATE_ENDED)
        val p5 = android.support.v4.util.Pair(categoryIv as View, EXTRA_SUCCESS_CATEGORY_IV)
        val p6 = android.support.v4.util.Pair(importanceIv as View, EXTRA_SUCCESS_IMPORTANCE_IV)
        val p7 = android.support.v4.util.Pair(cardView as View, EXTRA_SUCCESS_CARD_VIEW)

        val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, p1, p2, p3, p4, p5, p6, p7)

        startActivity(intent, activityOptionsCompat.toBundle())
    }

    override fun displaySearch() {

        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (searchBox != null) {
            presenter?.onHideSoftKeyboard(searchBox, imm)
        }

    }

    private fun showSoftKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        presenter?.onShowSoftKeyboard(imm, searchBox)

    }

}