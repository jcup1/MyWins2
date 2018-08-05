package com.theandroiddev.mywins.data.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class SuccessImage(
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
