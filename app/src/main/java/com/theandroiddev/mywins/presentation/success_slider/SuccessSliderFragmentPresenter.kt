package com.theandroiddev.mywins.presentation.success_slider

import com.theandroiddev.mywins.data.entity.SuccessImageEntity
import com.theandroiddev.mywins.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SuccessSliderFragmentPresenter @Inject constructor(
        val successImageLoader: SuccessImageLoader

) : MvpPresenter<SuccessSliderFragmentView>() {

    fun onResume(id: Long?) {

        if (id != null) {

            successImageLoader.fetchSuccess(id)
                    .subscribeOn(Schedulers.io())
                    .subscribe { successEntity ->
                        successImageLoader.getSuccessImages(id)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { successImageEntities ->
                                    ifViewAttached { view ->
                                        view.displaySuccessData(successEntity, successImageEntities)
                                    }
                                }
                    }
        } else {
            //TODO handle errors
        }
    }

    fun onSuccessImageClick(position: Int, successImages: MutableList<SuccessImageEntity>?) {

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