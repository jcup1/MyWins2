package com.theandroiddev.mywins.injection

import com.theandroiddev.mywins.edit_success.EditSuccessActivity
import com.theandroiddev.mywins.success_slider.SuccessSliderActivity
import com.theandroiddev.mywins.successes.SuccessesActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeSuccessesActivity(): SuccessesActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeSuccessSliderActivity(): SuccessSliderActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeEditSuccessActivity(): EditSuccessActivity


}
