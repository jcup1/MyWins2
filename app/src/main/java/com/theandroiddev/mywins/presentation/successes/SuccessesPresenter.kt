package com.theandroiddev.mywins.presentation.successes

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
import com.theandroiddev.mywins.utils.Constants.Companion.Category.*
import com.theandroiddev.mywins.utils.Constants.Companion.NOT_ACTIVE
import com.theandroiddev.mywins.utils.Constants.Companion.SortType
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
) : MvpPresenter<SuccessesView, SuccessesBundle>() {

    override fun onViewCreated() {

    }

    private var sortType: SortType = SortType.DATE_ADDED
    private var isSortingAscending: Boolean = true
    private var searchTerm: String? = ""

    val searchFilter: SearchFilter
        get() = SearchFilter(searchTerm, sortType, isSortingAscending)

    fun loadSuccesses(searchFilter: SearchFilter) {

        successesService.getSuccesses(searchFilter)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ successesServiceResult ->
                when (successesServiceResult) {
                    is SuccessesServiceResult.Successes -> {
                        if (successesServiceResult.successes.isEmpty()) {
                            ifViewAttached { view ->
                                view.displayNoSuccesses()
                            }
                        } else {
                            ifViewAttached { view ->
                                view.displaySuccesses(successesServiceResult.successes.map {
                                    it.toModel()
                                })
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

    fun onPause(successesToRemove: MutableList<SuccessModel>) {
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

    fun sendToRemoveQueue(position: Int, backupSuccess: SuccessModel?) {

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


    fun categoryPicked(category: Category) {
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
            successesService.getSuccesses(searchFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ successesServiceResult ->

                    when (successesServiceResult) {
                        is SuccessesServiceResult.Successes -> {
                            ifViewAttached { view ->
                                view.displaySuccesses(
                                    successesServiceResult.successes.map { it.toModel() }
                                )
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

            R.id.action_learn -> categoryPicked(KNOWLEDGE)
            R.id.action_sport -> categoryPicked(SPORT)
            R.id.action_journey -> categoryPicked(JOURNEY)
            R.id.action_money -> categoryPicked(BUSINESS)
            R.id.action_video -> categoryPicked(MEDIA)

            else -> {
            }
        }
    }

    fun onSearchTextChanged(searchTerm: String) {
        this.searchTerm = searchTerm
        loadSuccesses(searchFilter)
    }


    fun addSuccess(success: SuccessModel) {

        if (sharedPreferencesService.isFirstSuccessAdded == false) {
            successesService.removeAllSuccesses()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    sharedPreferencesService.setFirstSuccessAdded()

                }, {
                    ifViewAttached { view ->
                        view.alerts?.displayUnexpectedError()
                    }
                }).addToDisposables(disposables)
        }
        val argument = SuccessesServiceArgument.Successes(listOf(success.toSuccessesServiceModel()))
        successesService.saveSuccesses(argument)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadSuccesses(searchFilter)
//                    ifViewAttached { view ->
//                        view.displayUpdatedSuccesses()
//                    }
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

                    val argument = SuccessesServiceArgument.Successes(successesServiceResult.successes)
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

    fun startSlider(
        successes: MutableList<SuccessModel>, success: SuccessModel, position: Int,
        titleTv: TextView, categoryTv: TextView, dateStartedTv: TextView,
        dateEndedTv: TextView, categoryIv: ImageView, importanceIv: ImageView,
        constraintLayout: ConstraintLayout, cardView: CardView
    ) {

        //TODO Fix this animation
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            ifViewAttached { view ->
                view.displaySliderAnimation(successes, success, position, titleTv,
                        categoryTv, dateStartedTv, dateEndedTv, categoryIv, importanceIv,
                        constraintLayout, cardView)
            }
        } else {
            ifViewAttached { view ->
                view.displaySlider(successes)
            }

        }*/

        ifViewAttached { view ->
            view.displaySlider(successes)
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
