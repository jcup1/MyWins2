package com.theandroiddev.mywins.edit_success

import android.support.v7.widget.CardView
import com.esafirm.imagepicker.model.Image
import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.models.SuccessImage
import com.theandroiddev.mywins.mvp.MvpView

interface EditSuccessView : MvpView {

    fun displaySlider()

    fun displaySuccessImages(successImageList: ArrayList<SuccessImage>)

    fun displaySuccessImageRemovedSnackbar(successImage: SuccessImage, position: Int)

    fun displayUndoRemovingSuccessImage(successImage: SuccessImage, position: Int)

    fun displaySuccessData(success: Success, successImages: ArrayList<SuccessImage>)

    fun displayGallery(images: ArrayList<Image>)

    fun displayDeleteMenu(position: Int, cardView: CardView)

    fun updateImagePath(successImage: SuccessImage, selectedImagePosition: Int)

    fun addSuccessImages(successImages: ArrayList<SuccessImage>)


}