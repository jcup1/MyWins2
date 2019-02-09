package com.theandroiddev.mywins.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.theandroiddev.mywins.local.model.LocalSuccess
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface SuccessDao {

    //TODO replace ASC DESC
    @RawQuery
    fun getAll(query: SupportSQLiteQuery): Single<MutableList<LocalSuccess>>

    @Query("SELECT * FROM success WHERE title LIKE :title LIMIT 1")
    fun findByName(title: String): Single<LocalSuccess>

    @Query("SELECT * FROM success WHERE id LIKE :id")
    fun fetchSuccessById(id: Long): Single<LocalSuccess>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(successes: List<LocalSuccess>): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(success: LocalSuccess): Completable

    @Delete
    fun delete(success: LocalSuccess): Completable

    @Query("delete from success")
    fun removeAll(): Completable

}
