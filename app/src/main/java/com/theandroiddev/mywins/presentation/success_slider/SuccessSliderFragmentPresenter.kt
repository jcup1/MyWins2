package com.theandroiddev.mywins.presentation.success_slider

import com.theandroiddev.mywins.core.mvp.MvpPresenter
import com.theandroiddev.mywins.domain.service.success_images.SuccessImagesService
import com.theandroiddev.mywins.domain.service.success_images.SuccessImagesServiceResult
import com.theandroiddev.mywins.domain.service.success_images.toModel
import com.theandroiddev.mywins.domain.service.successes.*
import com.theandroiddev.mywins.presentation.edit_success.hasOne
import com.theandroiddev.mywins.presentation.successes.SuccessImageModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SuccessSliderFragmentPresenter @Inject constructor(
        val successesService: SuccessesService,
        val successImagesService: SuccessImagesService
) : MvpPresenter<SuccessSliderFragmentView, SuccessSliderBundle>() {

    override fun onViewCreated() {

    }

    fun onResume(id: Long?) {

        if (id != null) {
            fetchSuccess(id)
        } else {
            ifViewAttached { view ->
                view.alerts?.displayUnexpectedError()
            }
        }
    }

    private fun fetchSuccess(id: Long) {
        successesService.fetchSuccess(id)
                .subscribeOn(Schedulers.io())
                .subscribe { successesServiceResult ->

                    when (successesServiceResult) {
                        is SuccessesServiceResult.Successes -> {

                            if (successesServiceResult.successes.hasOne()) {
                                val successesServiceModel = successesServiceResult.successes.first()
                                getSuccessImages(id, successesServiceModel)
                            } else {
                                ifViewAttached { view ->
                                    view.alerts?.displayUnexpectedError()
                                }
                            }

                        }
                        is SuccessesServiceResult.Error -> {
                            ifViewAttached { view ->
                                view.alerts?.displayUnexpectedError()
                            }
                        }
                    }

                }.addToDisposables(disposables)
    }

    private fun getSuccessImages(id: Long, successesServiceModel: SuccessesServiceModel) {

        successImagesService.getSuccessImages(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { successImagesServiceResult ->

                    when (successImagesServiceResult) {
                        is SuccessImagesServiceResult.SuccessImages -> {

                            val successImageModels = successImagesServiceResult.successImages.map {
                                it.toModel()
                            }
                            ifViewAttached { view ->
                                view.displaySuccessData(
                                        successesServiceModel.toModel(),
                                        successImageModels)
                            }
                        }
                        is SuccessImagesServiceResult.Error -> {

                        }
                    }

                }.addToDisposables(disposables)
    }

    fun onSuccessImageClick(position: Int, successImages: MutableList<SuccessImageModel>?) {

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