package com.theandroiddev.mywins.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.theandroiddev.mywins.data.model.SuccessImageEntity
import com.theandroiddev.mywins.domain.service.success_images.SuccessImagesServiceModel

@Entity
data class LocalSuccessImage(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @ColumnInfo(name = "successId") var successId: Long?,
        @ColumnInfo(name = "imagePath") var imagePath: String
) {

    constructor() : this(
            id = null,
            successId = null,
            imagePath = ""
    )

}


fun LocalSuccessImage.toEntity(): SuccessImageEntity {
    return SuccessImageEntity(
            this.id,
            this.successId,
            this.imagePath
    )
}