package com.theandroiddev.mywins;

import android.app.Application;

import com.theandroiddev.mywins.injection.AppComponent;
import com.theandroiddev.mywins.injection.AppModule;
import com.theandroiddev.mywins.injection.DaggerAppComponent;

/**
 * Created by jakub on 07.11.17.
 */ 

public class MyWinsApplication extends Application {

    private AppComponent appComponent;

    @Override

    public void onCreate() {
        super.onCreate();
        appComponent = initDagger(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    protected AppComponent initDagger(MyWinsApplication application) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build();
    }

}
