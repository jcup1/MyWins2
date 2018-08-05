package com.theandroiddev.mywins.edit_success

import com.theandroiddev.mywins.data.models.SuccessImage
import com.theandroiddev.mywins.mvp.MvpView
import java.util.*

interface EditSuccessView : MvpView {

    fun displaySlider()

    fun displaySuccessImages(successImageList: ArrayList<SuccessImage>)
}