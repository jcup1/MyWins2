package com.theandroiddev.mywins.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.theandroiddev.mywins.data.dao.SuccessDao;
import com.theandroiddev.mywins.data.dao.SuccessImageDao;
import com.theandroiddev.mywins.data.entity.SuccessEntity;
import com.theandroiddev.mywins.data.entity.SuccessImageEntity;

@Database(entities = {SuccessEntity.class, SuccessImageEntity.class},
        version = 10, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SuccessDao successDao();

    public abstract SuccessImageDao successImageDao();

}
