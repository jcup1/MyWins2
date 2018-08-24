package com.theandroiddev.mywins.presentation.edit_success

import android.content.Intent
import android.support.v7.widget.CardView
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.data.entity.SuccessImageEntity
import com.theandroiddev.mywins.domain.service.successes.SuccessesService
import com.theandroiddev.mywins.mvp.MvpPresenter
import com.theandroiddev.mywins.presentation.success_slider.SuccessImageLoader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jakub on 12.11.17.
 */

class EditSuccessPresenter @Inject
constructor(
        private val successesService: SuccessesService,
        private val successImageLoader: SuccessImageLoader

) : MvpPresenter<EditSuccessView>() {

    private var images: ArrayList<Image> = arrayListOf()
    private var selectedImagePosition = -1
    private var editSuccessId: Long? = null

    private fun addImageIv(id: Long): SuccessImageEntity {

        return SuccessImageEntity(null, id, "")
    }

    fun onSwiped(position: Int, successImage: SuccessImageEntity?) {

        ifViewAttached { view ->
            if (successImage != null) {
                view.displaySuccessImageRemovedSnackbar(position, successImage)
            }
        }
    }

    fun onUndoToRemove(position: Int, successImage: SuccessImageEntity) {
        ifViewAttached { view ->
            view.displayUndoRemovingSuccessImage(position, successImage)
        }

    }

    fun onActivityCreate(id: Long) {

        editSuccessId = id

        successImageLoader.fetchSuccess(id)
                .subscribeOn(Schedulers.io())
                .subscribe { successEntity ->

                    successImageLoader.getSuccessImages(id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { successImageEntities ->

                                successImageEntities.add(0, SuccessImageEntity(null, id, ""))

                                ifViewAttached { view ->
                                    view.displaySuccessData(successEntity, successImageEntities)
                                }
                            }

                }

    }

    fun onImagePickerResultOk(successImages: MutableList<SuccessImageEntity>?, data: Intent,
                              isImagePickerNull: Boolean) {

        images = ImagePicker.getImages(data) as ArrayList<com.esafirm.imagepicker.model.Image>
        if (successImages != null) {
            printImages(successImages, images, isImagePickerNull)
        }
    }

    private fun printImages(successImages: MutableList<SuccessImageEntity>, images: MutableList<Image>,
                            isImagePickerNull: Boolean) {

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
                    val sImages = ArrayList<SuccessImageEntity>()
                    for (i in images) {
                        sImages.add(SuccessImageEntity(null, successId, i.path))

                    }
                    ifViewAttached { view ->
                        view.addSuccessImages(sImages)
                    }
                }
            }
        }

    }

    fun onSaveChanges(success: SuccessEntity, successImages: MutableList<SuccessImageEntity>?) {

        if (successImages != null) {
            success.id = editSuccessId
            successImages.removeAt(0)
            for (image in successImages) {
                image.successId = editSuccessId
            }
            successesService.editSuccess(success)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        //TODO handle result

                        val id = success.id
                        if (id != null) {
                            successesService.editSuccessImages(successImages, id)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe {
                                        //TODO result
                                        ifViewAttached { view ->
                                            view.displaySlider()
                                        }
                                    }
                        } else {
                            //TODO handle errors
                        }
                    }

        }

    }

    fun onCameraResultOk(successImages: MutableList<SuccessImageEntity>?, resultImages: MutableList<Image>,
                         isImagePickerNull: Boolean) {

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

    fun onSuccessImageRemoved(position: Int, successImage: SuccessImageEntity?) {

        ifViewAttached { view ->
            if (successImage != null) {
                view.displaySuccessImageRemovedSnackbar(position, successImage)
            }
        }
    }

}
