package com.theandroiddev.mywins.UI.Activities;

import android.content.Context;

import com.theandroiddev.mywins.MyWinsApplication;
import com.theandroiddev.mywins.UI.Helpers.Constants;
import com.theandroiddev.mywins.UI.Models.Success;
import com.theandroiddev.mywins.UI.Views.SuccessesActivityView;
import com.theandroiddev.mywins.UI.repositories.SuccessesRepository;

import java.util.ArrayList;

/**
 * Created by jakub on 04.11.17.
 */

public class SuccessesActivityPresenterImpl implements SuccessesActivityPresenter {

    private SuccessesActivityView view;
    private SuccessesRepository successesRepository;

    public SuccessesActivityPresenterImpl(SuccessesActivityView view, SuccessesRepository successesRepository) {
        this.view = view;
        this.successesRepository = successesRepository;
    }

    public SuccessesActivityPresenterImpl(Context context) {
        ((MyWinsApplication) context).getAppComponent().inject(this);

    }

    @Override
    public void loadSuccesses() {


//        successList = new ArrayList<>();
//        successToRemoveList = new ArrayList<>();
//
//        successAdapter = new SuccessAdapter(successList, R.layout.success_layout, getApplicationContext(), this);
//        recyclerView.setAdapter(successAdapter);

        ArrayList<Success> successList = successesRepository.getSuccessesWithNewSorting("", Constants.SORT_DATE_ADDED, true);

        if (successList.isEmpty()) {
            view.displayNoDefaultSuccesses();
        } else {
            view.displayDefaultSuccesses(successList);
        }
    }

    @Override
    public void setView(SuccessesActivityView view) {
        this.view = view;
    }

    /*public void loadSuccesses() {
        ArrayList<Success> successList = successesRepository.getSuccessesWithNewSorting("", Constants.SORT_DATE_ADDED, true);

        if (successList.isEmpty()) {
            view.displayNoSuccesses();
        } else {
            view.displaySuccesses(successList);
        }
    }*/


}
