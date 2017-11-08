package com.theandroiddev.mywins.data.repositories;

import android.content.Context;

import com.theandroiddev.mywins.UI.models.Success;
import com.theandroiddev.mywins.data.db.DBAdapter;

import java.util.ArrayList;

/**
 * Created by jakub on 04.11.17.
 */

public class DatabaseSuccessesRepository implements SuccessesRepository {
    private static final String TAG = "DatabaseSuccessesReposi";

    private DBAdapter dbAdapter;
    private SuccessesRepository mSuccessRepository;
    private Context context;

    public DatabaseSuccessesRepository(Context context) {
        dbAdapter = new DBAdapter(context);
        this.context = context;
    }

    @Override
    public void updateForDeleteSuccess(ArrayList<Success> successToRemoveList) {
        dbAdapter.removeSuccess(successToRemoveList);
    }

    @Override
    public ArrayList<Success> getSuccesses(String searchTerm, String sortType, boolean isAscending) {
        dbAdapter = new DBAdapter(context);
        return dbAdapter.getSuccesses(searchTerm, sortType, isAscending);
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
    public Success getSuccess(int id) {
        return dbAdapter.getSuccess(id);
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

}
