package com.theandroiddev.mywins.UI.activities;

import android.view.MenuItem;

import com.theandroiddev.mywins.UI.models.Success;
import com.theandroiddev.mywins.UI.views.SuccessesActivityView;
import com.theandroiddev.mywins.data.prefs.PreferencesHelper;
import com.theandroiddev.mywins.data.repositories.SuccessesRepository;

/**
 * Created by jakub on 07.11.17.
 */

public interface SuccessesActivityPresenter {

    void loadSuccesses();

    void setView(SuccessesActivityView successesActivityView);

    void setRepository(SuccessesRepository repository);

    void removeSuccessesFromQueue();

    void undoToRemove(final int position);

    void sendToRemoveQueue(final int position);

    void backupSuccess(int position);

    void updateSuccess(int clickedPosition);

    void categoryPicked(String category);

    void setPrefHelper(PreferencesHelper preferencesHelper);

    void handleOptionsItemSelected(MenuItem item);

    void handleMenuSearch();

    void setSearchText(String searchText);

    void addSuccess(Success s);

    void closeDB();

    void handleFirstSuccessPreference();

    void openDB();
}