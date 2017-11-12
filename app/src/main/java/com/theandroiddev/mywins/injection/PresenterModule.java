package com.theandroiddev.mywins.injection;

import android.content.Context;

import com.theandroiddev.mywins.successes.SuccessesContract;
import com.theandroiddev.mywins.successes.SuccessesPresenter;
import com.theandroiddev.mywins.successslider.SuccessSliderContract;
import com.theandroiddev.mywins.successslider.SuccessSliderPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jakub on 07.11.17.
 */

@Module
public class PresenterModule {

    @Provides
    @Singleton
    SuccessesContract.Presenter provideSuccessesActivityPresenter(Context context) {
        return new SuccessesPresenter(context);
    }

    @Provides
    @Singleton
    SuccessSliderContract.Presenter provideSuccessesSliderActivityPresenter(Context context) {
        return new SuccessSliderPresenter(context);
    }
}
