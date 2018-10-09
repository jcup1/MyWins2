package com.theandroiddev.mywins.core.injection.module

import com.theandroiddev.mywins.core.injection.scope.ActivityScope
import com.theandroiddev.mywins.presentation.edit_success.EditSuccessActivity
import com.theandroiddev.mywins.presentation.importance_popup.ImportancePopupActivity
import com.theandroiddev.mywins.presentation.insert_success.InsertSuccessActivity
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

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeImportancePopupActivity(): ImportancePopupActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeInsertSuccessActivity(): InsertSuccessActivity

}
