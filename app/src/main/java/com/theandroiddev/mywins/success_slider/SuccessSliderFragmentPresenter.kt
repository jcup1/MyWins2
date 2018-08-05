package com.theandroiddev.mywins.success_slider

import com.theandroiddev.mywins.data.models.SuccessImage
import com.theandroiddev.mywins.mvp.MvpPresenter
import javax.inject.Inject

class SuccessSliderFragmentPresenter @Inject constructor(
        val successImageLoader: SuccessImageLoader

) : MvpPresenter<SuccessSliderFragmentView>() {

    private val successImageList = ArrayList<SuccessImage>()

    fun onResume(id: String?) {

        if (id != null) {

            val success = successImageLoader.getSuccess(id)
            val successImages = successImageLoader.getSuccessImages(id)

            ifViewAttached { view ->
                view.displaySuccessData(success, successImages)
            }
        }
    }

    fun onSuccessImageClick(position: Int) {
        val imagePaths = ArrayList<String>()

        for (i in successImageList.indices) {
            imagePaths.add(successImageList[i].imagePath)
        }

        ifViewAttached { view ->
            view.startImageActivity(imagePaths, position)
        }

    }

}