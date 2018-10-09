package com.theandroiddev.mywins.presentation.success_slider

import com.theandroiddev.mywins.core.mvp.MvpView
import com.theandroiddev.mywins.presentation.successes.SuccessImageModel
import com.theandroiddev.mywins.presentation.successes.SuccessModel

interface SuccessSliderFragmentView : MvpView {

    fun displaySuccessData(
            success: SuccessModel,
            successImages: List<SuccessImageModel>
    )

    fun startImageActivity(position: Int, imagePaths: ArrayList<String>)

}