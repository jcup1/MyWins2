package com.theandroiddev.mywins.data.repositories;

import android.content.Context;

import com.theandroiddev.mywins.data.db.DBAdapter;
import com.theandroiddev.mywins.data.models.SearchFilter;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.models.SuccessImage;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by jakub on 04.11.17.
 */

public class DatabaseSuccessesRepository implements SuccessesRepository {
    private static final String TAG = "DatabaseSuccessesReposi";

    private DBAdapter dbAdapter;
    private SuccessesRepository mSuccessRepository;
    private Context context;

    @Inject
    public DatabaseSuccessesRepository(Context context) {
        dbAdapter = new DBAdapter(context);
        this.context = context;
    }

    @Override
    public void updateForDeleteSuccess(ArrayList<Success> successToRemoveList) {
        dbAdapter.removeSuccess(successToRemoveList);
    }

    @Override
    public ArrayList<Success> getSuccesses(SearchFilter searchFilter) {
        dbAdapter = new DBAdapter(context);
        return dbAdapter.getSuccesses(searchFilter.getSearchTerm(), searchFilter.getSortType(), searchFilter.isSortingAscending());
    }

    @Override
    public void registerRepository(DatabaseSuccessesRepository databaseSuccessesRepository) {
        this.mSuccessRepository = databaseSuccessesRepository;
    }

    @Override
    public void unRegisterRepository() {
        mSuccessRepository = null;
    }

    @Override
    public void removeSuccesses(ArrayList<Success> successToRemoveList) {
        dbAdapter.removeSuccess(successToRemoveList);
    }

    @Override
    public Success getSuccess(Long id) {
        return dbAdapter.fetchSuccess(id);
    }

    @Override
    public ArrayList<Success> getDefaultSuccesses() {
        return dbAdapter.getDefaultSuccesses();
    }

    @Override
    public void addSuccess(Success s) {
        dbAdapter.addSuccess(s);
    }

    @Override
    public void closeDB() {
        dbAdapter.closeDB();
    }

    @Override
    public void openDB() {
        dbAdapter.openDB();
    }

    @Override
    public ArrayList<SuccessImage> getSuccessImages(Long id) {
        return dbAdapter.getSuccessImages(id);
    }

    @Override
    public void editSuccess(Success editSuccess) {
        dbAdapter.editSuccess(editSuccess);
    }

    @Override
    public void editSuccessImages(ArrayList<SuccessImage> successImageList, Long successId) {
        dbAdapter.editSuccessImages(successImageList, successId);
    }

    @Override
    public void saveSuccesses(ArrayList<Success> defaultSuccesses) {
        for (Success s : defaultSuccesses)
            dbAdapter.addSuccess(s);
    }

    @Override
    public void clearDatabase() {
        dbAdapter.clear();
    }

}
