package com.theandroiddev.mywins.UI.activities

import android.view.MenuItem
import com.nhaarman.mockito_kotlin.whenever
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.domain.service.shared_preferences.SharedPreferencesService
import com.theandroiddev.mywins.domain.service.successes.SuccessesService
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceArgument
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceModel
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceResult
import com.theandroiddev.mywins.domain.service.successes.toModel
import com.theandroiddev.mywins.presentation.successes.SuccessModel
import com.theandroiddev.mywins.presentation.successes.SuccessesPresenter
import com.theandroiddev.mywins.presentation.successes.SuccessesView
import com.theandroiddev.mywins.presentation.successes.toSuccessesServiceModel
import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.SearchFilter
import io.reactivex.Completable
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

    lateinit var SUT: SuccessesPresenter

    val successesService = Mockito.mock(SuccessesService::class.java)
    val preferencesHelper = mock(SharedPreferencesService::class.java)
    val view = Mockito.mock(SuccessesView::class.java)

    beforeEachTest {

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

    }

    beforeEachTest {

        reset(successesService)
        reset(preferencesHelper)
        reset(view)
        SUT = SuccessesPresenter(successesService, preferencesHelper)
        SUT.attachView(view)
    }

    given("adding a new success") {
        on("proper category id passed") {
            it("should display category") {
                val category = Constants.Companion.Category.BUSINESS
                SUT.onFabCategorySelected(category.id)
                verify(view, times(1)).displayCategory(category)
            }
        }

        on("wrong category id passed") {
            it("should display 'other' category") {
                SUT.onFabCategorySelected(-2000)
                verify(view, times(1)).displayCategory(Constants.Companion.Category.OTHER)
            }
        }
    }

    given("loading Successes") {

        on("Successes available") {
            val successList = listOf(
                SuccessesServiceModel(),
                SuccessesServiceModel()
            )

            it("should display 2 Successes") {
                val searchFilter = SearchFilter("", Constants.Companion.SortType.DATE_ADDED, true)
                `when`(successesService.getSuccesses(searchFilter))
                    .thenReturn(SuccessesServiceResult.Successes(successList).asSingle())

                SUT.onSearchTextChanged("")

                verify(view, times(1)).displaySuccesses(successList.map { it.toModel() })
            }
        }

        on("no Successes") {
            val successList = listOf<SuccessesServiceModel>()

            it("should display no Successes") {
                val searchFilter = SearchFilter()
                `when`(successesService.getSuccesses(searchFilter))
                    .thenReturn(SuccessesServiceResult.Successes(successList).asSingle())

                SUT.onSearchTextChanged("")

                verify(view, times(1)).displaySuccesses(listOf())
            }
        }

    }

    given("pause") {

        on("passed successes") {
            it("should remove Successes") {
                val successToRemove = SuccessModel()

                val argument = SuccessesServiceArgument.Successes(
                    listOf(successToRemove.toSuccessesServiceModel())
                )

                whenever(successesService.removeSuccesses(argument)).thenReturn(Completable.complete())

                SUT.onPause(mutableListOf(successToRemove))

                verify(
                    successesService,
                    times(1)
                ).removeSuccesses(argument)
            }
        }

        on("passed empty list") {
            it("should not remove Successes") {
                val successesToRemove = mutableListOf<SuccessModel>()

                SUT.onPause(successesToRemove)

                val serviceModel = SuccessesServiceArgument.Successes(successesToRemove.map { it.toSuccessesServiceModel() })
                verify(
                    successesService,
                    never()
                ).removeSuccesses(serviceModel)
            }
        }
    }

    given("add success to remove queue") {

        on("success was backed up") {
            it("should remove Successes") {

            }
        }

        on("success wasn't backed up") {
            it("should not remove Successes") {

            }
        }
    }

    given("undo to remove") {

        on("backup success is null") {
            it("should not undo removing success") {

                val backupSuccess = null
                SUT.onUndoToRemove(0, backupSuccess)
                verify(view, never()).restoreSuccess(0, SuccessModel())

            }
        }

        on("backup success is not null") {
            it("should undo removing success") {

                val backupSuccess = SuccessModel()
                SUT.onUndoToRemove(0, backupSuccess)
                verify(view, times(1)).restoreSuccess(0, backupSuccess)
            }
        }

    }

    given("send to remove queue") {

        on("invalid success sent to remove queue") {
            it("should display success removed") {
                val position = 0
                val success = null
                SUT.onSuccessAddedToRemoveQueue(position, success)
                verify(view, never()).removeSuccess(position, SuccessModel())
            }
        }

        on("valid Successes sent to remove queue") {
            it("should not display success removed") {
                val position = 0
                val success = SuccessModel()
                SUT.onSuccessAddedToRemoveQueue(position, success)
                verify(view, times(1)).removeSuccess(position, success)
            }
        }

    }

    given("update success") {

        on("Successes not available") {
            it("should no update success") {
                val position = 0
                val successes = mutableListOf<SuccessModel>()
                SUT.updateSuccess(position, successes)
                verify(view, never()).displaySuccessChanged(position, SuccessModel())
            }
        }

        on("invalid clicked position") {
            it("should not update success") {
                val position = -10
                val successes = mutableListOf(SuccessModel())
                SUT.updateSuccess(position, successes)
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
//                    SUT.updateSuccess(position, Successes)
//                    verify(view, times(1)).displaySuccessChanged(position, updatedSuccess)
//                }
//
//            }
//        }

    }

    given("on back pressed") {

        on("search is opened") {

            it("hide search bar and display Successes") {
                val successesToDisplay =
                    mutableListOf(SuccessesServiceModel(), SuccessesServiceModel())

                `when`(successesService.getSuccesses(SUT.searchFilter))
                    .thenReturn(SuccessesServiceResult.Successes(successesToDisplay).asSingle())

                SUT.toggleSearchBar(true)

                verify(view, times(1)).hideSearchBar()

                verify(view, times(1)).displaySuccesses(successesToDisplay.map { it.toModel() })

            }

        }

        on("search is closed") {

            it("display search bar") {
                SUT.toggleSearchBar(false)
                verify(view, times(1)).displaySearchBar()
            }

        }
    }

    given("action search") {

        on("search is opened") {
            it("hide search bar") {

                whenever(successesService.getSuccesses(SearchFilter())).thenReturn(SuccessesServiceResult.Successes(
                    listOf()).asSingle())

                val isSearchOpened = true
                val mockedMenuItem = mock(MenuItem::class.java)
                whenever(mockedMenuItem.itemId).thenReturn(R.id.action_search)

                SUT.handleOptionsItemSelected(mockedMenuItem, isSearchOpened)
                verify(view, times(1)).hideSearchBar()
            }
        }

        on("search is closed") {
            it("on back pressed") {

                whenever(successesService.getSuccesses(SearchFilter())).thenReturn(SuccessesServiceResult.Successes(
                    listOf()).asSingle())

                val isSearchOpened = false
                val mockedMenuItem = mock(MenuItem::class.java)
                whenever(mockedMenuItem.itemId).thenReturn(R.id.action_search)

                SUT.handleOptionsItemSelected(mockedMenuItem, isSearchOpened)
                verify(view, times(1)).displaySearchBar()
            }
        }

    }

    given("show search") {

        it("display search") {

            SUT.showSearch()
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

