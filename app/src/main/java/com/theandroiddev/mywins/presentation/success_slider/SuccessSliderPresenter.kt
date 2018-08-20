package com.theandroiddev.mywins.presentation.success_slider

import com.github.ajalt.timberkt.d
import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.domain.service.successes.SuccessesService
import com.theandroiddev.mywins.mvp.MvpPresenter
import com.theandroiddev.mywins.utils.SearchFilter
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by jakub on 12.11.17.
 */

class SuccessSliderPresenter @Inject
constructor(
        private var successesService: SuccessesService
) : MvpPresenter<SuccessSliderView>() {

    fun onExtrasLoaded(searchFilter: SearchFilter, position: Int) {
        successesService.getSuccesses(searchFilter)
                .observeOn(AndroidSchedulers.mainThread())
                .map { successEntities ->

                    ifViewAttached { view ->
                        view.displaySuccesses(successEntities, position)
                    }

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
