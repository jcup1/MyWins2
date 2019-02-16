package com.theandroiddev.mywins.UI.activities

import com.nhaarman.mockito_kotlin.whenever
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.domain.service.shared_preferences.SharedPreferencesService
import com.theandroiddev.mywins.domain.service.successes.SuccessesService
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceArgument
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceModel
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceResult
import com.theandroiddev.mywins.domain.service.successes.toModel
import com.theandroiddev.mywins.presentation.successes.SortType
import com.theandroiddev.mywins.presentation.successes.SuccessCategory
import com.theandroiddev.mywins.presentation.successes.SuccessModel
import com.theandroiddev.mywins.presentation.successes.SuccessesPresenter
import com.theandroiddev.mywins.presentation.successes.SuccessesView
import com.theandroiddev.mywins.presentation.successes.toSuccessesServiceModel
import com.theandroiddev.mywins.utils.SearchFilter
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals

/**
 * Created by jakub on 28.10.17.
 */
class SuccessesPresenterTest : Spek({

    lateinit var sut: SuccessesPresenter

    val successesService = Mockito.mock(SuccessesService::class.java)
    val preferencesHelper = mock(SharedPreferencesService::class.java)
    val view = Mockito.mock(SuccessesView::class.java)

    beforeGroup {

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        sut = SuccessesPresenter(successesService, preferencesHelper)
        sut.attachView(view)
    }

    Feature("adding a new success") {
        var category = SuccessCategory.OTHER

        Scenario("proper category id passed") {
            Given("business category") {
                category = SuccessCategory.BUSINESS
            }
            When("fab category selected") {
                sut.onFabCategorySelected(category.id)
            }
            Then("should display category") {
                verify(view, times(1)).displayCategory(category)
            }
        }

        Scenario("wrong category id passed") {
            When("fab category selected") {
                sut.onFabCategorySelected(-2000)
            }
            Then("should display 'other' category") {
                verify(view, times(1)).displayCategory(SuccessCategory.OTHER)
            }
        }
    }

    Feature("loading Successes") {
        var successList = listOf<SuccessesServiceModel>()
        var searchFilter = SearchFilter()
        val searchTerm = ""

        Scenario("Successes are available and displayed") {
            Given("list of successes") {
                successList = listOf(SuccessesServiceModel(), SuccessesServiceModel())
                searchFilter = SearchFilter(SortType.DATE_ADDED, true)
            }
            When("getting successes") {


                whenever(successesService.getSuccesses(searchTerm, searchFilter))
                    .thenReturn(SuccessesServiceResult.Successes(successList).asSingle())
                whenever(successesService.getFilters()).thenReturn(searchFilter)

                sut.onSearchTextChanged(searchTerm)
            }
            Then("should display 2 Successes") {
                verify(view, times(1)).displaySuccesses(successList.map { it.toModel() })
            }
        }

        Scenario("No Successes available, TextView displayed") {
            Given("empty list") {
                successList = listOf()
                searchFilter = SearchFilter()
            }
            When("getting successes") {
                whenever(successesService.getFilters()).thenReturn(SearchFilter())

                whenever(successesService.getSuccesses(searchTerm, searchFilter))
                    .thenReturn(SuccessesServiceResult.Successes(successList).asSingle())
                sut.onSearchTextChanged(searchTerm)
            }
            Then("should display no Successes") {
                verify(view, times(1)).displaySuccesses(listOf())
            }
            Then("should display no successes available TextView") {
                assertEquals(view.isSuccessListVisible, false)
            }
        }

    }

    Feature("Removing successes from queue") {
        var successToRemove = SuccessModel()
        var argument = SuccessesServiceArgument.Successes(listOf())

        var backupSuccess = SuccessModel()
        var position = 0
        var successesSize = 5

        Scenario("App pauses and removes successes from queue") {
            Given("list of successes to remove") {
                successToRemove = SuccessModel()
                argument =
                    SuccessesServiceArgument.Successes(listOf(successToRemove).map { it.toSuccessesServiceModel() })
            }
            When("activity pauses") {
                whenever(successesService.removeSuccesses(argument)).thenReturn(Completable.complete())
                sut.onPause(mutableListOf(successToRemove))
            }
            Then("should remove Successes") {
                verify(
                    successesService,
                    times(1)
                ).removeSuccesses(argument)
            }
        }

        Scenario("App pauses and have no successes to remove") {
            Given("empty list of successes to remove") {
                argument =
                    SuccessesServiceArgument.Successes(listOf<SuccessModel>().map { it.toSuccessesServiceModel() })
            }
            When("activity pauses") {
                whenever(successesService.removeSuccesses(argument)).thenReturn(Completable.complete())
                sut.onPause(mutableListOf(successToRemove))
            }
            Then("should remove no Successes") {
                verify(
                    successesService,
                    never()
                ).removeSuccesses(argument)
            }
        }

        Scenario("Add successes to remove queue") {
            Given("proper data") {
                backupSuccess = SuccessModel()
                position = 0
                successesSize = 5
            }
            When("adding success to remove queue") {
                sut.onSuccessAddedToRemoveQueue(position, backupSuccess, successesSize)
            }
            Then("should remove Successes") {
                verify(view).removeSuccess(position, backupSuccess)
            }
            Then("should not display error") {
                verify(view, never()).alerts?.displayUnexpectedError()
            }
        }

        Scenario("Too small position to add success to remove queue") {
            Given("too small position") {
                backupSuccess = SuccessModel()
                position = -1
                successesSize = 5
            }
            When("adding success to remove queue") {
                sut.onSuccessAddedToRemoveQueue(position, backupSuccess, successesSize)
            }
            Then("should not remove Successes") {
                verify(view, never()).removeSuccess(position, backupSuccess)
            }
            Then("should display error") {
                verify(view).alerts?.displayUnexpectedError()
            }
        }

        Scenario("Too big position to add success to remove queue") {
            Given("too big position") {
                backupSuccess = SuccessModel()
                position = 5
                successesSize = 5
            }
            When("adding success to remove queue") {
                sut.onSuccessAddedToRemoveQueue(position, backupSuccess, successesSize)
            }
            Then("should not remove Successes") {
                verify(view, never()).removeSuccess(position, backupSuccess)
            }

            Then("should display error") {
                verify(view, times(2)).alerts?.displayUnexpectedError()
            }
        }

        Scenario("Can't add success to remove queue because success is null") {
            Given("null success") {
                position = 5
                successesSize = 5
            }
            When("adding success to remove queue") {
                sut.onSuccessAddedToRemoveQueue(position, backupSuccess, successesSize)
            }
            Then("should display error") {
                verify(view, times(3)).alerts?.displayUnexpectedError()
            }
        }

        Scenario("Undo to remove success failed") {
            Given("null success") {

            }
            When("undoing success remove") {
                sut.onUndoToRemove(0, null)
            }
            Then("should not undo removing success") {
                verify(view, never()).restoreSuccess(0, SuccessModel())
            }
        }

        Scenario("Undo to remove success succeed") {
            Given("valid success") {
                backupSuccess = SuccessModel()
            }
            When("undoing success remove") {
                sut.onUndoToRemove(0, backupSuccess)
            }
            Then("should not undo removing success") {
                verify(view, times(1)).restoreSuccess(0, backupSuccess)
            }
        }

    }

    Feature("Successes filters") {
        val customization = SearchFilter(SortType.DATE_ADDED, true)
        var newCustomization = SearchFilter()
        val successServiceResult =
            SuccessesServiceResult.Successes(listOf(SuccessesServiceModel(), SuccessesServiceModel()))
        val successes = successServiceResult.successes.map { it.toModel() }

        Scenario("Starting filters view") {
            When("handling filters click") {
                whenever(successesService.getFilters()).thenReturn(SearchFilter())
                sut.handleOptionsItemSelected(R.id.action_filter, false)
            }
            Then("display filters view") {
                verify(view, times(1)).displayFiltersView(customization)
            }
        }

        Scenario("New filters not saved") {
            Given("the same filters customization") {
                newCustomization = SearchFilter(SortType.DATE_ADDED, true)
            }
            When("handle new filers") {
                whenever(successesService.getFilters()).thenReturn(SearchFilter())
                sut.handleNewFilters(customization)
            }
            Then("don't save new filters") {
                verify(successesService, never()).saveFilters(newCustomization, customization)
            }
            Then("don't display successes") {
                verify(view, times(1)).displaySuccesses(successes)
            }
        }

        Scenario("New filters saved") {
            Given("different filters customization") {
                newCustomization = SearchFilter(SortType.IMPORTANCE, false)
            }
            When("handle new filers") {
                whenever(successesService.getFilters()).thenReturn(customization)
                whenever(successesService.getSuccesses("", newCustomization)).thenReturn(successServiceResult.asSingle())
                sut.handleNewFilters(newCustomization)
            }
            Then("save new filters") {
                verify(successesService, times(1)).saveFilters(newCustomization, customization)
            }
            Then("load successes") {
                verify(view, atLeastOnce()).displaySuccesses(successes)
            }

        }

        Scenario("Get inactive filters") {
            When("getting filters") {
                whenever(successesService.getSuccesses("", SearchFilter())).thenReturn(successServiceResult.asSingle())
                sut.searchFilter = SearchFilter()
            }
            Then("return inactive filters") {
                verify(successesService, atLeastOnce()).getFilters()
            }
            Then("display successes") {
                verify(view, atLeastOnce()).displaySuccesses(successes)
            }
            Then("display filters inactive") {
                verify(view, times(1)).areFiltersActive = false
            }
        }

        Scenario("Get active filters") {
            When("getting filters") {
                whenever(successesService.getSuccesses("", SearchFilter())).thenReturn(successServiceResult.asSingle())
                sut.searchFilter = SearchFilter()
            }
            Then("return active filters") {
                verify(successesService, atLeastOnce()).getFilters()
            }
            Then("display successes") {
                verify(view, atLeastOnce()).displaySuccesses(successes)
            }
            Then("display filters active") {
                verify(view, times(1)).areFiltersActive = true
            }
        }

    }

    Feature("Updating success") {
        var position = -1
        var successes = mutableListOf<SuccessModel>()
        var id: Long = -1
        Scenario("Successes not available") {
            Given("empty list of successes") {
                position = 0
                successes = mutableListOf()
            }
            When("updating success") {
                sut.updateSuccess(position, successes)
            }
            Then("should no update success") {
                verify(view, never()).displaySuccessChanged(position, SuccessModel())
            }
        }

        Scenario("invalid clicked position") {
            Given("negative position") {
                position = -10
                successes = mutableListOf(SuccessModel(id = 123))
            }
            When("updating success") {
                sut.updateSuccess(position, successes)
            }
            Then("should not update success") {
                verify(view, never()).removeSuccess(position, SuccessModel(id = 123))
            }
        }

        Scenario("success id is null") {
            Given("null success") {
                position = 0
                successes = mutableListOf(SuccessModel())
            }
            When("updating success") {
                sut.updateSuccess(position, successes)
            }
            Then("should not update success") {
                verify(view, never()).displaySuccessChanged(position, successes[position])
            }
            Then("should display error") {
                verify(view, times(5)).alerts?.displayUnexpectedError()
            }

        }
        Scenario("passed valid data") {
            Given("proper data") {
                position = 0
                successes = mutableListOf(SuccessModel(id = 1234))
                id = successes[position].id ?: 0
            }
            When("updating success") {
                whenever(successesService.fetchSuccess(id))
                    .thenReturn(SuccessesServiceResult.Successes(successes.map { it.toSuccessesServiceModel() }).asSingle())
                sut.updateSuccess(position, successes.toMutableList())
            }
            Then("should update success") {
                verify(view, times(1)).displaySuccessChanged(position, successes[position])
            }

        }

    }

    Feature("Successes search bar") {
        var successesToDisplay = mutableListOf<SuccessesServiceModel>()
        var isSearchOpened = false
        val customization = SearchFilter()
        val searchTerm = ""

        Scenario("On back pressed closes search bar and loads new list") {
            Given("valid successes to display") {
                successesToDisplay = mutableListOf(SuccessesServiceModel(), SuccessesServiceModel())
            }
            When("getting successes") {
                whenever(successesService.getSuccesses(searchTerm, sut.searchFilter))
                    .thenReturn(SuccessesServiceResult.Successes(successesToDisplay).asSingle())
                sut.handleBackPress(false, true)
            }
            Then("hide search bar") {
                verify(view, times(1)).hideSearchBar()

            }
            Then("display successes") {
                verify(view, atLeastOnce()).displaySuccesses(successesToDisplay.map { it.toModel() })
            }
        }

        Scenario("Search icon click opens search bar") {
            Given("search is closed") {
                isSearchOpened = false
            }
            When("clicking action search") {
                whenever(successesService.getSuccesses(searchTerm, customization)).thenReturn(
                    SuccessesServiceResult.Successes(
                        listOf(SuccessesServiceModel())
                    ).asSingle()
                )
                sut.handleOptionsItemSelected(R.id.action_search, isSearchOpened)
            }
            Then("display search bar") {
                verify(view, times(1)).displaySearchBar()
            }
        }

        Scenario("Search icon click closes search bar") {
            Given("search is opened") {
                isSearchOpened = true
            }
            When("clicking action search") {
                whenever(successesService.getSuccesses(searchTerm, customization)).thenReturn(
                    SuccessesServiceResult.Successes(
                        listOf()
                    ).asSingle()
                )
                sut.handleOptionsItemSelected(R.id.action_search, isSearchOpened)
            }
            Then("hide search bar") {
                //initial property = false and call
                verify(view, times(2)).hideSearchBar()
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

