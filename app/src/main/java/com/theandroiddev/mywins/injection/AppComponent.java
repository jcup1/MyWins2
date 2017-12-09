package com.theandroiddev.mywins.injection;

import com.theandroiddev.mywins.data.db.AppDatabase;
import com.theandroiddev.mywins.editsuccess.EditSuccessActivity;
import com.theandroiddev.mywins.editsuccess.EditSuccessPresenter;
import com.theandroiddev.mywins.successes.SuccessesActivity;
import com.theandroiddev.mywins.successes.SuccessesPresenter;
import com.theandroiddev.mywins.successslider.SuccessSliderActivity;
import com.theandroiddev.mywins.successslider.SuccessSliderContract;
import com.theandroiddev.mywins.successslider.SuccessSliderPresenter;
import com.theandroiddev.mywins.utils.SuccessesConfig;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jakub on 07.11.17.
 */

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class})
public interface AppComponent {
    void inject(SuccessesActivity target);

    void inject(SuccessesPresenter target);

    void inject(SuccessSliderActivity target);

    void inject(SuccessSliderPresenter target);

    void inject(SuccessSliderContract.SuccessImageLoader target);

    void inject(EditSuccessActivity target);

    void inject(EditSuccessPresenter target);

    void inject(AppDatabase target);

    void inject(SuccessesConfig target);



}
