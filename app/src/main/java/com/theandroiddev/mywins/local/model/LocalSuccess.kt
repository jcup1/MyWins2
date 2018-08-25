package com.theandroiddev.mywins.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.theandroiddev.mywins.data.model.SuccessEntity
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceModel
import com.theandroiddev.mywins.utils.Constants
import java.io.Serializable

@Entity(tableName = "success")
data class LocalSuccess(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @ColumnInfo(name = "title") var title: String,
        @ColumnInfo(name = "category") var category: String,
        @ColumnInfo(name = "description") var description: String,
        @ColumnInfo(name = "dateStarted") var dateStarted: String,
        @ColumnInfo(name = "dateEnded") var dateEnded: String,
        @ColumnInfo(name = "importance") var importance: Int
) : Serializable


fun LocalSuccess.toEntity(): SuccessEntity {
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
