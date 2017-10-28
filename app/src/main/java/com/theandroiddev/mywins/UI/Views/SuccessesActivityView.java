package com.theandroiddev.mywins.UI.Views;

import com.theandroiddev.mywins.UI.Models.Success;

import java.util.List;

/**
 * Created by jakub on 28.10.17.
 */

public interface SuccessesActivityView {

    void displaySuccesses(List<Success> successList);

    void displayNoSuccesses();
}
