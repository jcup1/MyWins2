package com.theandroiddev.mywins.success_slider

import com.github.ajalt.timberkt.d
import com.theandroiddev.mywins.data.models.SearchFilter
import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.repositories.SuccessesRepository
import com.theandroiddev.mywins.mvp.MvpPresenter
import javax.inject.Inject

/**
 * Created by jakub on 12.11.17.
 */

class SuccessSliderPresenter @Inject
constructor(
        private var successesRepository: SuccessesRepository
) : MvpPresenter<SuccessSliderView>() {

    fun onExtrasLoaded(searchFilter: SearchFilter, position: Int) {
        val successes = successesRepository.getSuccesses(searchFilter)

        ifViewAttached { view ->
            view.displaySuccesses(successes, position)
        }
    }

    fun onCreate() {
        successesRepository.openDB()
    }

    fun onDestroy() {
        successesRepository.closeDB()
    }

    fun sliderFabClicked(currentItem: Int, successes: ArrayList<Success>?) {
        if (successes != null) {
            d { "sliderFabClicked: id " + successes[currentItem].id }
            ifViewAttached { view ->
                view.displayEditSuccessActivity(successes[currentItem].id ?: 0)
            }
        }
    }

    fun onRequestCodeInsert() {

    }

}
