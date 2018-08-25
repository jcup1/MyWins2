package com.theandroiddev.mywins.presentation.success_slider

import com.github.ajalt.timberkt.d
import com.theandroiddev.mywins.core.mvp.MvpPresenter
import com.theandroiddev.mywins.presentation.successes.SuccessModel
import javax.inject.Inject

class SuccessSliderPresenter @Inject constructor() :
        MvpPresenter<SuccessSliderView, SuccessSliderBundle>() {
    override fun onViewCreated() {

    }

    fun sliderFabClicked(currentItem: Int, successes: MutableList<SuccessModel>?) {
        if (successes != null) {
            d { "sliderFabClicked: id " + successes[currentItem].id }
            ifViewAttached { view ->
                view.displayEditSuccessActivity(successes[currentItem].id ?: 0)
            }
        }
    }

    fun onRequestCodeInsert() {

    }

    fun onAfterCreate() {
        ifViewAttached { view ->
            view.displaySuccesses(bundle.successes, bundle.position)
        }
    }

}
