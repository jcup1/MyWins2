package com.theandroiddev.mywins.UI.Activities;

import com.theandroiddev.mywins.UI.Views.SuccessesActivityView;

/**
 * Created by jakub on 07.11.17.
 */

public interface SuccessesActivityPresenter {

    void loadSuccesses();

    void setView(SuccessesActivityView successesActivityView);
}