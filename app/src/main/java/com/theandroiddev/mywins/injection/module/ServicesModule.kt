package com.theandroiddev.mywins.injection.module

import android.content.Context
import com.theandroiddev.mywins.domain.service.shared_preferences.SharedPreferencesService
import com.theandroiddev.mywins.domain.service.successes.SuccessesService
import com.theandroiddev.mywins.domain.service.successes.SuccessesServiceImpl
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
    fun provideSuccessesService(successesServiceImpl: SuccessesServiceImpl): SuccessesService = successesServiceImpl

}

