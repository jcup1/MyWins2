package com.theandroiddev.mywins.edit_success

import android.content.Intent
import android.support.v7.widget.CardView
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.models.SuccessImage
import com.theandroiddev.mywins.data.repositories.SuccessesRepository
import com.theandroiddev.mywins.mvp.MvpPresenter
import com.theandroiddev.mywins.success_slider.SuccessImageLoader
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

    private var images: ArrayList<Image> = ArrayList()
    private var selectedImagePosition = -1
    private var editSuccessId: Long? = null

    fun onDestroy() {
        repository.closeDB()
    }

    fun openDB() {
        repository.openDB()
    }

    private fun addImageIv(id: Long): SuccessImage {

        return SuccessImage(null, id, "")
    }

    fun onSwiped(position: Int, successImage: SuccessImage?) {

        ifViewAttached { view ->
            if (successImage != null) {
                view.displaySuccessImageRemovedSnackbar(position, successImage)
            }
        }
    }

    fun onUndoToRemove(position: Int, successImage: SuccessImage) {
        ifViewAttached { view ->
            view.displayUndoRemovingSuccessImage(position, successImage)
        }

    }

    fun onActivityCreate(id: Long) {

        editSuccessId = id

        val success = successImageLoader.getSuccess(id)

        val successImages = successImageLoader.getSuccessImages(id)

        successImages.add(0, SuccessImage(null, id, ""))

        ifViewAttached { view ->
            view.displaySuccessData(success, successImages)
        }
    }

    fun onImagePickerResultOk(successImages: ArrayList<SuccessImage>?, data: Intent, isImagePickerNull: Boolean) {

        images = ImagePicker.getImages(data) as ArrayList<com.esafirm.imagepicker.model.Image>
        if (successImages != null) {
            printImages(successImages, images, isImagePickerNull)
        }
    }

    private fun printImages(successImages: ArrayList<SuccessImage>, images: ArrayList<Image>, isImagePickerNull: Boolean) {

        if (selectedImagePosition != 0) {

            ifViewAttached { view ->
                val successImage = successImages[selectedImagePosition]
                successImage.imagePath = images[0].path
                view.updateImagePath(selectedImagePosition, successImage)
            }

        } else {//selected = 0
            if (isImagePickerNull == false) {
                val successId = editSuccessId

                if (successId != null) {
                    val sImages = ArrayList<SuccessImage>()
                    for (i in images) {
                        sImages.add(SuccessImage(null, successId, i.path))

                    }
                    ifViewAttached { view ->
                        view.addSuccessImages(sImages)
                    }
                }
            }
        }


    }

    fun onSaveChanges(success: Success, successImages: ArrayList<SuccessImage>?) {

        if (successImages != null) {
            success.id = editSuccessId
            successImages.removeAt(0)
            for (image in successImages) {
                image.successId = editSuccessId
            }
            repository.editSuccess(success)
            repository.editSuccessImages(successImages, success.id)
        }

        ifViewAttached { view ->
            view.displaySlider()
        }
    }

    fun onCameraResultOk(successImages: ArrayList<SuccessImage>?, resultImages: List<Image>, isImagePickerNull: Boolean) {

        if (successImages != null) {
            images = resultImages as ArrayList<com.esafirm.imagepicker.model.Image>
            printImages(successImages, images, isImagePickerNull)
            ifViewAttached { view ->
                view.displayGallery(resultImages)
            }
        }
    }

    fun onSuccessImageClick(position: Int) {

        selectedImagePosition = position
        ifViewAttached { view ->
            view.displayGallery(images)
        }
    }

    fun onSuccessImageLongClick(position: Int, cardView: CardView) {

        ifViewAttached { view ->
            view.displayDeleteMenu(position, cardView)
        }
    }

    fun onSuccessImageRemoved(position: Int, successImage: SuccessImage?) {

        ifViewAttached { view ->
            if (successImage != null) {
                view.displaySuccessImageRemovedSnackbar(position, successImage)
            }
        }
    }

}
