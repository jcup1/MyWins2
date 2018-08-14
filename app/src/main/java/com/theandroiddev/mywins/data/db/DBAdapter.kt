package com.theandroiddev.mywins.data.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.models.SuccessImage
import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.Constants.Companion.ADD_ON_TOP
import com.theandroiddev.mywins.utils.Constants.Companion.CATEGORY
import com.theandroiddev.mywins.utils.Constants.Companion.CATEGORY_VALUE
import com.theandroiddev.mywins.utils.Constants.Companion.DATE_ENDED
import com.theandroiddev.mywins.utils.Constants.Companion.DATE_ENDED_VALUE
import com.theandroiddev.mywins.utils.Constants.Companion.DATE_STARTED
import com.theandroiddev.mywins.utils.Constants.Companion.DATE_STARTED_VALUE
import com.theandroiddev.mywins.utils.Constants.Companion.DESCRIPTION
import com.theandroiddev.mywins.utils.Constants.Companion.DESCRIPTION_VALUE
import com.theandroiddev.mywins.utils.Constants.Companion.IMAGE_ID
import com.theandroiddev.mywins.utils.Constants.Companion.IMAGE_PATH
import com.theandroiddev.mywins.utils.Constants.Companion.IMPORTANCE
import com.theandroiddev.mywins.utils.Constants.Companion.IMPORTANCE_VALUE
import com.theandroiddev.mywins.utils.Constants.Companion.SUCCESS_ID
import com.theandroiddev.mywins.utils.Constants.Companion.SUCCESS_ID_VALUE
import com.theandroiddev.mywins.utils.Constants.Companion.TB_NAME_IMAGES
import com.theandroiddev.mywins.utils.Constants.Companion.TB_NAME_SUCCESSES
import com.theandroiddev.mywins.utils.Constants.Companion.TITLE
import com.theandroiddev.mywins.utils.Constants.Companion.TITLE_VALUE
import com.theandroiddev.mywins.utils.Constants.Companion.dummyCategory
import com.theandroiddev.mywins.utils.Constants.Companion.dummyDescription
import com.theandroiddev.mywins.utils.Constants.Companion.dummyEndDate
import com.theandroiddev.mywins.utils.Constants.Companion.dummyImportance
import com.theandroiddev.mywins.utils.Constants.Companion.dummyStartDate
import com.theandroiddev.mywins.utils.Constants.Companion.dummyTitle
import java.util.*
import javax.inject.Inject

/**
 * Created by jakub on 14.08.17.
 */

