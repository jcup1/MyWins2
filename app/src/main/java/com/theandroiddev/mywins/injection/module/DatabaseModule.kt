package com.theandroiddev.mywins.injection.module

import android.app.Application
import android.arch.persistence.room.Room
import com.theandroiddev.mywins.data.dao.SuccessDao
import com.theandroiddev.mywins.data.dao.SuccessImageDao
import com.theandroiddev.mywins.data.database.AppDatabase
import com.theandroiddev.mywins.data.database.Migrations
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
    fun provideSuccessDao(database: AppDatabase): SuccessDao = database.successDao()

    @Provides
    fun provideSuccessImageDao(database: AppDatabase): SuccessImageDao = database.successImageDao()

}
