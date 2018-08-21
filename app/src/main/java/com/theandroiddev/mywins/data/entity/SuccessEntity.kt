package com.theandroiddev.mywins.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

/**
 * Created by jakub on 07.08.17.
 */

@Entity(tableName = "success")
data class SuccessEntity(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @ColumnInfo(name = "title") var title: String,
        @ColumnInfo(name = "category") var category: String,
        @ColumnInfo(name = "description") var description: String,
        @ColumnInfo(name = "dateStarted") var dateStarted: String,
        @ColumnInfo(name = "dateEnded") var dateEnded: String,
        @ColumnInfo(name = "importance") var importance: Int
) : Serializable
