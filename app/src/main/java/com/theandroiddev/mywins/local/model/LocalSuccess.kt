package com.theandroiddev.mywins.local.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.theandroiddev.mywins.data.successes.model.SuccessEntity
import com.theandroiddev.mywins.utils.Constants
import java.io.Serializable

@Entity(tableName = "success")
data class LocalSuccess(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @ColumnInfo(name = "title") var title: String,
        @ColumnInfo(name = "category") var category: String,
        @ColumnInfo(name = "description") var description: String,
        @ColumnInfo(name = "date_added") var dateAdded: String,
        @ColumnInfo(name = "date_started") var dateStarted: String,
        @ColumnInfo(name = "date_ended") var dateEnded: String,
        @ColumnInfo(name = "importance") var importance: Int,
        @ColumnInfo(name = "repeat_count") var repeatCount: Int
) : Serializable


fun LocalSuccess.toEntity(): SuccessEntity {
    return SuccessEntity(
            this.id,
            this.title,
            Constants.Companion.Category.valueOf(this.category),
            this.description,
            this.dateAdded,
            this.dateStarted,
            this.dateEnded,
            Constants.Companion.Importance.values()[this.importance],
            this.repeatCount
    )
}
