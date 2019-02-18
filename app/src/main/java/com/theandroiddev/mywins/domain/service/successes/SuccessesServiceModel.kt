package com.theandroiddev.mywins.domain.service.successes

import com.theandroiddev.mywins.data.model.SuccessEntity
import com.theandroiddev.mywins.presentation.successes.Importance
import com.theandroiddev.mywins.presentation.successes.SuccessCategory
import com.theandroiddev.mywins.presentation.successes.SuccessModel
import java.io.Serializable

data class SuccessesServiceModel(
        val id: Long? = null,
        val title: String = "N/A",
        val category: SuccessCategory = SuccessCategory.NONE,
        val description: String = "N/A",
        val dateAdded: String = "N/A",
        val dateStarted: String = "N/A",
        val dateEnded: String = "N/A",
        val importance: Importance = Importance.NONE) : Serializable

fun SuccessesServiceModel.toModel(): SuccessModel {
    return SuccessModel(
            this.id,
            this.title,
            this.category,
            this.description,
            this.dateAdded,
            this.dateStarted,
            this.dateEnded,
            this.importance
    )
}

fun SuccessesServiceModel.toEntity(): SuccessEntity {
    return SuccessEntity(
            this.id,
            this.title,
            this.category,
            this.description,
            this.dateAdded,
            this.dateStarted,
            this.dateEnded,
            this.importance
    )
}