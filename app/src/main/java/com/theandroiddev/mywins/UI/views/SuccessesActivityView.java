package com.theandroiddev.mywins.UI.views;

import com.theandroiddev.mywins.UI.models.Success;

import java.util.ArrayList;

/**
 * Created by jakub on 28.10.17.
 */

public interface SuccessesActivityView {

    void displayDefaultSuccesses(ArrayList<Success> successList);

    void displayNoSuccesses();

    void displaySuccesses(ArrayList<Success> successList);

    void updateAdapterList(ArrayList<Success> successList);

    void undoToRemove(int position);

    void successRemoved(int position);

    void displaySuccessChanged();

    void displayCategory(String category);

    void hideSearchBar();

    void displaySearchBar();

    void displayUpdatedSuccesses();
}
