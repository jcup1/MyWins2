package com.theandroiddev.mywins.UI.activities

import android.view.MenuItem
import com.nhaarman.mockito_kotlin.whenever
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.domain.service.shared_preferences.SharedPreferencesService
import com.theandroiddev.mywins.domain.service.successes.*
import com.theandroiddev.mywins.presentation.successes.SuccessModel
import com.theandroiddev.mywins.presentation.successes.SuccessesPresenter
import com.theandroiddev.mywins.presentation.successes.SuccessesView
import com.theandroiddev.mywins.presentation.successes.toSuccessesServiceModel
import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.SearchFilter
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
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
    val presenter = Mockito.mock(SuccessesPresenter::class.java)
    val presenterSpy = spy(presenter)
    val view = Mockito.mock(SuccessesView::class.java)

    beforeEachTest {

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

    }

    afterEachTest {
        reset(successesRepository)
        reset(preferencesHelper)
        reset(view)
        presenter.attachView(view)
    }

    presenter.attachView(view)

    given("loading Successes") {

        on("Successes available") {
            val successList = listOf(
                SuccessesServiceModel(),
                SuccessesServiceModel()
            )

            it("should display 2 Successes") {
                val searchFilter = SearchFilter(null, Constants.Companion.SortType.DATE_ADDED, true)
                `when`(successesRepository.getSuccesses(searchFilter))
                    .thenReturn(SuccessesServiceResult.Successes(successList).asFlowable())

                presenter.loadSuccesses(searchFilter)

                verify(view, times(1)).displaySuccesses(successList.map { it.toModel() })
            }
        }

        on("no Successes") {
            val successList = listOf<SuccessesServiceModel>()

            it("should display no Successes") {
                val searchFilter = SearchFilter(null, Constants.Companion.SortType.DATE_ADDED, true)
                `when`(successesRepository.getSuccesses(searchFilter))
                    .thenReturn(SuccessesServiceResult.Successes(successList).asFlowable())

                presenter.loadSuccesses(searchFilter)

                verify(view, times(1)).displayNoSuccesses()
            }
        }

    }

    given("pause") {

        on("success was backed up") {
            it("should remove Successes") {
                val successes = mutableListOf(SuccessModel(), SuccessModel())
                presenter.onPause(successes)
                verify(
                    successesRepository,
                    times(1)
                ).removeSuccesses(SuccessesServiceArgument.Successes(successes.map { it.toSuccessesServiceModel() }))
            }
        }

        on("success wasn't backed up") {
            it("should not remove Successes") {
                val successes = mutableListOf<SuccessModel>()

                presenter.onPause(successes)
                verify(
                    successesRepository,
                    never()
                ).removeSuccesses(SuccessesServiceArgument.Successes(successes.map { it.toSuccessesServiceModel() }))
            }
        }
    }

    given("undo to remove") {

        on("backup success is null") {
            it("should not undo removing success") {

                val backupSuccess = null
                presenter.onUndoToRemove(0, backupSuccess)
                verify(view, never()).restoreSuccess(0, SuccessModel())

            }
        }

        on("backup success is not null") {
            it("should undo removing success") {

                val backupSuccess = SuccessModel()
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
                verify(view, never()).removeSuccess(position, SuccessModel())
            }
        }

        on("valid Successes sent to remove queue") {
            it("should not display success removed") {
                val position = 0
                val success = SuccessModel()
                presenter.sendToRemoveQueue(position, success)
                verify(view, times(1)).removeSuccess(position, success)
            }
        }

    }

    given("update success") {

        on("Successes not available") {
            it("should no update success") {
                val position = 0
                val successes = mutableListOf<SuccessModel>()
                presenter.updateSuccess(position, successes)
                verify(view, never()).displaySuccessChanged(position, SuccessModel())
            }
        }

        on("invalid clicked position") {
            it("should not update success") {
                val position = -10
                val successes = mutableListOf(SuccessModel())
                presenter.updateSuccess(position, successes)
                verify(view, never()).removeSuccess(position, SuccessModel())
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
            val category = Constants.Companion.Category.BUSINESS
            presenter.categoryPicked(category)
            verify(view, times(1)).displayCategory(category)
        }
    }

    given("on back pressed") {

        on("search is opened") {

            it("hide search bar and display Successes") {
                val successesToDisplay = mutableListOf(SuccessesServiceModel(), SuccessesServiceModel())

                `when`(successesRepository.getSuccesses(presenter.searchFilter))
                    .thenReturn(SuccessesServiceResult.Successes(successesToDisplay).asFlowable())

                presenter.toggleSearchBar(true)

                verify(view, times(1)).hideSearchBar()

                verify(view, times(1)).displaySuccesses(successesToDisplay.map { it.toModel() })

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

