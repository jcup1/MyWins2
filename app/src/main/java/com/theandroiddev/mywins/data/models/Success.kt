package com.theandroiddev.mywins.data.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by jakub on 07.08.17.
 */
@Parcelize
@Entity(tableName = "success")
data class Success(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @ColumnInfo(name = "title") var title: String,
        @ColumnInfo(name = "category") var category: String,
        @ColumnInfo(name = "description") var description: String,
        @ColumnInfo(name = "dateStarted") var dateStarted: String,
        @ColumnInfo(name = "dateEnded") var dateEnded: String,
        @ColumnInfo(name = "importance") var importance: Int
) : Parcelable {
    constructor() : this(
            id = null,
            title = "",
            category = "",
            description = "",
            dateStarted = "",
            dateEnded = "",
            importance = 0

    )
}
