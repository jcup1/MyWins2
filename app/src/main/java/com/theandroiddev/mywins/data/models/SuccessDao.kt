package com.theandroiddev.mywins.data.models

import android.arch.persistence.room.*
import java.util.*

/**
 * Created by grazyna on 2017-11-18.
 */
@Dao
interface SuccessDao {

    //TODO replace ASC DESC
    @Query("SELECT * FROM success WHERE title LIKE :searchTerm ORDER BY :sortType ASC")
    fun getAllASC(searchTerm: String, sortType: String): List<Success>

    @Query("SELECT * FROM success WHERE title LIKE :searchTerm ORDER BY :sortType DESC")
    fun getAllDESC(searchTerm: String, sortType: String): List<Success>

    @Query("SELECT * FROM success WHERE title LIKE :title LIMIT 1")
    fun findByName(title: String): Success

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(successList: ArrayList<Success>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(success: Success)

    @Delete
    fun delete(success: Success)

    @Query("delete from success")
    fun removeAll()

}
