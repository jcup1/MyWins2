package com.theandroiddev.mywins.success_slider

import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.models.SuccessImage
import com.theandroiddev.mywins.mvp.MvpView

interface SuccessSliderFragmentView : MvpView {

    fun displaySuccessData(success: Success, successImageList: ArrayList<SuccessImage>)

    fun startImageActivity(imagePaths: ArrayList<String>, position: Int)

}