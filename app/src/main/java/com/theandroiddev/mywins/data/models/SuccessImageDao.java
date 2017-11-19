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
public interface SuccessImageDao {

    //TODO replace ASC DESC
    @Query("SELECT * FROM successImage WHERE id LIKE :successId")
    List<SuccessImage> getAll(String successId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrayList<SuccessImage> successImageList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(SuccessImage successImage);

    @Delete
    void delete(SuccessImage successImage);

    @Query("delete from successImage")
    void removeAll();
}
