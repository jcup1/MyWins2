package com.theandroiddev.mywins.data.successes.model

import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceModel
import com.theandroiddev.mywins.local.model.LocalSuccess
import com.theandroiddev.mywins.utils.Constants.Companion.Category
import com.theandroiddev.mywins.utils.Constants.Companion.Importance
import java.io.Serializable

/**
 * Created by jakub on 07.08.17.
 */

data class SuccessEntity(
        var id: Long? = null,
        val title: String = "N/A",
        val category: Category = Category.NONE,
        val description: String = "N/A",
        val dateAdded: String = "N/A",
        val dateStarted: String = "N/A",
        val dateEnded: String = "N/A",
        val importance: Importance = Importance.NONE) : Serializable

fun SuccessEntity.toLocal() = LocalSuccess(
        this.id,
        this.title,
        this.category.name,
        this.description,
        this.dateAdded,
        this.dateStarted,
        this.dateEnded,
        this.importance.value
)

fun SuccessEntity.toServiceModel() = SuccessesServiceModel(
        this.id,
        this.title,
        this.category,
        this.description,
        this.dateAdded,
        this.dateStarted,
        this.dateEnded,
        this.importance
)