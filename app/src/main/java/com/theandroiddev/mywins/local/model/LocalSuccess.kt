package com.theandroiddev.mywins.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.theandroiddev.mywins.data.model.SuccessEntity
import com.theandroiddev.mywins.presentation.successes.Importance
import com.theandroiddev.mywins.presentation.successes.SuccessCategory
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
        @ColumnInfo(name = "importance") var importance: Int
) : Serializable


fun LocalSuccess.toEntity(): SuccessEntity {
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
