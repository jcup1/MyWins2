package com.theandroiddev.mywins.successslider;

import android.content.Context;
import android.util.Log;

import com.theandroiddev.mywins.MyWinsApplication;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.repositories.SuccessesRepository;
import com.theandroiddev.mywins.successes.SearchFilter;

import java.util.ArrayList;

/**
 * Created by jakub on 12.11.17.
 */

public class SuccessSliderPresenter implements SuccessSliderContract.Presenter {
    private static final String TAG = "SuccessSliderPresenter";

    private SuccessSliderContract.View view;
    private SuccessesRepository successesRepository;
    private ArrayList<Success> successList;

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
        successList = successesRepository.getSuccesses(searchFilter);

        view.displaySuccesses(successList);
    }

    @Override
    public void openDB() {
        successesRepository.openDB();
    }

    @Override
    public void closeDB() {
        successesRepository.closeDB();
    }

    @Override
    public void startEditSuccess(int currentItem) {
        Log.d(TAG, "startEditSuccess: id " + successList.get(currentItem).getId());
        view.displayEditSuccessActivity(successList.get(currentItem).getId());
    }
}
