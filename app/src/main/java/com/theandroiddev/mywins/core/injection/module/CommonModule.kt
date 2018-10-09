package com.theandroiddev.mywins.core.injection.module

import com.theandroiddev.mywins.utils.SuccessesConfig
import dagger.Module
import dagger.Provides

@Module
class CommonModule {

    @Provides
    fun contributeSuccessConfig() = SuccessesConfig()

}