package com.theandroiddev.mywins.data.dao

import android.arch.persistence.room.*
import com.theandroiddev.mywins.data.entity.SuccessImageEntity

@Dao
interface SuccessImageDao {

    //TODO replace ASC DESC
    @Query("SELECT * FROM SuccessImageEntity WHERE id LIKE :successId")
    fun getAll(successId: Long): MutableList<SuccessImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(successImageList: List<SuccessImageEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(successImage: SuccessImageEntity)

    @Delete
    fun delete(successImage: SuccessImageEntity)

    @Query("DELETE FROM SuccessImageEntity WHERE successId = :successId")
    fun deleteSuccessImages(successId: Long)

    @Query("delete from SuccessImageEntity")
    fun removeAll()
}
