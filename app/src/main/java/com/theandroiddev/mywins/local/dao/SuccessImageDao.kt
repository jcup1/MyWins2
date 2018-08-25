package com.theandroiddev.mywins.local.dao

import android.arch.persistence.room.*
import com.theandroiddev.mywins.local.model.LocalSuccessImage

@Dao
interface SuccessImageDao {

    //TODO replace ASC DESC
    @Query("SELECT * FROM LocalSuccessImage WHERE successId LIKE :successId")
    fun getAll(successId: Long): MutableList<LocalSuccessImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(successImageList: List<LocalSuccessImage>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(successImage: LocalSuccessImage)

    @Delete
    fun delete(successImage: LocalSuccessImage)

    @Query("DELETE FROM LocalSuccessImage WHERE successId = :successId")
    fun deleteSuccessImages(successId: Long)

    @Query("delete from LocalSuccessImage")
    fun removeAll()
}
