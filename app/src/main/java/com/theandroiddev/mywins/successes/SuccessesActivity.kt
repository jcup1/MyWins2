package com.theandroiddev.mywins.successes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
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
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.getbase.floatingactionbutton.FloatingActionButton
import com.getbase.floatingactionbutton.FloatingActionsMenu
import com.theandroiddev.mywins.InsertSuccessActivity
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.prefs.PreferencesHelper
import com.theandroiddev.mywins.mvp.MvpDaggerAppCompatActivity
import com.theandroiddev.mywins.success_slider.SuccessSliderActivity
import com.theandroiddev.mywins.utils.Constants.*
import com.theandroiddev.mywins.utils.SuccessesConfig
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.search_bar.*
import java.util.*

class SuccessesActivity : MvpDaggerAppCompatActivity<SuccessesView, SuccessesPresenter>(), android.view.View.OnClickListener, SuccessAdapter.OnItemClickListener, SuccessesView {
    var floatingActionsMenu: FloatingActionsMenu? = null
    var successAdapter: SuccessAdapter? = null

    var action: ActionBar? = null
    var videoDrawable: Drawable? = null
    var moneyDrawable: Drawable? = null
    var journeyDrawable: Drawable? = null
    var sportDrawable: Drawable? = null
    var learnDrawable: Drawable? = null

