package com.theandroiddev.mywins.UI.activities

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
            val category = Constants.Companion.Category.BUSINESS
            SUT.onFabCategorySelected(category.id)
            it("should display category") {
                verify(view, times(1)).displayCategory(category)
            }
        }

        on("wrong category id passed") {
            SUT.onFabCategorySelected(-2000)
            it("should display 'other' category") {
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
            val searchFilter = SearchFilter("", Constants.Companion.SortType.DATE_ADDED, true)
            `when`(successesService.getSuccesses(searchFilter))
                .thenReturn(SuccessesServiceResult.Successes(successList).asSingle())

            SUT.onSearchTextChanged("")

            it("should display 2 Successes") {
                verify(view, times(1)).displaySuccesses(successList.map { it.toModel() })
            }
        }

        on("no Successes") {
            val successList = listOf<SuccessesServiceModel>()
            val searchFilter = SearchFilter()
            `when`(successesService.getSuccesses(searchFilter))
                .thenReturn(SuccessesServiceResult.Successes(successList).asSingle())

            SUT.onSearchTextChanged("")

            it("should display no Successes") {
                verify(view, times(1)).displaySuccesses(listOf())
            }
        }

    }

    given("pause") {

        on("passed valid successes") {
            val successToRemove = SuccessModel()

            val argument = SuccessesServiceArgument.Successes(
                listOf(successToRemove.toSuccessesServiceModel())
            )

            whenever(successesService.removeSuccesses(argument)).thenReturn(Completable.complete())
            SUT.onPause(mutableListOf(successToRemove))

            it("should remove Successes") {
                verify(
                    successesService,
                    times(1)
                ).removeSuccesses(argument)
            }
        }

        on("passed empty list") {
            val successesToRemove = mutableListOf<SuccessModel>()

            SUT.onPause(successesToRemove)

            val serviceModel =
                SuccessesServiceArgument.Successes(successesToRemove.map { it.toSuccessesServiceModel() })

            it("should not remove Successes") {
                verify(
                    successesService,
                    never()
                ).removeSuccesses(serviceModel)
            }
        }
    }

    given("filters clicked") {

        on("") {
            it("") {

            }
        }
    }

    given("add success to remove queue") {

        on("proper data") {
            val backupSuccess = SuccessModel()
            val position = 0
            val successesSize = 5
            SUT.onSuccessAddedToRemoveQueue(position, backupSuccess, successesSize)

            it("should remove Successes") {
                verify(view).removeSuccess(position, backupSuccess)
            }
            it("should not display error") {
                verify(view, never()).alerts?.displayUnexpectedError()
            }
        }

        on("too small position") {
            val backupSuccess = SuccessModel()
            val position = -1
            val successesSize = 5
            SUT.onSuccessAddedToRemoveQueue(position, backupSuccess, successesSize)

            it("should not remove Successes") {
                verify(view, never()).removeSuccess(position, backupSuccess)
            }
            it("should display error") {
                verify(view).alerts?.displayUnexpectedError()
            }
        }

        on("too big position") {
            val backupSuccess = SuccessModel()
            val position = 5
            val successesSize = 5
            SUT.onSuccessAddedToRemoveQueue(position, backupSuccess, successesSize)

            it("should not remove Successes") {
                verify(view, never()).removeSuccess(position, backupSuccess)
            }

            it("should display error") {
                verify(view).alerts?.displayUnexpectedError()
            }
        }

        on("success is null") {
            val backupSuccess = null
            val position = 5
            val successesSize = 5
            SUT.onSuccessAddedToRemoveQueue(position, backupSuccess, successesSize)

            it("should display error") {
                verify(view).alerts?.displayUnexpectedError()
            }
        }
    }

    given("undo to remove") {

        on("backup success is null") {
            val backupSuccess = null
            SUT.onUndoToRemove(0, backupSuccess)

            it("should not undo removing success") {
                verify(view, never()).restoreSuccess(0, SuccessModel())

            }
        }

        on("backup success is not null") {
            val backupSuccess = SuccessModel()
            SUT.onUndoToRemove(0, backupSuccess)

            it("should undo removing success") {
                verify(view, times(1)).restoreSuccess(0, backupSuccess)
            }
        }

    }

    given("update success") {

        on("Successes not available") {
            val position = 0
            val successes = mutableListOf<SuccessModel>()
            SUT.updateSuccess(position, successes)

            it("should no update success") {
                verify(view, never()).displaySuccessChanged(position, SuccessModel())
            }
        }

        on("invalid clicked position") {
            val position = -10
            val successes = mutableListOf(SuccessModel(id = 123))
            SUT.updateSuccess(position, successes)

            it("should not update success") {
                verify(view, never()).removeSuccess(position, SuccessModel(id = 123))
            }
        }

        on("success id is null") {
            val position = 0
            val successes = listOf(SuccessModel())

            SUT.updateSuccess(position, successes.toMutableList())

            it("should not update success") {
                verify(view, never()).displaySuccessChanged(position, successes[position])
            }
            it("should display error") {
                verify(view).alerts?.displayUnexpectedError()
            }

        }
        on("passed valid data") {
            val position = 0
            val successes = listOf(SuccessModel(id = 1234))

            val id = successes[position].id ?: 0

            `when`(successesService.fetchSuccess(id))
                .thenReturn(SuccessesServiceResult.Successes(successes.map { it.toSuccessesServiceModel() }).asSingle())
            SUT.updateSuccess(position, successes.toMutableList())

            it("should update success") {
                verify(view, times(1)).displaySuccessChanged(position, successes[position])
            }

        }

    }

    given("on back pressed") {

        on("search is opened") {
            val successesToDisplay =
                mutableListOf(SuccessesServiceModel(), SuccessesServiceModel())

            `when`(successesService.getSuccesses(SUT.searchFilter))
                .thenReturn(SuccessesServiceResult.Successes(successesToDisplay).asSingle())

            SUT.handleBackPress(false, true)

            it("hide search bar") {
                verify(view, times(1)).hideSearchBar()

            }

            it("display successes") {
                verify(view, times(1)).displaySuccesses(successesToDisplay.map { it.toModel() })
            }

        }

        on("search is closed") {
            whenever(successesService.getSuccesses(SearchFilter())).thenReturn(
                SuccessesServiceResult.Successes(
                    listOf(SuccessesServiceModel())
                ).asSingle()
            )

            SUT.handleOptionsItemSelected(R.id.action_search, false)

            it("display search bar") {
                verify(view, times(1)).displaySearchBar()
            }

        }
    }

    given("action start search") {
        on("search is closed") {
            whenever(successesService.getSuccesses(SearchFilter())).thenReturn(
                SuccessesServiceResult.Successes(
                    listOf()
                ).asSingle()
            )
            val isSearchOpened = false

            SUT.handleOptionsItemSelected(R.id.action_search, isSearchOpened)

            it("display search bar") {
                verify(view, times(1)).displaySearchBar()
            }
        }

    }

    given("action search") {
        on("search is opened") {
            whenever(successesService.getSuccesses(SearchFilter())).thenReturn(
                SuccessesServiceResult.Successes(
                    listOf()
                ).asSingle()
            )
            val isSearchOpened = true
            SUT.handleOptionsItemSelected(R.id.action_search, isSearchOpened)
            it("hide search bar") {
                verify(view, times(1)).hideSearchBar()
            }
        }

        on("search is closed") {
            whenever(successesService.getSuccesses(SearchFilter())).thenReturn(
                SuccessesServiceResult.Successes(
                    listOf()
                ).asSingle()
            )
            val isSearchOpened = false
            it("display search") {
                SUT.handleOptionsItemSelected(R.id.action_search, isSearchOpened)
                verify(view, times(1)).displaySearchBar()
            }
        }

    }
})

fun <T> T.asFlowable(): Flowable<T> {
    return Flowable.just(this)
}

fun <T> T.asSingle(): Single<T> {
    return Single.just(this)
}

