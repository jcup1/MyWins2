package com.theandroiddev.mywins.local.database

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

object Migrations {

    @JvmField
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {

        }
    }

}
