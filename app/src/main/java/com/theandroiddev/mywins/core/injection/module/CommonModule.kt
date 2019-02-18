package com.theandroiddev.mywins.core.injection.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.theandroiddev.mywins.utils.SuccessesConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonModule {

    @Provides
    fun contributeSuccessConfig() = SuccessesConfig()

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
            .create()
    }

}