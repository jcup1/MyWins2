package com.theandroiddev.mywins.presentation.edit_success

import android.support.v7.widget.CardView
import com.esafirm.imagepicker.model.Image
import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.data.entity.SuccessImageEntity
import com.theandroiddev.mywins.mvp.MvpView

interface EditSuccessView : MvpView {

    fun displaySlider()

    fun displaySuccessImages(successImages: ArrayList<SuccessImageEntity>)

    fun displaySuccessImageRemovedSnackbar(position: Int, successImage: SuccessImageEntity)

    fun displayUndoRemovingSuccessImage(position: Int, successImage: SuccessImageEntity)

    fun displaySuccessData(success: SuccessEntity, successImages: MutableList<SuccessImageEntity>)

    fun displayGallery(images: ArrayList<Image>)

    fun displayDeleteMenu(position: Int, cardView: CardView)

    fun updateImagePath(selectedImagePosition: Int, successImage: SuccessImageEntity)

    fun addSuccessImages(successImages: ArrayList<SuccessImageEntity>)


}