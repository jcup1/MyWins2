package com.theandroiddev.mywins.UI.Activities;

import com.theandroiddev.mywins.UI.Helpers.Constants;
import com.theandroiddev.mywins.UI.Models.Success;
import com.theandroiddev.mywins.UI.Views.SuccessesActivityView;
import com.theandroiddev.mywins.UI.repositories.SuccessesRepository;

import java.util.ArrayList;

/**
 * Created by jakub on 04.11.17.
 */

class SuccessesActivityPresenter {

    private SuccessesActivityView view;
    private SuccessesRepository successesRepository;

    public SuccessesActivityPresenter(SuccessesActivityView view, SuccessesRepository successesRepository) {
        this.view = view;
        this.successesRepository = successesRepository;
    }


    public void loadDefaultSuccesses() {
        ArrayList<Success> successList = successesRepository.getSuccessesWithNewSorting("", Constants.SORT_DATE_ADDED, true);

        if (successList.isEmpty()) {
            view.displayNoDefaultSuccesses();
        } else {
            view.displayDefaultSuccesses(successList);
        }
    }

    public void loadSuccesses() {
        ArrayList<Success> successList = successesRepository.getSuccessesWithNewSorting("", Constants.SORT_DATE_ADDED, true);

        if (successList.isEmpty()) {
            view.displayNoSuccesses();
        } else {
            view.displaySuccesses(successList);
        }
    }


}
