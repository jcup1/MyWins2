package com.theandroiddev.mywins.presentation.edit_success

import android.content.Intent
import androidx.cardview.widget.CardView
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.theandroiddev.mywins.core.mvp.MvpPresenter
import com.theandroiddev.mywins.domain.service.success_images.SuccessImagesService
import com.theandroiddev.mywins.domain.service.success_images.SuccessImagesServiceArgument
import com.theandroiddev.mywins.domain.service.success_images.SuccessImagesServiceResult
import com.theandroiddev.mywins.domain.service.success_images.toModel
import com.theandroiddev.mywins.domain.service.successes.SuccessesService
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceArgument
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceModel
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceResult
import com.theandroiddev.mywins.domain.service.successes.toModel
import com.theandroiddev.mywins.presentation.successes.SuccessImageModel
import com.theandroiddev.mywins.presentation.successes.SuccessModel
import com.theandroiddev.mywins.presentation.successes.toSuccessImagesServiceModel
import com.theandroiddev.mywins.presentation.successes.toSuccessesServiceModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jakub on 12.11.17.
 */

class EditSuccessPresenter @Inject
constructor(
    private val successesService: SuccessesService,
    private val successImagesService: SuccessImagesService
) : MvpPresenter<EditSuccessView, EditSuccessBundle>() {

    override fun onViewCreated() {
        loadSuccess(bundle.successId)
    }

    private var images: ArrayList<Image> = arrayListOf()
    private var selectedImagePosition = -1

    fun onSwiped(position: Int, successImage: SuccessImageModel?) {

        ifViewAttached { view ->
            if (successImage != null) {
                view.displaySuccessImageRemovedSnackbar(position, successImage)
            }
        }
    }

    fun onUndoToRemove(position: Int, successImage: SuccessImageModel) {
        ifViewAttached { view ->
            view.displayUndoRemovingSuccessImage(position, successImage)
        }
    }

    private fun loadSuccess(id: Long) {
        successesService.fetchSuccess(id)
            .subscribeOn(Schedulers.io())
            .subscribe ({ successesServiceResult ->

                when (successesServiceResult) {
                    is SuccessesServiceResult.Successes -> {

                        if (successesServiceResult.successes.hasOne()) {
                            val successesServiceModel =
                                successesServiceResult.successes.first()
                            loadSuccessImages(successesServiceModel, id)
                        }
                    }
                    is SuccessesServiceResult.Error -> {
                        ifViewAttached { view ->
                            view.alerts?.displayUnexpectedError()
                        }
                    }
                }

            }, {
                ifViewAttached { view ->
                    view.alerts?.displayUnexpectedError()
                }

            }).addToDisposables(disposables)
    }

    private fun loadSuccessImages(successesServiceModel: SuccessesServiceModel, id: Long) {

        successImagesService.getSuccessImages(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ successImagesServiceResult ->

                when (successImagesServiceResult) {
                    is SuccessImagesServiceResult.SuccessImages -> {
                        val successes =
                            successImagesServiceResult.successImages.map {
                                it.toModel()
                            }.toMutableList()

                        successes.add(0, SuccessImageModel(null, id, ""))

                        ifViewAttached { view ->
                            view.displaySuccessData(
                                successesServiceModel.toModel(),
                                successes
                            )
                        }
                    }
                    is SuccessImagesServiceResult.Error -> {
                        ifViewAttached { view ->
                            view.alerts?.displayUnexpectedError()
                        }
                    }
                }

            }, {
                ifViewAttached { view ->
                    view.alerts?.displayUnexpectedError()
                }
            }).addToDisposables(disposables)
    }

    fun onImagePickerResultOk(
        successImages: MutableList<SuccessImageModel>?, data: Intent,
        isImagePickerNull: Boolean
    ) {

        images = ImagePicker.getImages(data) as ArrayList<com.esafirm.imagepicker.model.Image>
        if (successImages != null) {
            printImages(successImages, images, isImagePickerNull)
        }
    }

    private fun printImages(
        successImages: MutableList<SuccessImageModel>, images: MutableList<Image>,
        isImagePickerNull: Boolean
    ) {

        if (selectedImagePosition != 0) {

            ifViewAttached { view ->
                val successImage = successImages[selectedImagePosition]
                successImage.imagePath = images[0].path
                view.updateImagePath(selectedImagePosition, successImage)
            }
        } else {//selected = 0
            if (isImagePickerNull == false) {
                val successId = bundle.successId

                val sImages = ArrayList<SuccessImageModel>()
                for (i in images) {
                    sImages.add(SuccessImageModel(null, successId, i.path))
                }
                ifViewAttached { view ->
                    view.addSuccessImages(sImages)
                }
            }
        }
    }

    fun onSaveChanges(success: SuccessModel, successImages: MutableList<SuccessImageModel>?) {

        if (successImages != null) {
            success.id = bundle.successId
            if (successImages.isNotEmpty()) {
                successImages.removeAt(0)
            }
            for (image in successImages) {
                image.successId = bundle.successId
            }
            val argument =
                SuccessesServiceArgument.Successes(mutableListOf(success.toSuccessesServiceModel()))
            successesService.editSuccess(argument)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val id = success.id
                    if (id != null) {
                        successImagesService.editSuccessImages(
                            SuccessImagesServiceArgument.SuccessImages(
                                successImages.map { it.toSuccessImagesServiceModel() },
                                bundle.successId
                            )
                        )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe ({
                                ifViewAttached { view ->
                                    view.displaySlider()
                                }
                            }, {
                                ifViewAttached { view ->
                                    view.alerts?.displayUnexpectedError()
                                }

                            }).addToDisposables(disposables)
                    } else {
                        ifViewAttached { view ->
                            view.alerts?.displayUnexpectedError()
                        }
                    }
                }, {
                    ifViewAttached { view ->
                        view.alerts?.displayUnexpectedError()
                    }
                }).addToDisposables(disposables)
        }
    }

    fun onCameraResultOk(
        successImages: MutableList<SuccessImageModel>?, resultImages: MutableList<Image>,
        isImagePickerNull: Boolean
    ) {

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

    fun onSuccessImageRemoved(position: Int, successImage: SuccessImageModel?) {

        ifViewAttached { view ->
            if (successImage != null) {
                view.displaySuccessImageRemovedSnackbar(position, successImage)
            }
        }
    }
}

//TODO clean up exs
fun <E> List<E>.hasOne(): Boolean {
    return this.size == 1
}
