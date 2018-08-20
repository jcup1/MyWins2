package com.theandroiddev.mywins.injection.module

import com.theandroiddev.mywins.injection.scope.ActivityScope
import com.theandroiddev.mywins.presentation.edit_success.EditSuccessActivity
import com.theandroiddev.mywins.presentation.success_slider.SuccessSliderActivity
import com.theandroiddev.mywins.presentation.successes.SuccessesActivity
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
