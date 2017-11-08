package com.theandroiddev.mywins.UI.Activities;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;

import com.theandroiddev.mywins.MyWinsApplication;
import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.UI.Helpers.Constants;
import com.theandroiddev.mywins.UI.Models.Success;
import com.theandroiddev.mywins.UI.Views.SuccessesActivityView;
import com.theandroiddev.mywins.UI.repositories.SuccessesRepository;
import com.theandroiddev.mywins.local.PreferencesHelper;

import java.util.ArrayList;

import static com.theandroiddev.mywins.UI.Helpers.Constants.NOT_ACTIVE;

/**
 * Created by jakub on 04.11.17.
 */

public class SuccessesActivityPresenterImpl implements SuccessesActivityPresenter {
    private static final String TAG = "SuccessesActivityPresen";
    ArrayList<Success> successList;
    ArrayList<Success> successToRemoveList;
    Success backupSuccess;
    PreferencesHelper preferencesHelper;
    private SuccessesActivityView view;
    private SuccessesRepository successesRepository;
    private String sortType;
    private boolean isSortingAscending;
    private boolean isSearchOpened = false;
    private String searchText;

    public SuccessesActivityPresenterImpl(Context context) {
        ((MyWinsApplication) context).getAppComponent().inject(this);
        successList = new ArrayList<>();
        successToRemoveList = new ArrayList<>();
        sortType = Constants.SORT_DATE_ADDED;
        isSortingAscending = true;
    }

    @Override
    public void loadSuccesses() {

        successList = successesRepository.getSuccesses(searchText, sortType, isSortingAscending);

        if (successList.isEmpty()) {
            view.displayNoSuccesses();
        } else {
            view.displaySuccesses(successList);
        }
    }

    @Override
    public void setView(SuccessesActivityView view) {
        this.view = view;
    }

    @Override
    public void setRepository(SuccessesRepository repository) {
        this.successesRepository = repository;
    }

    @Override
    public void removeSuccessesFromQueue() {
        Log.d(TAG, "removeSuccessesFromQueue: BEF1" + successToRemoveList);
        successesRepository.removeSuccesses(successToRemoveList);
        successToRemoveList.clear();
        Log.d(TAG, "removeSuccessesFromQueue: BEF2" + successToRemoveList);

    }

    @Override
    public void undoToRemove(final int position) {
        Log.d(TAG, "onSwipedUTR: " + position);
        successList.add(position, backupSuccess);
        view.undoToRemove(position);
        successToRemoveList.remove(backupSuccess);

    }

    @Override
    public void sendToRemoveQueue(final int position) {
        Log.d(TAG, "onSwipedSTRQ: " + position);
        successList.remove(position);
        view.successRemoved(position);
        successToRemoveList.add(backupSuccess);
        Log.d(TAG, "sendToRemoveQueue: " + successList);
        if (successList.isEmpty()) {
            loadSuccesses();
        }

    }

    @Override
    public void backupSuccess(int position) {
        backupSuccess = successList.get(position);
    }

    @Override
    public void updateSuccess(int clickedPosition) {

        if (clickedPosition != NOT_ACTIVE) {
            int id = successList.get(clickedPosition).getId();
            successList.set(clickedPosition, successesRepository.getSuccess(id));
            view.displaySuccessChanged();
        }

    }

    @Override
    public void categoryPicked(String category) {
        view.displayCategory(category);
    }

    @Override
    public void setPrefHelper(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;

    }

    @Override
    public void handleOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            handleMenuSearch();
        }//TODO it's so dumb
        if (id == R.id.action_date_started) {

            if (!sortType.equals(Constants.SORT_DATE_STARTED)) {
                sortType = Constants.SORT_DATE_STARTED;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            loadSuccesses();

        }
        if (id == R.id.action_date_ended) {

            if (!sortType.equals(Constants.SORT_DATE_ENDED)) {
                sortType = Constants.SORT_DATE_ENDED;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            loadSuccesses();

        }
        if (id == R.id.action_title) {

            if (!sortType.equals(Constants.SORT_TITLE)) {
                sortType = Constants.SORT_TITLE;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            loadSuccesses();

        }
        if (id == R.id.action_date_added) {

            if (!sortType.equals(Constants.SORT_DATE_ADDED)) {
                sortType = Constants.SORT_DATE_ADDED;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            loadSuccesses();

        }
        if (id == R.id.action_importance) {

            if (!sortType.equals(Constants.SORT_IMPORTANCE)) {
                sortType = Constants.SORT_IMPORTANCE;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            loadSuccesses();

        }
        if (id == R.id.action_description) {

            if (!sortType.equals(Constants.SORT_DESCRIPTION)) {
                sortType = Constants.SORT_DESCRIPTION;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            loadSuccesses();

        }
    }

    @Override
    public void handleMenuSearch() {
        if (isSearchOpened) {
            view.hideSearchBar();
            isSearchOpened = false;
            view.displaySuccesses(successesRepository.getSuccesses("", sortType, isSortingAscending));
        } else {
            view.displaySearchBar();
            isSearchOpened = true;
        }
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public void addSuccess(Success s) {

        if (!preferencesHelper.isFirstSuccessAdded()) {
            clearSuccesses();
            preferencesHelper.setFirstSuccessAdded();
        }
        successesRepository.addSuccess(s);
        loadSuccesses();
        view.displayUpdatedSuccesses();

    }

    @Override
    public void handleFirstSuccessPreference() {

        if (!preferencesHelper.isFirstSuccessAdded()) {

            view.displayDefaultSuccesses(successesRepository.getDefaultSuccesses());

        }
    }

    @Override
    public void openDB() {
        successesRepository.openDB();
    }


    @Override
    public void closeDB() {
        successesRepository.closeDB();
    }

    private void clearSuccesses() {
        successList.clear();
    }

}
