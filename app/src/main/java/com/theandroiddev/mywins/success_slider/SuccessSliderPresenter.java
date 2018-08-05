package com.theandroiddev.mywins.success_slider;

import android.util.Log;

import com.theandroiddev.mywins.data.models.SearchFilter;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.repositories.SuccessesRepository;
import com.theandroiddev.mywins.mvp.MvpPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by jakub on 12.11.17.
 */

public class SuccessSliderPresenter extends MvpPresenter<SuccessSliderView> {
    private static final String TAG = "SuccessSliderPresenter";

    private SuccessSliderView view;
    private SuccessesRepository successesRepository;
    private ArrayList<Success> successList;

    @Inject
    public SuccessSliderPresenter() {
    }

    public void setView(SuccessSliderView view) {
        this.view = view;
    }

    public void setRepository(SuccessesRepository repository) {
        this.successesRepository = repository;
    }

    public void dropView() {
        view = null;
    }


    public void loadSuccesses(SearchFilter searchFilter) {
        successList = successesRepository.getSuccesses(searchFilter);

        view.displaySuccesses(successList);
    }

    public void openDB() {
        successesRepository.openDB();
    }

    public void closeDB() {
        successesRepository.closeDB();
    }

    public void startEditSuccess(int currentItem) {
        Log.d(TAG, "startEditSuccess: id " + successList.get(currentItem).getId());
        view.displayEditSuccessActivity(successList.get(currentItem).getId());
    }
}
