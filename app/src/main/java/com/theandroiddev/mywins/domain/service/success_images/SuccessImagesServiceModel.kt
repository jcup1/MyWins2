package com.theandroiddev.mywins.domain.service.success_images

import com.theandroiddev.mywins.data.successes.model.SuccessImageEntity
import com.theandroiddev.mywins.presentation.successes.SuccessImageModel
import java.io.Serializable

data class SuccessImagesServiceModel(
        var id: Long?,
        var successId: Long?,
        var imagePath: String) : Serializable

fun SuccessImagesServiceModel.toModel(): SuccessImageModel {
    return SuccessImageModel(
            this.id,
            this.successId,
            this.imagePath
    )
}

fun SuccessImagesServiceModel.toEntity(): SuccessImageEntity {
    return SuccessImageEntity(
            this.id,
            this.successId,
            this.imagePath
    )
}