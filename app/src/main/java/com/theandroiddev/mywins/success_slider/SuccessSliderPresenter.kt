package com.theandroiddev.mywins.success_slider

import com.github.ajalt.timberkt.d
import com.theandroiddev.mywins.data.models.SearchFilter
import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.repositories.SuccessesRepository
import com.theandroiddev.mywins.mvp.MvpPresenter
import java.util.*
import javax.inject.Inject

/**
 * Created by jakub on 12.11.17.
 */

class SuccessSliderPresenter @Inject
constructor(
        private var successesRepository: SuccessesRepository
) : MvpPresenter<SuccessSliderView>() {

    private var successList: ArrayList<Success> = ArrayList()

    fun loadSuccesses(searchFilter: SearchFilter) {
        successList = successesRepository.getSuccesses(searchFilter)

        ifViewAttached { view ->
            view.displaySuccesses(successList)
        }
    }

    fun openDB() {
        successesRepository.openDB()
    }

    fun closeDB() {
        successesRepository.closeDB()
    }

    fun startEditSuccess(currentItem: Int) {
        d { "startEditSuccess: id " + successList[currentItem].id }
        ifViewAttached { view ->
            view.displayEditSuccessActivity(successList[currentItem].id)
        }
    }

}
