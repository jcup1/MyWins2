package com.theandroiddev.mywins.domain.service.successes

import com.theandroiddev.mywins.data.repository.SuccessesLocalDataSource
import com.theandroiddev.mywins.domain.service.shared_preferences.SharedPreferencesService
import com.theandroiddev.mywins.presentation.successes.SortType
import com.theandroiddev.mywins.utils.SearchFilter
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.mockito.Mockito
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class SuccessesServiceImplTest : Spek({

    val successesLocalDataSource = Mockito.mock(SuccessesLocalDataSource::class.java)
    val sharedPreferencesService = Mockito.mock(SharedPreferencesService::class.java)
    val sut = SuccessesServiceImpl(successesLocalDataSource, sharedPreferencesService)

    beforeGroup {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    Feature("Filters") {
        var oldFilers = SearchFilter()
        var newFilters = SearchFilter()
        Scenario("saving filters") {
            Given("Different filters") {
                oldFilers = SearchFilter(SortType.TITLE, true)
                newFilters = SearchFilter(SortType.IMPORTANCE, false)
            }
            When("saving filters") {
                sut.saveFilters(newFilters, oldFilers)
            }
            Then("save filters") {
                sharedPreferencesService.saveSuccessesFilters(newFilters)
            }
        }
    }

})