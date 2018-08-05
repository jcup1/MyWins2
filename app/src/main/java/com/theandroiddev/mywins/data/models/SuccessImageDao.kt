package com.theandroiddev.mywins.data.models

import android.arch.persistence.room.*
import java.util.*

/**
 * Created by grazyna on 2017-11-18.
 */
@Dao
interface SuccessImageDao {

    //TODO replace ASC DESC
    @Query("SELECT * FROM successImage WHERE id LIKE :successId")
    fun getAll(successId: String): List<SuccessImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(successImageList: ArrayList<SuccessImage>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(successImage: SuccessImage)

    @Delete
    fun delete(successImage: SuccessImage)

    @Query("delete from successImage")
    fun removeAll()
}
