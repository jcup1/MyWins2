package com.theandroiddev.mywins.UI.activities

import android.view.MenuItem
import com.nhaarman.mockito_kotlin.whenever
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.data.models.SearchFilter
import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.prefs.SharedPreferencesService
import com.theandroiddev.mywins.data.repositories.SuccessesRepository
import com.theandroiddev.mywins.successes.SuccessesPresenter
import com.theandroiddev.mywins.successes.SuccessesView
import com.theandroiddev.mywins.utils.Constants
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito
import org.mockito.Mockito.*

/**
 * Created by jakub on 28.10.17.
 */
class SuccessesPresenterTest : Spek({

    val successesRepository = Mockito.mock(SuccessesRepository::class.java)
    val preferencesHelper = mock(SharedPreferencesService::class.java)
    val presenter = SuccessesPresenter(successesRepository, preferencesHelper)
    val presenterSpy = spy(presenter)
    val view = Mockito.mock(SuccessesView::class.java)

    afterEachTest {
        reset(successesRepository)
        reset(preferencesHelper)
        reset(view)
        presenter.attachView(view)
    }


    presenter.attachView(view)

    given("loading successes") {

        on("successes available") {
            val successList = ArrayList<Success>()

            successList.add(Success())
            successList.add(Success())

            it("should display 2 successes") {
                val searchFilter = SearchFilter(null, Constants.SORT_DATE_ADDED, true)
                `when`(successesRepository.getSuccesses(searchFilter))
                        .thenReturn(successList)

                presenter.loadSuccesses(searchFilter)

                verify(view, times(1)).displaySuccesses(successList)
            }
        }

        on("no successes") {
            val successList = ArrayList<Success>()

            it("should display no successes") {
                val searchFilter = SearchFilter(null, Constants.SORT_DATE_ADDED, true)
                `when`(successesRepository.getSuccesses(searchFilter))
                        .thenReturn(successList)

                presenter.loadSuccesses(searchFilter)

                verify(view, times(1)).displayNoSuccesses()
            }
        }

    }

    given("pause") {

        on("success was backed up") {
            it("should remove successes") {
                val successes = arrayListOf(Success(), Success())
                presenter.onPause(successes)
                verify(successesRepository, times(1)).removeSuccesses(successes)
            }
        }

        on("success wasn't backed up") {
            it("should not remove successes") {
                val successes: ArrayList<Success>? = null

                presenter.onPause(successes)
                verify(successesRepository, never()).removeSuccesses(arrayListOf())
            }
        }
    }

    given("undo to remove") {

        on("backup success is null") {
            it("should not undo removing success") {

                val backupSuccess = null
                presenter.onUndoToRemove(0, backupSuccess)
                verify(view, never()).restoreSuccess(0, Success())

            }
        }

        on("backup success is not null") {
            it("should undo removing success") {

                val backupSuccess = Success()
                presenter.onUndoToRemove(0, backupSuccess)
                verify(view, times(1)).restoreSuccess(0, backupSuccess)
            }
        }

    }

    given("send to remove queue") {

        on("invalid success sent to remove queue") {
            it("should display success removed") {
                val position = 0
                val success = null
                presenter.sendToRemoveQueue(position, success)
                verify(view, never()).displaySuccessRemoved(position, Success())
            }
        }

        on("valid successes sent to remove queue") {
            it("should not display success removed") {
                val position = 0
                val success = Success()
                presenter.sendToRemoveQueue(position, success)
                verify(view, times(1)).displaySuccessRemoved(position, success)
            }
        }

    }

    given("update success") {

        on("successes not available") {
            it("should no update success") {
                val position = 0
                val successes = arrayListOf<Success>()
                presenter.updateSuccess(position, successes)
                verify(view, never()).displaySuccessChanged(position, Success())
            }
        }

        on("invalid clicked position") {
            it("should not update success") {
                val position = -10
                val successes = arrayListOf(Success())
                presenter.updateSuccess(position, successes)
                verify(view, never()).displaySuccessRemoved(position, Success())
            }
        }

        on("valid clicked position and successes available") {
            it("should display success removed") {
                val position = 0
                val successes = arrayListOf(Success())
                val updatedSuccess = Success()
                `when`(successesRepository.fetchSuccess(successes[position].id)).thenReturn(updatedSuccess)
                presenter.updateSuccess(position, successes)
                verify(view, times(1)).displaySuccessChanged(position, updatedSuccess)
            }
        }

    }
    given("category picked") {

        it("should display category") {
            val category = "business"
            presenter.categoryPicked(category)
            verify(view, times(1)).displayCategory(category)
        }
    }

    given("on back pressed") {

        on("search is opened") {

            it("hide search bar and display successes") {
                val successesToDisplay = arrayListOf(Success(), Success())

                `when`(successesRepository.getSuccesses(presenter.searchFilter)).thenReturn(successesToDisplay)

                presenter.onBackPressed(true)

                verify(view, times(1)).hideSearchBar()

                verify(view, times(1)).displaySuccesses(successesToDisplay)

            }

        }

        on("search is closed") {

            it("display search bar") {
                presenter.onBackPressed(false)
                verify(view, times(1)).displaySearchBar()
            }

        }
    }

    given("action search") {

        on("search is opened") {
            it("on back pressed") {

                val isSearchOpened = true
                val mockedMenuItem = mock(MenuItem::class.java)
                whenever(mockedMenuItem.itemId).thenReturn(R.id.action_search)

                presenterSpy.handleOptionsItemSelected(mockedMenuItem, isSearchOpened)
                verify(presenterSpy, times(1)).onBackPressed(isSearchOpened)
            }
        }

        on("search is closed") {
            it("on back pressed") {

                val isSearchOpened = false
                val mockedMenuItem = mock(MenuItem::class.java)
                whenever(mockedMenuItem.itemId).thenReturn(R.id.action_search)

                presenterSpy.handleOptionsItemSelected(mockedMenuItem, isSearchOpened)
                verify(presenterSpy, times(1)).onBackPressed(isSearchOpened)
            }
        }


    }

    given("show search") {

        it("display search") {

            presenter.showSearch()
            verify(view, times(1)).displaySearch()
        }
    }

})
