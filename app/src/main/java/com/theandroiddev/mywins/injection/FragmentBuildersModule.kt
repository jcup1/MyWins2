package com.theandroiddev.mywins.injection

import com.theandroiddev.mywins.success_slider.SuccessSliderFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeSuccessSliderFragment(): SuccessSliderFragment

}
