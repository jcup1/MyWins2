package com.theandroiddev.mywins.local.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.theandroiddev.mywins.local.dao.SuccessDao;
import com.theandroiddev.mywins.local.dao.SuccessImageDao;
import com.theandroiddev.mywins.local.model.LocalSuccess;
import com.theandroiddev.mywins.local.model.LocalSuccessImage;

@Database(entities = {LocalSuccess.class, LocalSuccessImage.class},
        version = 10, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SuccessDao localSuccessDao();

    public abstract SuccessImageDao localSuccessImageDao();

}
