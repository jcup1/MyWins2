package com.theandroiddev.mywins.edit_success

import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.models.SuccessImage
import com.theandroiddev.mywins.data.repositories.SuccessesRepository
import com.theandroiddev.mywins.mvp.MvpPresenter
import com.theandroiddev.mywins.success_slider.SuccessImageLoader
import java.util.*
import javax.inject.Inject

/**
 * Created by jakub on 12.11.17.
 */

class EditSuccessPresenter @Inject
constructor(
        private val repository: SuccessesRepository,
        private val successImageLoader: SuccessImageLoader

) : MvpPresenter<EditSuccessView>() {

    init {
        openDB()
    }

    private var successImageList: ArrayList<SuccessImage> = ArrayList()

    fun editSuccess(editSuccess: Success, successImageList: ArrayList<SuccessImage>) {
        repository.editSuccess(editSuccess)
        repository.editSuccessImages(successImageList, editSuccess.id)

        view.displaySlider()
    }

    fun loadSuccessImages(id: String) {

        successImageList = ArrayList()
        successImageList.clear()
        successImageList.addAll(repository.getSuccessImages(id))
        successImageList.add(0, addImageIv(id))

    }

    fun closeDB() {
        repository.closeDB()
    }

    fun openDB() {
        repository.openDB()
    }

    private fun addImageIv(id: String): SuccessImage {

        return SuccessImage(id)
    }

    fun getSuccess(id: String): Success = successImageLoader.getSuccess(id)

    fun getSuccessImages(id: String): ArrayList<SuccessImage> = successImageLoader.getSuccessImages(id)
}
