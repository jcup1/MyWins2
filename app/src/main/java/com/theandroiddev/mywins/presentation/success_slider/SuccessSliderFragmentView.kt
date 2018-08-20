package com.theandroiddev.mywins.presentation.success_slider

import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.data.entity.SuccessImageEntity
import com.theandroiddev.mywins.mvp.MvpView

interface SuccessSliderFragmentView : MvpView {

    fun displaySuccessData(success: SuccessEntity, successImages: MutableList<SuccessImageEntity>)

    fun startImageActivity(position: Int, imagePaths: ArrayList<String>)

}