package com.theandroiddev.mywins.data.dao

import android.arch.persistence.room.*
import com.theandroiddev.mywins.data.entity.SuccessEntity

@Dao
interface SuccessDao {

    //TODO replace ASC DESC
    @Query("SELECT * FROM success WHERE title LIKE :searchTerm ORDER BY :sortType ASC")
    fun getAllASC(searchTerm: String, sortType: String): MutableList<SuccessEntity>

    @Query("SELECT * FROM success WHERE title LIKE :searchTerm ORDER BY :sortType DESC")
    fun getAllDESC(searchTerm: String, sortType: String): MutableList<SuccessEntity>

    @Query("SELECT * FROM success WHERE title LIKE :title LIMIT 1")
    fun findByName(title: String): SuccessEntity

    @Query("SELECT * FROM success WHERE id LIKE :id")
    fun fetchSuccessById(id: Long): SuccessEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(successList: MutableList<SuccessEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(success: SuccessEntity): Int

    @Delete
    fun delete(success: SuccessEntity): Int

    @Query("delete from success")
    fun removeAll(): Int

}
