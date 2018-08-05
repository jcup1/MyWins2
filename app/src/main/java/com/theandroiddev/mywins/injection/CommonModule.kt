package com.theandroiddev.mywins.injection

import com.theandroiddev.mywins.success_slider.SuccessImageLoader
import com.theandroiddev.mywins.success_slider.SuccessImageLoaderImpl
import com.theandroiddev.mywins.utils.SuccessesConfig
import dagger.Module
import dagger.Provides

@Module
class CommonModule {

    @Provides
    fun contributeSuccessConfig() = SuccessesConfig()

    @Provides
    fun SuccessImageLoader(successImageLoaderImpl: SuccessImageLoaderImpl): SuccessImageLoader = successImageLoaderImpl
}