package com.theandroiddev.mywins.injection.module

import com.theandroiddev.mywins.injection.scope.FragmentScope
import com.theandroiddev.mywins.presentation.success_slider.SuccessSliderFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeSuccessSliderFragment(): SuccessSliderFragment

}
