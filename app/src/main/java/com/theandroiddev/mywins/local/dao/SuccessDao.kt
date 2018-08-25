package com.theandroiddev.mywins.local.dao

import android.arch.persistence.room.*
import com.theandroiddev.mywins.local.model.LocalSuccess

@Dao
interface SuccessDao {

    //TODO replace ASC DESC
    @Query("SELECT * FROM success WHERE title LIKE :searchTerm ORDER BY :sortType ASC")
    fun getAllASC(searchTerm: String, sortType: String): MutableList<LocalSuccess>

    @Query("SELECT * FROM success WHERE title LIKE :searchTerm ORDER BY :sortType DESC")
    fun getAllDESC(searchTerm: String, sortType: String): MutableList<LocalSuccess>

    @Query("SELECT * FROM success WHERE title LIKE :title LIMIT 1")
    fun findByName(title: String): LocalSuccess

    @Query("SELECT * FROM success WHERE id LIKE :id")
    fun fetchSuccessById(id: Long): LocalSuccess

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(successes: List<LocalSuccess>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(success: LocalSuccess)

    @Delete
    fun delete(success: LocalSuccess)

    @Query("delete from success")
    fun removeAll()

}
