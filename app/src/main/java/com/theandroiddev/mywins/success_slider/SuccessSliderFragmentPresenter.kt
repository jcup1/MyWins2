package com.theandroiddev.mywins.success_slider

import com.theandroiddev.mywins.data.models.SuccessImage
import com.theandroiddev.mywins.mvp.MvpPresenter
import javax.inject.Inject

class SuccessSliderFragmentPresenter @Inject constructor(
        val successImageLoader: SuccessImageLoader

) : MvpPresenter<SuccessSliderFragmentView>() {

    fun onResume(id: Long?) {

        if (id != null) {

            val success = successImageLoader.fetchSuccess(id)
            val successImages = successImageLoader.getSuccessImages(id)

            ifViewAttached { view ->
                if (success != null) {
                    view.displaySuccessData(success, successImages)
                }
            }
        }
    }

    fun onSuccessImageClick(position: Int, successImages: ArrayList<SuccessImage>?) {

        if (successImages != null) {

            val imagePaths = ArrayList<String>()

            for (i in successImages.indices) {
                imagePaths.add(successImages[i].imagePath)
            }

            ifViewAttached { view ->
                view.startImageActivity(position, imagePaths)
            }
        }
    }

}