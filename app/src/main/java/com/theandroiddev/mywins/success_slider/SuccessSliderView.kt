package com.theandroiddev.mywins.success_slider

import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.mvp.MvpView
import java.util.*

interface SuccessSliderView : MvpView {

    fun displaySuccesses(successes: ArrayList<Success>, position: Int)

    fun displayEditSuccessActivity(id: String)
}