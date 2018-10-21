package com.theandroiddev.mywins.local.dao

import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.*
import com.theandroiddev.mywins.local.model.LocalSuccess
import org.intellij.lang.annotations.Language

@Dao
interface SuccessDao {

    //TODO replace ASC DESC
    @RawQuery
    fun getAll(query: SupportSQLiteQuery): MutableList<LocalSuccess>

    @Query("SELECT * FROM success WHERE title LIKE :title LIMIT 1")
    fun findByName(title: String): LocalSuccess

    @Query("SELECT * FROM success WHERE id LIKE :id")
    fun fetchSuccessById(id: Long): LocalSuccess

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(successes: List<LocalSuccess>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(success: LocalSuccess)

    @Delete
    fun delete(successes: List<LocalSuccess>)

    @Query("delete from success")
    fun removeAll()

}
