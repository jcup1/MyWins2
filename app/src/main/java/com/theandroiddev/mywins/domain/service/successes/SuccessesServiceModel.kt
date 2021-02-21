package com.theandroiddev.mywins.domain.service.successes

import com.theandroiddev.mywins.data.successes.model.SuccessEntity
import com.theandroiddev.mywins.presentation.successes.SuccessModel
import com.theandroiddev.mywins.utils.Constants.Companion.Category
import com.theandroiddev.mywins.utils.Constants.Companion.Importance
import java.io.Serializable

data class SuccessesServiceModel(
        val id: Long? = null,
        val title: String = "N/A",
        val category: Category = Category.NONE,
        val description: String = "N/A",
        val dateAdded: String = "N/A",
        val dateStarted: String = "N/A",
        val dateEnded: String = "N/A",
        val importance: Importance = Importance.NONE,
        val repeatCount: Int = 1) : Serializable

fun SuccessesServiceModel.toModel(): SuccessModel {
    return SuccessModel(
            this.id,
            this.title,
            this.category,
            this.description,
            this.dateAdded,
            this.dateStarted,
            this.dateEnded,
            this.importance,
            this.repeatCount
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
            this.importance,
            this.repeatCount
    )
}