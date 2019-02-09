package com.theandroiddev.mywins.core.injection.module

import android.app.Application
import androidx.room.Room
import com.theandroiddev.mywins.local.dao.SuccessDao
import com.theandroiddev.mywins.local.dao.SuccessImageDao
import com.theandroiddev.mywins.local.database.AppDatabase
import com.theandroiddev.mywins.local.database.Migrations
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppatabase(application: Application): AppDatabase =
            Room.databaseBuilder(
                    application.applicationContext,
                    AppDatabase::class.java,
                    "mywins.db"
            )
                    .addMigrations(Migrations.MIGRATION_1_2)
                    .build()

    @Provides
    fun provideSuccessDao(database: AppDatabase): SuccessDao = database.localSuccessDao()

    @Provides
    fun provideSuccessImageDao(database: AppDatabase): SuccessImageDao = database.localSuccessImageDao()

}
