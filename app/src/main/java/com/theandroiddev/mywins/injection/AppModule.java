package com.theandroiddev.mywins.injection;

import android.app.Application;
import android.content.Context;

import com.theandroiddev.mywins.utils.SuccessesConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jakub on 07.11.17.
 */

@Module
public class AppModule {
    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }

    @Provides
    SuccessesConfig successesConfig() {
        return new SuccessesConfig();
    }
}
