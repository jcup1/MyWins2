package com.theandroiddev.mywins.presentation.success_slider

import com.github.ajalt.timberkt.d
import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.mvp.MvpPresenter
import javax.inject.Inject

/**
 * Created by jakub on 12.11.17.
 */

class SuccessSliderPresenter @Inject constructor() : MvpPresenter<SuccessSliderView>() {

    fun onExtrasLoaded(successes: MutableList<SuccessEntity>, position: Int) {

        ifViewAttached { view ->
            view.displaySuccesses(successes, position)
        }

    }

    fun sliderFabClicked(currentItem: Int, successes: MutableList<SuccessEntity>?) {
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
