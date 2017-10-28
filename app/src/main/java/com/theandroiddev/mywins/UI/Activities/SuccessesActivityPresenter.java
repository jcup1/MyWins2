package com.theandroiddev.mywins.UI.Activities;

import com.theandroiddev.mywins.UI.Models.Success;
import com.theandroiddev.mywins.UI.Views.SuccessesActivityView;
import com.theandroiddev.mywins.UI.repositories.SuccessesRepository;

import java.util.List;

/**
 * Created by jakub on 28.10.17.
 */

class SuccessesActivityPresenter {

    private SuccessesActivityView view;
    private SuccessesRepository successesRepository;

    public SuccessesActivityPresenter(SuccessesActivityView view, SuccessesRepository successesRepository) {
        this.view = view;
        this.successesRepository = successesRepository;
    }

    public void loadSuccesses() {
        List<Success> successList = successesRepository.getSuccesses();
        if (successList.isEmpty()) {
            view.displayNoSuccesses();
        } else {
            view.displaySuccesses(successList);
        }
    }
}
