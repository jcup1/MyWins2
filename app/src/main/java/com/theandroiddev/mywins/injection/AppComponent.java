package com.theandroiddev.mywins.injection;

import com.theandroiddev.mywins.UI.activities.SuccessesActivity;
import com.theandroiddev.mywins.UI.activities.SuccessesActivityPresenterImpl;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jakub on 07.11.17.
 */

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class})
public interface AppComponent {
    void inject(SuccessesActivity target);

    //    void inject(SuccessAdapter target);
    void inject(SuccessesActivityPresenterImpl target);

}
