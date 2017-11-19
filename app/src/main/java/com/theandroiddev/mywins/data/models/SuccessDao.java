package com.theandroiddev.mywins.data.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grazyna on 2017-11-18.
 */
@Dao
public interface SuccessDao {

    //TODO replace ASC DESC
    @Query("SELECT * FROM success WHERE title LIKE :searchTerm ORDER BY :sortType ASC")
    List<Success> getAllASC(String searchTerm, String sortType);

    @Query("SELECT * FROM success WHERE title LIKE :searchTerm ORDER BY :sortType DESC")
    List<Success> getAllDESC(String searchTerm, String sortType);

    @Query("SELECT * FROM success WHERE title LIKE :title LIMIT 1")
    Success findByName(String title);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrayList<Success> successList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Success success);

    @Delete
    void delete(Success success);

    @Query("delete from success")
    void removeAll();

}
