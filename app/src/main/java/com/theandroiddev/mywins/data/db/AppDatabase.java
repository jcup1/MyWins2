package com.theandroiddev.mywins.data.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.models.SuccessDao;
import com.theandroiddev.mywins.data.models.SuccessImage;

/**
 * Created by grazyna on 2017-11-18.
 */
@Database(entities = {Success.class, SuccessImage.class},
        version = 10, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {


        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "SuccessDatabase.db")
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract SuccessDao successDao();

    public abstract SuccessImage successImageDao();

}
