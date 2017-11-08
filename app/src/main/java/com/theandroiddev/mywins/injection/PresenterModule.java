package com.theandroiddev.mywins.injection;

import android.content.Context;

import com.theandroiddev.mywins.UI.activities.SuccessesActivityPresenter;
import com.theandroiddev.mywins.UI.activities.SuccessesActivityPresenterImpl;

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
    SuccessesActivityPresenter provideSuccessesActivityPresenter(Context context) {
        return new SuccessesActivityPresenterImpl(context);
    }
}
