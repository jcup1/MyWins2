package com.theandroiddev.mywins.UI.activities

import android.view.MenuItem
import com.nhaarman.mockito_kotlin.whenever
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.data.model.SuccessEntity
import com.theandroiddev.mywins.domain.service.shared_preferences.SharedPreferencesService
import com.theandroiddev.mywins.domain.service.successes.SuccessesService
import com.theandroiddev.mywins.presentation.successes.SuccessesPresenter
import com.theandroiddev.mywins.presentation.successes.SuccessesView
import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.SearchFilter
import io.reactivex.Flowable
import io.reactivex.Single
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

    val successesRepository = Mockito.mock(SuccessesService::class.java)
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

    given("loading Successes") {

        on("Successes available") {
            val successList = mutableListOf<SuccessEntity>()

            successList.add(SuccessEntity())
            successList.add(SuccessEntity())

            it("should display 2 Successes") {
                val searchFilter = SearchFilter(null, Constants.SORT_DATE_ADDED, true)
                `when`(successesRepository.getSuccesses(searchFilter))
                        .thenReturn(successList.asFlowable())

                presenter.loadSuccesses(searchFilter)

                verify(view, times(1)).displaySuccesses(successList)
            }
        }

        on("no Successes") {
            val successList = mutableListOf<SuccessEntity>()

            it("should display no Successes") {
                val searchFilter = SearchFilter(null, Constants.SORT_DATE_ADDED, true)
                `when`(successesRepository.getSuccesses(searchFilter))
                        .thenReturn(successList.asFlowable())

                presenter.loadSuccesses(searchFilter)

                verify(view, times(1)).displayNoSuccesses()
            }
        }

    }

    given("pause") {

        on("success was backed up") {
            it("should remove Successes") {
                val successes = mutableListOf(SuccessEntity(), SuccessEntity())
                presenter.onPause(successes)
                verify(successesRepository, times(1)).removeSuccesses(successes)
            }
        }

        on("success wasn't backed up") {
            it("should not remove Successes") {
                val successes: MutableList<SuccessEntity>? = null

                presenter.onPause(successes)
                verify(successesRepository, never()).removeSuccesses(mutableListOf())
            }
        }
    }

    given("undo to remove") {

        on("backup success is null") {
            it("should not undo removing success") {

                val backupSuccess = null
                presenter.onUndoToRemove(0, backupSuccess)
                verify(view, never()).restoreSuccess(0, SuccessEntity())

            }
        }

        on("backup success is not null") {
            it("should undo removing success") {

                val backupSuccess = SuccessEntity()
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
                verify(view, never()).displaySuccessRemoved(position, SuccessEntity())
            }
        }

        on("valid Successes sent to remove queue") {
            it("should not display success removed") {
                val position = 0
                val success = SuccessEntity()
                presenter.sendToRemoveQueue(position, success)
                verify(view, times(1)).displaySuccessRemoved(position, success)
            }
        }

    }

    given("update success") {

        on("Successes not available") {
            it("should no update success") {
                val position = 0
                val successes = mutableListOf<SuccessEntity>()
                presenter.updateSuccess(position, successes)
                verify(view, never()).displaySuccessChanged(position, SuccessEntity())
            }
        }

        on("invalid clicked position") {
            it("should not update success") {
                val position = -10
                val successes = mutableListOf(SuccessEntity())
                presenter.updateSuccess(position, successes)
                verify(view, never()).displaySuccessRemoved(position, SuccessEntity())
            }
        }
//TODO fix tests
//        on("valid clicked position and Successes available") {
//            it("should display success removed") {
//                val position = 0
//                val updatedSuccess = SuccessEntity(
//                        123,
//                        "",
//                        "",
//                        "",
//                        "",
//                        "",
//                        0
//                )
//
//                val Successes = mutableListOf(updatedSuccess)
//
//                val id = Successes[position].id
//                if(id == null) {
//                    Assert.fail()
//                } else {
//                    `when`(successesRepository.fetchSuccess(id))
//                            .thenReturn(updatedSuccess.asSingle())
//                    presenter.updateSuccess(position, Successes)
//                    verify(view, times(1)).displaySuccessChanged(position, updatedSuccess)
//                }
//
//            }
//        }

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

            it("hide search bar and display Successes") {
                val successesToDisplay = mutableListOf(SuccessEntity(), SuccessEntity())

                `when`(successesRepository.getSuccesses(presenter.searchFilter))
                        .thenReturn(successesToDisplay.asFlowable())

                presenter.toggleSearchBar(true)

                verify(view, times(1)).hideSearchBar()

                verify(view, times(1)).displaySuccesses(successesToDisplay)

            }

        }

        on("search is closed") {

            it("display search bar") {
                presenter.toggleSearchBar(false)
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
                verify(presenterSpy, times(1)).toggleSearchBar(isSearchOpened)
            }
        }

        on("search is closed") {
            it("on back pressed") {

                val isSearchOpened = false
                val mockedMenuItem = mock(MenuItem::class.java)
                whenever(mockedMenuItem.itemId).thenReturn(R.id.action_search)

                presenterSpy.handleOptionsItemSelected(mockedMenuItem, isSearchOpened)
                verify(presenterSpy, times(1)).toggleSearchBar(isSearchOpened)
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

fun <T> T.asFlowable(): Flowable<T> {
    return Flowable.just(this)
}

fun <T> T.asSingle(): Single<T> {
    return Single.just(this)
}

