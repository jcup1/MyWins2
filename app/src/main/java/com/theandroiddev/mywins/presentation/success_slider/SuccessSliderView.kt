package com.theandroiddev.mywins.presentation.success_slider

import com.theandroiddev.mywins.core.mvp.MvpView
import com.theandroiddev.mywins.presentation.successes.SuccessModel

interface SuccessSliderView : MvpView {

    fun displaySuccesses(successes: MutableList<SuccessModel>, position: Int)

    fun displayEditSuccessActivity(id: Long)
}