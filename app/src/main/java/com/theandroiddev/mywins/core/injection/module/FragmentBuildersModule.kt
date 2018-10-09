package com.theandroiddev.mywins.core.injection.module

import com.theandroiddev.mywins.core.injection.scope.FragmentScope
import com.theandroiddev.mywins.presentation.success_slider.SuccessSliderFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeSuccessSliderFragment(): SuccessSliderFragment

}
