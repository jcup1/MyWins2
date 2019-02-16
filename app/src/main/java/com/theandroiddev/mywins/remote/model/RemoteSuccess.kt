package com.theandroiddev.mywins.remote.model

import com.theandroiddev.mywins.data.model.SuccessEntity
import com.theandroiddev.mywins.presentation.successes.Importance
import com.theandroiddev.mywins.presentation.successes.SuccessCategory

class SuccessRemoteModel(
        var id: Long,
        val title: String,
        val category: String,
        val description: String,
        val dateAdded: String,
        val dateStarted: String,
        val dateEnded: String,
        val importance: Int)

fun SuccessRemoteModel.toEntity(): SuccessEntity {
    return SuccessEntity(
            this.id,
            this.title,
            SuccessCategory.valueOf(this.category),
            this.description,
            this.dateAdded,
            this.dateStarted,
            this.dateEnded,
            Importance.values()[this.importance]
    )
}