package com.theandroiddev.mywins.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
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