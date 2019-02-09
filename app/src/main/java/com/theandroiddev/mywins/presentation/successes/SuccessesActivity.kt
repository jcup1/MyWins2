package com.theandroiddev.mywins.presentation.successes

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ItemTouchHelper
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
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.google.android.material.snackbar.Snackbar
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.core.mvp.MvpDaggerAppCompatActivity
import com.theandroiddev.mywins.core.mvp.startActivity
import com.theandroiddev.mywins.presentation.insert_success.InsertSuccessActivity
import com.theandroiddev.mywins.presentation.insert_success.InsertSuccessBundle
import com.theandroiddev.mywins.presentation.success_slider.SuccessSliderActivity
import com.theandroiddev.mywins.presentation.success_slider.SuccessSliderBundle
import com.theandroiddev.mywins.utils.Constants.Companion.Category
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
import com.theandroiddev.mywins.utils.KEY_MVP_BUNDLE
import com.theandroiddev.mywins.utils.SuccessesConfig
import io.codetail.animation.ViewAnimationUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class SuccessesActivity :
    MvpDaggerAppCompatActivity<SuccessesView, SuccessesBundle, SuccessesPresenter>(),
    android.view.View.OnClickListener, SuccessAdapter.OnItemClickListener, SuccessesView {


    private lateinit var successAdapter: SuccessAdapter

    private var action: ActionBar? = null

    var searchBox: EditText? = null

    private var simpleCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                target: androidx.recyclerview.widget.RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                direction: Int
            ) {

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
        presenter.onPause(successAdapter.successesToRemove)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        presenter.onResumeActivity(successAdapter.successes, clickedPosition)
        if (searchBox != null) {
            showSoftKeyboard()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        searchAction = menu.findItem(R.id.action_search)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onBackPressed() {

        when {
            multiple_actions?.isExpanded == true -> multiple_actions?.collapse()
            searchBox != null -> presenter?.toggleSearchBar(true)
            else -> super.onBackPressed()
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

        successesConfig.configFABs(
            applicationContext,
            action_learn, action_sport, action_journey, action_money, action_video
        )

        action_learn.setOnClickListener(this)
        action_sport.setOnClickListener(this)
        action_journey.setOnClickListener(this)
        action_money.setOnClickListener(this)
        action_video.setOnClickListener(this)
    }

    private fun setUpRecycler() {

        successAdapter =
            SuccessAdapter(R.layout.success_layout, this, DrawableSelector(applicationContext))
        recycler_view.adapter = successAdapter
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (data != null) {
            if (requestCode == REQUEST_CODE_INSERT) {
                if (resultCode == Activity.RESULT_OK) {
                    onSuccessAdded(data)
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                } else {
                    onSuccessNotAdded()
                }

            }
            if (requestCode == REQUEST_CODE_SLIDER) {
                if (resultCode == Activity.RESULT_OK) {
                    onSliderResultSuccess(data)
                }
            }
        }
    }


    override fun removeSuccess(position: Int, backupSuccess: SuccessModel) {
        successAdapter.successes.removeAt(position)
        successRemoved(position)
        successAdapter.successesToRemove.add(backupSuccess)
        if (successAdapter.successes.isEmpty()) {
            //onExtrasLoaded();
            displayNoSuccesses()
        }
    }

    override fun restoreSuccess(position: Int, backupSuccess: SuccessModel) {
        successAdapter.successes.add(position, backupSuccess)
        successAdapter.successesToRemove.remove(backupSuccess)
        recycler_view?.scrollToPosition(position)
        successAdapter.notifyItemInserted(position)

        if (successAdapter.successes.size == 1) {
            presenter?.setSuccessListVisible(recycler_view, empty_list_text)
        }

    }

    private fun onSliderResultSuccess(data: Intent) {
        val position = data.getIntExtra("position", 0)

        recycler_view?.scrollToPosition(position)
    }

    private fun onSuccessNotAdded() {
        Snackbar.make(
            main_constraint, getString(R.string.snack_success_not_added),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun onSuccessAdded(data: Intent) {
        val success = data.extras?.getSerializable(EXTRA_INSERT_SUCCESS_ITEM)
        if (success != null && success is SuccessModel) {
            presenter?.addSuccess(success)
        }
    }

    private fun toRemove(position: Int) {
        showUndoSnackbar(position)
    }

    private fun showUndoSnackbar(position: Int) {
        val successToRemove = successAdapter.successes[position]
        successAdapter.backupSuccess = successToRemove

        val snackbar = Snackbar
            .make(
                main_constraint, getString(R.string.snack_success_removed),
                Snackbar.LENGTH_LONG
            )
            .setAction(getString(R.string.snack_undo)) {

                val backupSuccessModel = successAdapter.backupSuccess
                presenter?.onUndoToRemove(position, backupSuccessModel)

            }
        snackbar.show()

        presenter?.sendToRemoveQueue(position, successAdapter.backupSuccess)
    }

    override fun onItemClick(
        success: SuccessModel, position: Int, titleTv: TextView, categoryTv: TextView,
        dateStartedTv: TextView, dateEndedTv: TextView, categoryIv: ImageView,
        importanceIv: ImageView, constraintLayout: ConstraintLayout, cardView: CardView
    ) {

        this.clickedPosition = position
        hideSoftKeyboard()
        successAdapter.successes
        presenter?.startSlider(
            successAdapter.successes, success, position, titleTv, categoryTv, dateStartedTv, dateEndedTv, categoryIv,
            importanceIv, constraintLayout, cardView
        )

        startSuccessesSlider()
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

    override fun displayDefaultSuccesses(successList: MutableList<SuccessModel>) {
        successAdapter.updateSuccessList(successList)
        presenter?.setSuccessListVisible(recycler_view, empty_list_text)
    }

    override fun displayNoSuccesses() {
        presenter?.setSuccessListInvisible(recycler_view, empty_list_text)
    }

    override fun displaySuccesses(successes: List<SuccessModel>) {
        successAdapter.updateSuccessList(successes)
        presenter?.setSuccessListVisible(recycler_view, empty_list_text)
    }

    override fun updateAdapterList(successList: MutableList<SuccessModel>) {
        successAdapter.updateSuccessList(successList)
    }

    override fun successRemoved(position: Int) {
        successAdapter.notifyItemRemoved(position)
    }

    override fun clearSuccessesToRemove() {
        successAdapter.successesToRemove.clear()
    }

    override fun displaySuccessChanged(position: Int, updatedSuccess: SuccessModel) {
        successAdapter.successes[position] = updatedSuccess
        successAdapter.notifyItemChanged(clickedPosition)
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
        val layoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
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
        successAdapter.notifyDataSetChanged()
    }

    override fun displayCategory(category: Category) {

        startActivity<InsertSuccessActivity>(InsertSuccessBundle(category), REQUEST_CODE_INSERT)
        multiple_actions?.collapse()
    }

    private fun startSuccessesSlider() {
        val successes = successAdapter.successes
        startActivity<SuccessSliderActivity>(
            SuccessSliderBundle(clickedPosition, successes),
            REQUEST_CODE_SLIDER
        )
    }

    override fun displaySliderAnimation(
        successes: MutableList<SuccessModel>,
        success: SuccessModel, position: Int,
        titleTv: TextView, categoryTv: TextView,
        dateStartedTv: TextView, dateEndedTv: TextView,
        categoryIv: ImageView, importanceIv: ImageView,
        constraintLayout: ConstraintLayout, cardView: CardView
    ) {

        val intent = Intent(this@SuccessesActivity, SuccessSliderActivity::class.java)

        intent.putExtra(KEY_MVP_BUNDLE, SuccessSliderBundle(clickedPosition, successes))

        val p1 = androidx.core.util.Pair(titleTv as View, EXTRA_SUCCESS_TITLE)
        val p2 = androidx.core.util.Pair(categoryTv as View, EXTRA_SUCCESS_CATEGORY)
        val p3 = androidx.core.util.Pair(dateStartedTv as View, EXTRA_SUCCESS_DATE_STARTED)
        val p4 = androidx.core.util.Pair(dateEndedTv as View, EXTRA_SUCCESS_DATE_ENDED)
        val p5 = androidx.core.util.Pair(categoryIv as View, EXTRA_SUCCESS_CATEGORY_IV)
        val p6 = androidx.core.util.Pair(importanceIv as View, EXTRA_SUCCESS_IMPORTANCE_IV)
        val p7 = androidx.core.util.Pair(cardView as View, EXTRA_SUCCESS_CARD_VIEW)

        val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this, p1, p2, p3, p4, p5, p6, p7
        )

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
