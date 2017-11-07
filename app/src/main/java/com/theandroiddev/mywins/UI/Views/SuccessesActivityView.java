package com.theandroiddev.mywins.UI.Views;

import com.theandroiddev.mywins.UI.Models.Success;

import java.util.ArrayList;

/**
 * Created by jakub on 28.10.17.
 */

public interface SuccessesActivityView {

    void displayDefaultSuccesses(ArrayList<Success> successList);

    void displayNoDefaultSuccesses();

    void displayNoSuccesses();

    void displaySuccesses(ArrayList<Success> successList);

    void updateAdapterList(ArrayList<Success> successList);
}
