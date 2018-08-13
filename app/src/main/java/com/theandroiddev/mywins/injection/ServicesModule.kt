package com.theandroiddev.mywins.injection

import android.content.Context
import com.theandroiddev.mywins.data.db.DBAdapter
import com.theandroiddev.mywins.data.db.DBHelper
import com.theandroiddev.mywins.data.prefs.SharedPreferencesService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServicesModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesService(context: Context) = SharedPreferencesService(context)

    @Provides
    @Singleton
    fun provideDatabaseAdapter(context: Context) = DBAdapter(DBHelper(context))

    @Provides
    @Singleton
    fun provideDatabaseHelper(context: Context) = DBHelper(context)

}