class DBAdapter @Inject constructor(
        val dbHelper: DBHelper
) {
    private var sqLiteDatabase: SQLiteDatabase? = null

    val defaultSuccesses: ArrayList<Success> = arrayListOf()

    init {

        openDB()
        Constants()
        var i = 0
        while (i < 5) {
            defaultSuccesses.add(Success(null, dummyTitle[i], dummyCategory[i], dummyDescription[i],
                    dummyStartDate[i], dummyEndDate[i], dummyImportance[i]))
            i++
        }
    }

    fun openDB() {

        sqLiteDatabase = dbHelper.writableDatabase

    }

    fun closeDB() {
        //        dbHelper.close();
    }

    fun addSuccess(success: Success) {

        sqLiteDatabase?.insert(TB_NAME_SUCCESSES, SUCCESS_ID, getSuccessContentValues(success))
    }

    private fun retrieveSuccess(searchTerm: String?, sortType: String?): Cursor? {

        if (sqLiteDatabase != null) {
            val columns = arrayOf(SUCCESS_ID, TITLE, CATEGORY, IMPORTANCE, DESCRIPTION, DATE_STARTED, DATE_ENDED)
            return if (searchTerm != null && searchTerm.isNotEmpty()) {
                val sql = "SELECT * FROM $TB_NAME_SUCCESSES WHERE $TITLE LIKE '%$searchTerm%'"
                sqLiteDatabase?.rawQuery(sql, null)
            } else {
                sqLiteDatabase?.query(TB_NAME_SUCCESSES, columns, null, null, null, null, sortType)
            }
        } else return null

    }

    fun removeSuccess(successesToRemove: ArrayList<Success>) {


        for (i in successesToRemove.indices) {
            sqLiteDatabase?.delete(TB_NAME_SUCCESSES, "$SUCCESS_ID=? and $TITLE=?", arrayOf(successesToRemove[i].id.toString(), successesToRemove[i].title))

        }

    }

    fun editSuccess(showSuccess: Success) {

        sqLiteDatabase?.update(TB_NAME_SUCCESSES, getSuccessContentValues(showSuccess), SUCCESS_ID + " = " + showSuccess.id, null)
    }

    private fun addSuccessImages(successImage: ArrayList<SuccessImage>) {

        for (image in successImage) {

            val contentValues = ContentValues()
            contentValues.put(IMAGE_PATH, image.imagePath)
            contentValues.put(SUCCESS_ID, image.successId)
            sqLiteDatabase?.insert(TB_NAME_IMAGES, IMAGE_ID, contentValues)

        }

    }

    fun getSuccessImages(successId: Long?): ArrayList<SuccessImage> {

        val successImages = ArrayList<SuccessImage>()

        val sql = "SELECT * FROM $TB_NAME_IMAGES WHERE $SUCCESS_ID=$successId"
        val cursor = sqLiteDatabase?.rawQuery(sql, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val sI = SuccessImage(null, successId, cursor.getString(1))

                    successImages.add(sI)

                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return successImages
    }

    fun editSuccessImages(successImages: ArrayList<SuccessImage>, successId: Long?) {

        deleteImages(successId)
        addSuccessImages(successImages)

    }

    private fun deleteImages(successId: Long?) {
        sqLiteDatabase?.execSQL("DELETE FROM $TB_NAME_IMAGES WHERE $SUCCESS_ID = $successId")

    }

    fun fetchSuccess(id: Long?): Success? {

        val sql = "SELECT * FROM $TB_NAME_SUCCESSES WHERE $SUCCESS_ID = $id"
        val cursor = sqLiteDatabase?.rawQuery(sql, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val title = cursor.getString(1)
                val category = cursor.getString(2)
                val importance = cursor.getInt(3)
                val description = cursor.getString(4)
                val dateStarted = cursor.getString(5)
                val dateEnded = cursor.getString(6)
                cursor.close()
                val s = Success(id, title, category, description, dateStarted, dateEnded, importance)
                s.id = id
                //s.setId(id);
                return s
            }
        }

        return null
    }

    fun getSuccesses(searchTerm: String?, sortType: String?,
                     isSortingAscending: Boolean): ArrayList<Success> {

        var success: Success
        val successList = ArrayList<Success>()
        val cursor = retrieveSuccess(searchTerm, sortType)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(SUCCESS_ID_VALUE)
                val title = cursor.getString(TITLE_VALUE)
                val category = cursor.getString(CATEGORY_VALUE)
                val importance = cursor.getInt(IMPORTANCE_VALUE)
                val description = cursor.getString(DESCRIPTION_VALUE)
                val dateStarted = cursor.getString(DATE_STARTED_VALUE)
                val dateEnded = cursor.getString(DATE_ENDED_VALUE)

                success = Success(id, title, category, description, dateStarted, dateEnded, importance)
                if (isSortingAscending) {
                    successList.add(ADD_ON_TOP, success)
                } else {
                    successList.add(success) //default = add on bottom
                }

            }
            cursor.close()
        }

        return successList
    }

    private fun getSuccessContentValues(success: Success): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(TITLE, success.title)
        contentValues.put(CATEGORY, success.category)
        contentValues.put(IMPORTANCE, success.importance)
        contentValues.put(DESCRIPTION, success.description)
        contentValues.put(DATE_STARTED, success.dateStarted)
        contentValues.put(DATE_ENDED, success.dateEnded)
        return contentValues
    }


    fun clear() {
        sqLiteDatabase?.delete(TB_NAME_SUCCESSES, null, null)
        sqLiteDatabase?.delete(TB_NAME_IMAGES, null, null)
    }
}