    var simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            toRemove(viewHolder.adapterPosition)

        }
    }
    private var searchAction: MenuItem? = null
    private var searchBox: EditText? = null
    private var clickedPosition = NOT_ACTIVE

    private val searchText: String
        get() = if (searchBox != null) {
            searchBox?.text.toString()
        } else ""

    override fun onPause() {
        super.onPause()
        presenter?.removeSuccessesFromQueue()

    }

    override fun onDestroy() {
        presenter?.onDestroyActivity()
        super.onDestroy()

    }

    override fun onResume() {
        super.onResume()

        if (edtSearch != null)
            presenter?.onResumeActivity(this, clickedPosition, edtSearch)

    }


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        searchAction = menu.findItem(R.id.action_search)
        return super.onPrepareOptionsMenu(menu)
    }


    override fun onBackPressed() {

        if (floatingActionsMenu?.isExpanded == true) {
            floatingActionsMenu?.collapse()
        } else if (searchBox != null) {

            presenter?.handleMenuSearch()
        } else {

            super.onBackPressed()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferencesHelper = PreferencesHelper(this)
        setSupportActionBar(show_toolbar)
        setUpFABs()
        initCircularReveal()
        setUpRecycler()
        presenter?.onCreateActivity(applicationContext, this, preferencesHelper)


    }

    private fun initCircularReveal() {
        shadow_view.visibility = android.view.View.GONE

        floatingActionsMenu = findViewById(R.id.multiple_actions)

    }

    private fun setUpFABs() {

        val successesConfig = SuccessesConfig()

        successesConfig.configFABs(applicationContext,
                videoDrawable, moneyDrawable, journeyDrawable, sportDrawable, learnDrawable,
                action_learn, action_sport, action_journey, action_money, action_video)

        action_learn.setOnClickListener(this)
        action_sport.setOnClickListener(this)
        action_journey.setOnClickListener(this)
        action_money.setOnClickListener(this)
        action_video.setOnClickListener(this)

    }

    private fun setUpRecycler() {

        successAdapter = SuccessAdapter(R.layout.success_layout, applicationContext, this)
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

        presenter?.handleOptionsItemSelected(item)

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(shadowView: android.view.View) {
        val fab = shadowView as FloatingActionButton

        presenter?.selectCategory(fab.id)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_INSERT) {
            if (resultCode == Activity.RESULT_OK) {
                onSuccessAdded(data)

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                onSuccessNotAdded()
            }
        }

        if (requestCode == REQUEST_CODE_SLIDER) {
            if (resultCode == Activity.RESULT_OK) {
                onSliderResultSuccess(data)
            }
        }

    }

    private fun onSliderResultSuccess(data: Intent) {
        val position = data.getIntExtra("position", 0)

        recycler_view?.scrollToPosition(position)

    }

    fun onSuccessNotAdded() {
        Snackbar.make(main_constraint, getString(R.string.snack_success_not_added), Snackbar.LENGTH_SHORT).show()

    }

    fun onSuccessAdded(data: Intent) {
        val s = data.extras?.getParcelable<Success>(EXTRA_INSERT_SUCCESS_ITEM)
        if (s != null) {
            presenter?.addSuccess(s)
        }
    }

    private fun toRemove(position: Int) {
        showUndoSnackbar(position)

    }

    private fun showUndoSnackbar(position: Int) {
        presenter?.backupSuccess(position)
        val snackbar = Snackbar
                .make(main_constraint, getString(R.string.snack_success_removed), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.snack_undo)) { presenter?.undoToRemove(position) }
        snackbar.show()

        presenter?.sendToRemoveQueue(position)
    }

    override fun onItemClick(success: Success, position: Int, titleTv: TextView, categoryTv: TextView, dateStartedTv: TextView, dateEndedTv: TextView, categoryIv: ImageView, importanceIv: ImageView, constraintLayout: ConstraintLayout, cardView: CardView) {

        this.clickedPosition = position
        hideSoftKeyboard()
        presenter?.startSlider(success, position, titleTv, categoryTv, dateStartedTv, dateEndedTv, categoryIv, importanceIv, constraintLayout, cardView)

    }

    override fun onLongItemClick(position: Int, cardview: CardView) {
        val popupMenu: PopupMenu
        popupMenu = PopupMenu(this@SuccessesActivity, cardview)

        popupMenu.menuInflater.inflate(R.menu.menu_item, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.remove_item_menu) {

                toRemove(position)

            }

            true
        }
    }

    override fun displayDefaultSuccesses(successList: ArrayList<Success>) {
        successAdapter?.updateSuccessList(successList)
        presenter?.setSuccessListVisible(recycler_view, empty_list_text)

    }

    override fun displayNoSuccesses() {
        presenter?.setSuccessListInvisible(recycler_view, empty_list_text)
    }

    override fun displaySuccesses(successList: ArrayList<Success>) {
        successAdapter?.updateSuccessList(successList)
        presenter?.setSuccessListVisible(recycler_view, empty_list_text)
    }

    override fun updateAdapterList(successList: ArrayList<Success>) {
        successAdapter?.updateSuccessList(successList)
    }

    override fun undoToRemove(position: Int) {
        presenter?.loadSuccesses()
        recycler_view?.scrollToPosition(position)

    }

    override fun successRemoved(position: Int) {
        successAdapter?.notifyItemRemoved(position)
    }

    override fun displaySuccessChanged() {
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
        searchBox = null
        presenter?.clearSearch()
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
            showSoftKeyboard()

        }

        edtSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                presenter?.setSearchTerm(searchText)
                presenter?.loadSuccesses()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
        searchBox?.setOnEditorActionListener { v, actionId, event ->
            presenter?.setSearchTerm(searchText)
            presenter?.showSearch()

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
        floatingActionsMenu?.collapse()

    }

    override fun displaySlider(successList: ArrayList<Success>) {
        val intent = Intent(this@SuccessesActivity, SuccessSliderActivity::class.java)
        intent.putExtra("searchfilter", presenter?.searchFilter)
        intent.putExtra("position", clickedPosition)
        startActivityForResult(intent, REQUEST_CODE_SLIDER)
    }

    override fun displaySliderAnimation(successes: ArrayList<Success>, success: Success, position: Int, titleTv: TextView, categoryTv: TextView, dateStartedTv: TextView, dateEndedTv: TextView, categoryIv: ImageView, importanceIv: ImageView, constraintLayout: ConstraintLayout, cardView: CardView) {

        val intent = Intent(this@SuccessesActivity, SuccessSliderActivity::class.java)

        intent.putExtra("searchfilter", presenter?.searchFilter)
        intent.putExtra("position", clickedPosition)

        //        Pair<android.view.View, String> p1, p2, p3, p4, p5, p6, p7;
        //        p1 = Pair.create((android.view.View) titleTv, EXTRA_SUCCESS_TITLE);
        //        p2 = Pair.create((android.view.View) categoryTv, EXTRA_SUCCESS_CATEGORY);
        //        p3 = Pair.create((android.view.View) dateStartedTv, EXTRA_SUCCESS_DATE_STARTED);
        //        p4 = Pair.create((android.view.View) dateEndedTv, EXTRA_SUCCESS_DATE_ENDED);
        //        p5 = Pair.create((android.view.View) categoryIv, EXTRA_SUCCESS_CATEGORY_IV);
        //        p6 = Pair.create((android.view.View) importanceIv, EXTRA_SUCCESS_IMPORTANCE_IV);
        //        p7 = Pair.create((android.view.View) cardView, EXTRA_SUCCESS_CARD_VIEW);
        //
        //        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
        //                this,
        //                p1, p2, p3, p4, p5, p6, p7);

        //        startActivity(intent, activityOptionsCompat.toBundle());
        startActivity(intent)
    }

    override fun displaySearch() {

        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        presenter?.hideSoftKeyboard(searchBox, imm)

    }

    private fun showSoftKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        presenter?.showSoftKeyboard(imm, searchBox)

    }

}
