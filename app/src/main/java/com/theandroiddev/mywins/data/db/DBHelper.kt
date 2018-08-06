package com.theandroiddev.mywins.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.theandroiddev.mywins.utils.Constants

/**
 * Created by jakub on 14.08.17.
 */

class DBHelper(context: Context) : SQLiteOpenHelper(context, Constants.DB_NAME, null, Constants.DB_VERSION) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(Constants.CREATE_TB_SUCCESSES)
        sqLiteDatabase.execSQL(Constants.CREATE_TB_IMAGES)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {

        sqLiteDatabase.execSQL(Constants.DROP_TB_SUCCESSES)
        sqLiteDatabase.execSQL(Constants.DROP_TB_IMAGES)
        onCreate(sqLiteDatabase)

        dbUpdate = true

    }

    companion object {

        //TODO maybe handle it without static
        var dbUpdate: Boolean = false
    }


}
