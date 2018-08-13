package com.theandroiddev.mywins.data.repositories

import com.theandroiddev.mywins.data.db.DBAdapter
import com.theandroiddev.mywins.data.models.SearchFilter
import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.models.SuccessImage
import java.util.*
import javax.inject.Inject

/**
 * Created by jakub on 04.11.17.
 */

class DatabaseSuccessesRepository @Inject constructor(private var dbAdapter: DBAdapter) : SuccessesRepository {


    override fun updateForDeleteSuccess(successToRemoveList: ArrayList<Success>) {
        dbAdapter.removeSuccess(successToRemoveList)
    }

    override fun getSuccesses(searchFilter: SearchFilter): ArrayList<Success> {
        return dbAdapter.getSuccesses(searchFilter.searchTerm, searchFilter.sortType, searchFilter.isSortingAscending)
    }

    override fun removeSuccesses(successToRemoveList: ArrayList<Success>) {
        dbAdapter.removeSuccess(successToRemoveList)
    }

    override fun fetchSuccess(id: Long?): Success? {
        return dbAdapter.fetchSuccess(id)
    }

    override fun getDefaultSuccesses(): ArrayList<Success> {
        return dbAdapter.defaultSuccesses
    }

    override fun addSuccess(s: Success) {
        dbAdapter.addSuccess(s)
    }

    override fun closeDB() {
        dbAdapter.closeDB()
    }

    override fun openDB() {
        dbAdapter.openDB()
    }

    override fun getSuccessImages(id: Long?): ArrayList<SuccessImage> {
        return dbAdapter.getSuccessImages(id)
    }

    override fun editSuccess(editSuccess: Success) {
        dbAdapter.editSuccess(editSuccess)
    }

    override fun editSuccessImages(successImageList: ArrayList<SuccessImage>, successId: Long?) {
        dbAdapter.editSuccessImages(successImageList, successId)
    }

    override fun saveSuccesses(defaultSuccesses: ArrayList<Success>) {
        for (s in defaultSuccesses)
            dbAdapter.addSuccess(s)
    }

    override fun clearDatabase() {
        dbAdapter.clear()
    }

}
