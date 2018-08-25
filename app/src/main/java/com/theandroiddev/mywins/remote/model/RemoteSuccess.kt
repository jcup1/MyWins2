package com.theandroiddev.mywins.remote.model

import com.theandroiddev.mywins.data.model.SuccessEntity
import com.theandroiddev.mywins.local.model.toEntity
import com.theandroiddev.mywins.utils.Constants

class SuccessRemoteModel(
        var id: Long,
        val title: String,
        val category: String,
        val description: String,
        val dateStarted: String,
        val dateEnded: String,
        val importance: Int)

fun SuccessRemoteModel.toEntity(): SuccessEntity {
    return SuccessEntity(
            this.id,
            this.title,
            Constants.Companion.Category.valueOf(this.category),
            this.description,
            this.dateStarted,
            this.dateEnded,
            Constants.Companion.Importance.values()[this.importance]
    )
}