package com.theandroiddev.mywins.presentation.success_slider

import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.mvp.MvpView

interface SuccessSliderView : MvpView {

    fun displaySuccesses(successes: MutableList<SuccessEntity>, position: Int)

    fun displayEditSuccessActivity(id: Long)
}