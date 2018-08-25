package com.theandroiddev.mywins.presentation.successes

import com.theandroiddev.mywins.domain.service.success_images.SuccessImagesServiceModel

data class SuccessImageModel(
        var id: Long?,
        var successId: Long?,
        var imagePath: String
)

fun SuccessImageModel.toSuccessImagesServiceModel(): SuccessImagesServiceModel {
    return SuccessImagesServiceModel(
            this.id,
            this.successId,
            this.imagePath
    )
}