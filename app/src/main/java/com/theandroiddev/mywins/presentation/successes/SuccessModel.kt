package com.theandroiddev.mywins.presentation.successes

import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceModel
import java.io.Serializable

data class SuccessModel(
    var id: Long? = null,
    val title: String = "N/A",
    val category: SuccessCategory = SuccessCategory.NONE,
    val description: String = "N/A",
    val dateAdded: String = "N/A",
    val dateStarted: String = "N/A",
    val dateEnded: String = "N/A",
    val importance: Importance = Importance.NONE
) : Serializable

enum class SuccessCategory(val res: Int, val id: Int?) {
    MEDIA(R.string.category_media, R.id.action_video),
    SPORT(R.string.category_sport, R.id.action_sport),
    BUSINESS(R.string.category_business, R.id.action_money),
    JOURNEY(R.string.category_journey, R.id.action_journey),
    KNOWLEDGE(R.string.category_learn, R.id.action_learn),
    OTHER(R.string.category_other, null),
    NONE(R.string.category_none, null)
}

enum class Importance(val value: Int, val res: Int) {
    NONE(0, R.string.importance_none),
    SMALL(1, R.string.importance_small),
    MEDIUM(2, R.string.importance_medium),
    BIG(3, R.string.importance_big),
    HUGE(4, R.string.importance_huge)
}

enum class SortType {
    TITLE,
    CATEGORY,
    IMPORTANCE,
    DESCRIPTION,
    DATE_STARTED,
    DATE_ENDED,
    DATE_ADDED
}

fun SuccessModel.toSuccessesServiceModel(): SuccessesServiceModel {
    return SuccessesServiceModel(
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