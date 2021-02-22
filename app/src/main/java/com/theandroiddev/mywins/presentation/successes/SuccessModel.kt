package com.theandroiddev.mywins.presentation.successes

import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceModel
import com.theandroiddev.mywins.utils.Constants.Companion.Category
import com.theandroiddev.mywins.utils.Constants.Companion.Importance
import java.io.Serializable

data class SuccessModel(
        var id: Long? = null,
        val title: String = "N/A",
        val category: Category = Category.NONE,
        val description: String = "N/A",
        val dateAdded: String = "N/A",
        val dateStarted: String = "N/A",
        val dateEnded: String = "N/A",
        val importance: Importance = Importance.NONE,
        val repeatCount: Int = 1) : Serializable

fun SuccessModel.toSuccessesServiceModel(): SuccessesServiceModel {
    return SuccessesServiceModel(
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