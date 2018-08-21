package com.theandroiddev.mywins.presentation.successes

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.domain.service.shared_preferences.SharedPreferencesService
import com.theandroiddev.mywins.domain.service.successes.SuccessesService
import com.theandroiddev.mywins.mvp.MvpPresenter
import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.Constants.Companion.CATEGORY_JOURNEY
import com.theandroiddev.mywins.utils.Constants.Companion.CATEGORY_LEARN
import com.theandroiddev.mywins.utils.Constants.Companion.CATEGORY_MONEY
import com.theandroiddev.mywins.utils.Constants.Companion.CATEGORY_SPORT
import com.theandroiddev.mywins.utils.Constants.Companion.CATEGORY_VIDEO
import com.theandroiddev.mywins.utils.Constants.Companion.NOT_ACTIVE
import com.theandroiddev.mywins.utils.SearchFilter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jakub on 04.11.17.
 */

class SuccessesPresenter @Inject() constructor(
        private val successesService: SuccessesService,
        private val sharedPreferencesService: SharedPreferencesService
) : MvpPresenter<SuccessesView>() {

    private var sortType: String = Constants.SORT_DATE_ADDED
    private var isSortingAscending: Boolean = true
    private var searchTerm: String? = ""


    val searchFilter: SearchFilter
        get() = SearchFilter(searchTerm, sortType, isSortingAscending)

    fun loadSuccesses(searchFilter: SearchFilter) {

        successesService.getSuccesses(searchFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { successEntities ->
                    if (successEntities.isEmpty()) {

                        ifViewAttached { view ->
                            view.displayNoSuccesses()
                        }
                    } else {

                        ifViewAttached { view ->
                            view.displaySuccesses(successEntities)
                        }

                    }
                }

    }

    fun onPause(successesToRemove: MutableList<SuccessEntity>?) {
        if (successesToRemove != null) {
            successesService.removeSuccesses(successesToRemove)
        }
    }


    fun onUndoToRemove(position: Int, backupSuccess: SuccessEntity?) {

        if (backupSuccess != null) {
            ifViewAttached { view ->
                view.restoreSuccess(position, backupSuccess)
            }
        }

    }


    fun sendToRemoveQueue(position: Int, backupSuccess: SuccessEntity?) {

        if (backupSuccess != null) {
            ifViewAttached { view ->
                view.displaySuccessRemoved(position, backupSuccess)
            }
        }

    }

    fun updateSuccess(clickedPosition: Int, successes: MutableList<SuccessEntity>) {
        //TODO check it later
        if (successes.size > clickedPosition) {

            if (clickedPosition > NOT_ACTIVE) {
                val id = successes[clickedPosition].id
                if (id != null) {
                    successesService.fetchSuccess(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { successEntity ->
                                ifViewAttached { view ->
                                    view.displaySuccessChanged(clickedPosition, successEntity)
                                }
                            }

                } else {
                    //TODO handle errors
                }

            }
        }

    }


    fun categoryPicked(category: String) {
        ifViewAttached { view ->
            view.displayCategory(category)
        }
    }

    fun handleOptionsItemSelected(item: MenuItem, isSearchOpened: Boolean) {
        val id = item.itemId

        if (id == R.id.action_search) {
            toggleSearchBar(isSearchOpened)
        }//TODO it's so dumb
        if (id == R.id.action_date_started) {

            if (sortType != Constants.SORT_DATE_STARTED) {
                sortType = Constants.SORT_DATE_STARTED
            } else {
                isSortingAscending = !isSortingAscending
            }
            loadSuccesses(searchFilter)

        }
        if (id == R.id.action_date_ended) {

            if (sortType != Constants.SORT_DATE_ENDED) {
                sortType = Constants.SORT_DATE_ENDED
            } else {
                isSortingAscending = !isSortingAscending
            }
            loadSuccesses(searchFilter)

        }
        if (id == R.id.action_title) {

            if (sortType != Constants.SORT_TITLE) {
                sortType = Constants.SORT_TITLE
            } else {
                isSortingAscending = !isSortingAscending
            }
            loadSuccesses(searchFilter)

        }
        if (id == R.id.action_date_added) {

            if (sortType != Constants.SORT_DATE_ADDED) {
                sortType = Constants.SORT_DATE_ADDED
            } else {
                isSortingAscending = !isSortingAscending
            }
            loadSuccesses(searchFilter)

        }
        if (id == R.id.action_importance) {

            if (sortType != Constants.SORT_IMPORTANCE) {
                sortType = Constants.SORT_IMPORTANCE
            } else {
                isSortingAscending = !isSortingAscending
            }
            loadSuccesses(searchFilter)

        }
        if (id == R.id.action_description) {

            if (sortType != Constants.SORT_DESCRIPTION) {
                sortType = Constants.SORT_DESCRIPTION
            } else {
                isSortingAscending = !isSortingAscending
            }
            loadSuccesses(searchFilter)

        }
    }


    fun toggleSearchBar(isSearchOpened: Boolean) {
        if (isSearchOpened) {
            ifViewAttached { view ->
                view.hideSearchBar()
            }
            successesService.getSuccesses(searchFilter)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { successEntities ->
                        ifViewAttached { view ->
                            view.displaySuccesses(successEntities)
                        }
                    }

        } else {
            ifViewAttached { view ->
                view.displaySearchBar()
            }
        }

    }


    fun showSearch() {
        ifViewAttached { view ->
            view.displaySearch()
        }
    }

    fun onEditorActionListener(searchTerm: String) {
        this.searchTerm = searchTerm
        showSearch()
    }

    fun onResumeActivity(successes: MutableList<SuccessEntity>?, clickedPosition: Int) {

        if (successes != null) {
            updateSuccess(clickedPosition, successes)
        }

    }


    fun onShowSoftKeyboard(imm: InputMethodManager?, searchBox: EditText?) {

        if (searchBox != null) {
            imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT)
        }
    }


    fun onHideSoftKeyboard(searchBox: EditText?, imm: InputMethodManager?) {

        if (imm != null && searchBox != null) {
            imm.hideSoftInputFromWindow(searchBox.windowToken, 0)
        }

    }

    fun onHideSearchBar() {
        clearSearch()
    }

    fun setSuccessListVisible(recyclerView: RecyclerView, emptyListTv: TextView) {
        recyclerView.visibility = android.view.View.VISIBLE
        emptyListTv.visibility = android.view.View.INVISIBLE
    }


    fun setSuccessListInvisible(recyclerView: RecyclerView, emptyListTv: TextView) {
        recyclerView.visibility = android.view.View.INVISIBLE
        emptyListTv.visibility = android.view.View.VISIBLE
    }


    fun selectCategory(id: Int) {

        when (id) {

            R.id.action_learn -> categoryPicked(CATEGORY_LEARN)
            R.id.action_sport -> categoryPicked(CATEGORY_SPORT)
            R.id.action_journey -> categoryPicked(CATEGORY_JOURNEY)
            R.id.action_money -> categoryPicked(CATEGORY_MONEY)
            R.id.action_video -> categoryPicked(CATEGORY_VIDEO)

            else -> {
            }
        }
    }

    fun onSearchTextChanged(searchTerm: String) {
        this.searchTerm = searchTerm
        loadSuccesses(searchFilter)
    }


    fun addSuccess(s: SuccessEntity) {

        if (sharedPreferencesService.isFirstSuccessAdded == true) {
            successesService.clearDatabase()
            sharedPreferencesService.setFirstSuccessAdded()
        }
        successesService.addSuccess(s)
        loadSuccesses(searchFilter)
        ifViewAttached { view ->
            view.displayUpdatedSuccesses()
        }

    }


    fun checkPreferences() {
        if (sharedPreferencesService.isFirstRun) {
            //TODO add to database
            val defaultSuccesses = successesService.getDefaultSuccesses()
            successesService.saveSuccesses(defaultSuccesses)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        sharedPreferencesService.setNotFirstRun()
                        ifViewAttached { view ->
                            view.displaySuccesses(defaultSuccesses)
                        }
                    }
        } else {
            loadSuccesses(SearchFilter())
        }
    }

    fun startSlider(successEntities: MutableList<SuccessEntity>, success: SuccessEntity, position: Int, titleTv: TextView, categoryTv: TextView,
                    dateStartedTv: TextView, dateEndedTv: TextView, categoryIv: ImageView,
                    importanceIv: ImageView, constraintLayout: ConstraintLayout, cardView: CardView) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            ifViewAttached { view ->
                view.displaySliderAnimation(successEntities, success, position, titleTv,
                        categoryTv, dateStartedTv, dateEndedTv, categoryIv, importanceIv,
                        constraintLayout, cardView)
            }
        } else {
            ifViewAttached { view ->
                view.displaySlider(successEntities)
            }

        }

    }


    fun clearSearch() {
        searchTerm = ""
        //onExtrasLoaded();

    }

    override fun detachView() {

    }

    override fun destroy() {

    }

}
