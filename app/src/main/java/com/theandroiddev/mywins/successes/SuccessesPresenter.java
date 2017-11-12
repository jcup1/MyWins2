package com.theandroiddev.mywins.successes;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;

import com.theandroiddev.mywins.MyWinsApplication;
import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.prefs.PreferencesHelper;
import com.theandroiddev.mywins.data.repositories.SuccessesRepository;
import com.theandroiddev.mywins.utils.Constants;

import java.util.ArrayList;

import static com.theandroiddev.mywins.utils.Constants.NOT_ACTIVE;

/**
 * Created by jakub on 04.11.17.
 */

public class SuccessesPresenter implements SuccessesContract.Presenter {
    private static final String TAG = "SuccessesActivityPresen";
    ArrayList<Success> successList;
    ArrayList<Success> successToRemoveList;
    Success backupSuccess;
    PreferencesHelper preferencesHelper;
    private SuccessesContract.View view;
    private SuccessesRepository successesRepository;
    private String sortType;
    private boolean isSortingAscending;
    private boolean isSearchOpened = false;
    private String searchTerm;

    public SuccessesPresenter(Context context) {
        ((MyWinsApplication) context).getAppComponent().inject(this);
        successList = new ArrayList<>();
        successToRemoveList = new ArrayList<>();
        sortType = Constants.SORT_DATE_ADDED;
        isSortingAscending = true;
    }

    @Override
    public void loadSuccesses() {

        successList = successesRepository.getSuccesses(getSearchFilter());

        if (successList.isEmpty()) {
            view.displayNoSuccesses();
        } else {
            view.displaySuccesses(successList);
        }
    }

    @Override
    public void setRepository(SuccessesRepository repository) {
        this.successesRepository = repository;
    }

    @Override
    public void removeSuccessesFromQueue() {
        successesRepository.removeSuccesses(successToRemoveList);
        successToRemoveList.clear();

    }

    @Override
    public void undoToRemove(final int position) {
        successList.add(position, backupSuccess);
        view.undoToRemove(position);
        successToRemoveList.remove(backupSuccess);

    }

    @Override
    public void sendToRemoveQueue(final int position) {
        successList.remove(position);
        view.successRemoved(position);
        successToRemoveList.add(backupSuccess);
        //TODO handle it
        if (successList.isEmpty()) {
            //loadSuccesses();
            view.displayNoSuccesses();
        }

    }

    @Override
    public void backupSuccess(int position) {
        backupSuccess = successList.get(position);
    }

    @Override
    public void updateSuccess(int clickedPosition) {
        //TODO check it later
        if (preferencesHelper.isFirstSuccessAdded() && successList.size() > clickedPosition) {

            if (clickedPosition != NOT_ACTIVE) {
                String id = successList.get(clickedPosition).getId();
                successList.set(clickedPosition, successesRepository.getSuccess(id));
                view.displaySuccessChanged();
            }
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
            view.displaySuccesses(successesRepository.getSuccesses(getSearchFilter()));
        } else {
            view.displaySearchBar();
            isSearchOpened = true;
        }
    }

    @Override
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
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
    public void startSlider() {
        Log.d(TAG, "startSlider: " + successList.get(0).getId());
        view.displaySlider(successesRepository.getSuccesses(getSearchFilter()));
    }

    @Override
    public SearchFilter getSearchFilter() {
        return new SearchFilter(searchTerm, sortType, isSortingAscending);
    }

    @Override
    public void clearSearch() {
        searchTerm = "";
    }


    @Override
    public void closeDB() {
        successesRepository.closeDB();
    }

    private void clearSuccesses() {
        successList.clear();
    }

    @Override
    public void setView(SuccessesContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }
}
