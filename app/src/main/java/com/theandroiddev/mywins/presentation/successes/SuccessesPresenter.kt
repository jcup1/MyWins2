package com.theandroiddev.mywins.presentation.successes

import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.github.ajalt.timberkt.Timber.d
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.core.mvp.MvpPresenter
import com.theandroiddev.mywins.domain.service.shared_preferences.SharedPreferencesService
import com.theandroiddev.mywins.domain.service.successes.SuccessesService
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceArgument
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceResult
import com.theandroiddev.mywins.domain.service.successes.toModel
import com.theandroiddev.mywins.presentation.edit_success.hasOne
import com.theandroiddev.mywins.utils.Constants.Companion.Category
import com.theandroiddev.mywins.utils.Constants.Companion.NOT_ACTIVE
import com.theandroiddev.mywins.utils.Constants.Companion.SortType
import com.theandroiddev.mywins.utils.SearchFilter
import com.theandroiddev.mywins.utils.getCategoryById
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jakub on 04.11.17.
 */

class SuccessesPresenter @Inject constructor(
    private val successesService: SuccessesService,
    private val sharedPreferencesService: SharedPreferencesService
) : MvpPresenter<SuccessesView, SuccessesBundle>() {

    override fun onViewCreated() {
    }

    private var sortType: SortType = SortType.DATE_ADDED
    private var isSortingAscending: Boolean = true
    private var searchTerm: String? = ""

    val searchFilter: SearchFilter
        get() = SearchFilter(searchTerm, sortType, isSortingAscending)

    var successes = listOf<SuccessModel>()
        set(value) {
            field = value
            ifViewAttached { view ->
                view.isSuccessListVisible = value.isNotEmpty()
                view.displaySuccesses(value)
            }
        }

    private fun loadSuccesses(searchFilter: SearchFilter) {

        successesService.getSuccesses(searchFilter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ successesServiceResult ->
                when (successesServiceResult) {
                    is SuccessesServiceResult.Successes -> {
                        successes = successesServiceResult.successes.map {
                            it.toModel()
                        }
                    }
                    is SuccessesServiceResult.Error -> {
                        ifViewAttached { view ->
                            view.alerts?.displayUnexpectedError()
                        }
                    }
                }

            }, {
                ifViewAttached { view ->
                    view.alerts?.displayUnexpectedError()
                }
            }).addToDisposables(disposables)
    }

    fun onPause(successesToRemove: MutableList<SuccessModel>) {

        if (successesToRemove.isEmpty()) {
            return
        }

        val argument = SuccessesServiceArgument.Successes(
            successesToRemove.map { it.toSuccessesServiceModel() }
        )
        successesService.removeSuccesses(argument)
            .subscribeOn(Schedulers.io())
            .subscribe({
                ifViewAttached { view ->
                    d { "Removed successes to remove" }
                    view.clearSuccessesToRemove()
                }
            }, {
                ifViewAttached { view ->
                    //TODO more specified errors
                    view.alerts?.displayUnexpectedError()
                }
            }).addToDisposables(disposables)
    }

    fun onUndoToRemove(position: Int, backupSuccess: SuccessModel?) {

        if (backupSuccess != null) {
            ifViewAttached { view ->
                view.restoreSuccess(position, backupSuccess)
            }
        } else {
            ifViewAttached { view ->
                view.alerts?.displayUnexpectedError()
            }
        }
    }

    fun onSuccessAddedToRemoveQueue(position: Int, backupSuccess: SuccessModel?) {

        if (backupSuccess != null) {
            ifViewAttached { view ->
                view.removeSuccess(position, backupSuccess)
            }
        } else {
            ifViewAttached { view ->
                view.alerts?.displayUnexpectedError()
            }
        }
    }

    fun updateSuccess(clickedPosition: Int, successes: MutableList<SuccessModel>) {
        //TODO check it later
        if (successes.size > clickedPosition) {

            if (clickedPosition > NOT_ACTIVE) {
                val id = successes[clickedPosition].id
                if (id != null) {
                    fetchSuccess(id, clickedPosition)
                } else {
                    ifViewAttached { view ->
                        view.alerts?.displayUnexpectedError()
                    }
                }
            }
        }
    }

    private fun fetchSuccess(id: Long, clickedPosition: Int) {
        successesService.fetchSuccess(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ successesServiceResult ->

                when (successesServiceResult) {
                    is SuccessesServiceResult.Successes -> {
                        if (successesServiceResult.successes.hasOne()) {
                            val successesServiceModel =
                                successesServiceResult.successes.first()
                            ifViewAttached { view ->
                                view.displaySuccessChanged(
                                    clickedPosition,
                                    successesServiceModel.toModel()
                                )
                            }
                        } else {
                            ifViewAttached { view ->
                                view.alerts?.displayUnexpectedError()
                            }
                        }
                    }
                    is SuccessesServiceResult.Error -> {
                        ifViewAttached { view ->
                            view.alerts?.displayUnexpectedError()
                        }
                    }
                }

            }, {
                ifViewAttached { view ->
                    view.alerts?.displayUnexpectedError()
                }
            }).addToDisposables(disposables)
    }

    private fun categoryPicked(category: Category) {
        ifViewAttached { view ->
            view.displayCategory(category)
        }
    }

    fun handleOptionsItemSelected(item: MenuItem, isSearchOpened: Boolean) {
        val id = item.itemId

        if (id == R.id.action_search) {
            toggleSearchBar(isSearchOpened)
        }
        when (id) {
            R.id.action_date_started -> {

                if (sortType != SortType.DATE_STARTED) {
                    sortType = SortType.DATE_STARTED
                } else {
                    isSortingAscending = !isSortingAscending
                }
                loadSuccesses(searchFilter)
            }
            R.id.action_date_ended -> {
                if (sortType != SortType.DATE_ENDED) {
                    sortType = SortType.DATE_ENDED
                } else {
                    isSortingAscending = !isSortingAscending
                }
                loadSuccesses(searchFilter)
            }
            R.id.action_title -> {
                if (sortType != SortType.TITLE) {
                    sortType = SortType.TITLE
                } else {
                    isSortingAscending = !isSortingAscending
                }
                loadSuccesses(searchFilter)
            }
            R.id.action_date_added -> {
                if (sortType != SortType.DATE_ADDED) {
                    sortType = SortType.DATE_ADDED
                } else {
                    isSortingAscending = !isSortingAscending
                }
                loadSuccesses(searchFilter)
            }
            R.id.action_importance -> {
                if (sortType != SortType.IMPORTANCE) {
                    sortType = SortType.IMPORTANCE
                } else {
                    isSortingAscending = !isSortingAscending
                }
                loadSuccesses(searchFilter)
            }
            R.id.action_description -> {
                if (sortType != SortType.DESCRIPTION) {
                    sortType = SortType.DESCRIPTION
                } else {
                    isSortingAscending = !isSortingAscending
                }
                loadSuccesses(searchFilter)
            }
        }
    }

    fun toggleSearchBar(isSearchOpened: Boolean) {
        if (isSearchOpened) {
            ifViewAttached { view ->
                view.hideSearchBar()
            }
            loadSuccesses(searchFilter)
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

    fun onResumeActivity(successes: MutableList<SuccessModel>, clickedPosition: Int) {

        updateSuccess(clickedPosition, successes)
    }

    fun onShowSoftKeyboard(imm: InputMethodManager?, searchBox: EditText?) {

        if (searchBox != null) {
            imm?.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.SHOW_IMPLICIT
            )
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

    fun onFabCategorySelected(categoryId: Int?) {
        val category = categoryId?.getCategoryById() ?: Category.OTHER
        categoryPicked(category)
    }

    fun onSearchTextChanged(searchTerm: String) {
        this.searchTerm = searchTerm
        loadSuccesses(searchFilter)
    }

    fun onSuccessAdded(success: SuccessModel) {

        val argument = SuccessesServiceArgument.Successes(listOf(success.toSuccessesServiceModel()))
        val isFirstSuccessAdded = sharedPreferencesService.isFirstSuccessAdded

        if (isFirstSuccessAdded == false) {
            successesService.removeAllSuccesses().andThen(successesService.saveSuccesses(argument))
        } else {
            successesService.saveSuccesses(argument)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (isFirstSuccessAdded == false) {
                    sharedPreferencesService.setFirstSuccessAdded()
                }
                loadSuccesses(searchFilter)

            }, {
                ifViewAttached { view ->
                    view.alerts?.displayUnexpectedError()
                }
            }).addToDisposables(disposables)
    }

    fun checkPreferences() {
        if (sharedPreferencesService.isFirstRun) {
            //TODO add to database
            val successesServiceResult = successesService.getDefaultSuccesses()
            when (successesServiceResult) {
                is SuccessesServiceResult.Successes -> {

                    val argument =
                        SuccessesServiceArgument.Successes(successesServiceResult.successes)
                    successesService.saveSuccesses(argument)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            sharedPreferencesService.setNotFirstRun()
                            ifViewAttached { view ->
                                loadSuccesses(searchFilter)
                            }
                        }, {
                            ifViewAttached { view ->
                                view.alerts?.displayUnexpectedError()
                            }
                        })
                }
                is SuccessesServiceResult.Error -> {
                    ifViewAttached { view ->
                        view.alerts?.displayUnexpectedError()
                    }
                }
            }
        } else {
            loadSuccesses(SearchFilter())
        }
    }

    private fun clearSearch() {
        searchTerm = ""
        //onExtrasLoaded();
    }

    override fun detachView() {
    }

    override fun destroy() {
    }
}
