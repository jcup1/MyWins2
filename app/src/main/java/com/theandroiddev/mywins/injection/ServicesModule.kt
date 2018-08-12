package com.theandroiddev.mywins.injection

import android.content.Context
import com.theandroiddev.mywins.data.prefs.SharedPreferencesService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServicesModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesService(context: Context) = SharedPreferencesService(context)

}

