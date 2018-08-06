package com.theandroiddev.mywins.data.repositories

import com.theandroiddev.mywins.data.models.SearchFilter
import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.models.SuccessImage
import java.util.*

/**
 * Created by jakub on 28.10.17.
 */

interface SuccessesRepository {

    fun getDefaultSuccesses(): ArrayList<Success>

    fun updateForDeleteSuccess(successToRemoveList: ArrayList<Success>)

    fun getSuccesses(searchFilter: SearchFilter): ArrayList<Success>

    fun removeSuccesses(successToRemoveList: ArrayList<Success>)

    fun fetchSuccess(id: Long?): Success?

    fun addSuccess(s: Success)

    fun closeDB()

    fun openDB()

    fun getSuccessImages(id: Long?): ArrayList<SuccessImage>

    fun editSuccess(editSuccess: Success)

    fun editSuccessImages(successImageList: ArrayList<SuccessImage>, successId: Long?)

    fun saveSuccesses(defaultSuccesses: ArrayList<Success>)

    fun clearDatabase()
}
