package com.theandroiddev.mywins.presentation.edit_success

import androidx.cardview.widget.CardView
import com.esafirm.imagepicker.model.Image
import com.theandroiddev.mywins.core.mvp.MvpView
import com.theandroiddev.mywins.presentation.successes.SuccessImageModel
import com.theandroiddev.mywins.presentation.successes.SuccessModel

interface EditSuccessView : MvpView {

    fun displaySlider()

    fun displaySuccessImages(successImages: ArrayList<SuccessImageModel>)

    fun displaySuccessImageRemovedSnackbar(position: Int, successImage: SuccessImageModel)

    fun displayUndoRemovingSuccessImage(position: Int, successImage: SuccessImageModel)

    fun displaySuccessData(success: SuccessModel, successImages: MutableList<SuccessImageModel>)

    fun displayGallery(images: ArrayList<Image>)

    fun displayDeleteMenu(position: Int, cardView: CardView)

    fun updateImagePath(selectedImagePosition: Int, successImage: SuccessImageModel)

    fun addSuccessImages(successImages: ArrayList<SuccessImageModel>)

}