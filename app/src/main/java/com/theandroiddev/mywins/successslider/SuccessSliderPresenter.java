package com.theandroiddev.mywins.successslider;

import android.content.Context;

import com.theandroiddev.mywins.MyWinsApplication;
import com.theandroiddev.mywins.data.repositories.SuccessesRepository;
import com.theandroiddev.mywins.successes.SearchFilter;

/**
 * Created by jakub on 12.11.17.
 */

public class SuccessSliderPresenter implements SuccessSliderContract.Presenter {

    private SuccessSliderContract.View view;
    private SuccessesRepository successesRepository;

    public SuccessSliderPresenter(Context context) {
        ((MyWinsApplication) context).getAppComponent().inject(this);
    }

    @Override
    public void setView(SuccessSliderContract.View view) {
        this.view = view;
    }

    @Override
    public void setRepository(SuccessesRepository repository) {
        this.successesRepository = repository;
    }

    @Override
    public void dropView() {
        view = null;
    }


    @Override
    public void loadSuccesses(SearchFilter searchFilter) {

        view.displaySuccesses(successesRepository.getSuccesses(searchFilter));
    }

    @Override
    public void openDB() {
        successesRepository.openDB();
    }

    @Override
    public void closeDB() {
        successesRepository.closeDB();
    }
}
