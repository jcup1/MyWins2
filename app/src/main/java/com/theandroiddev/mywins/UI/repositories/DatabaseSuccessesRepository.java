package com.theandroiddev.mywins.UI.repositories;

import android.content.Context;

import com.theandroiddev.mywins.UI.Models.Success;
import com.theandroiddev.mywins.local.DBAdapter;

import java.util.ArrayList;

/**
 * Created by jakub on 04.11.17.
 */

public class DatabaseSuccessesRepository implements SuccessesRepository {

    private DBAdapter dbAdapter;
    private SuccessesRepository mSuccessRepository;

    public DatabaseSuccessesRepository(Context context) {
        dbAdapter = new DBAdapter(context);
    }

    @Override
    public void updateForDeleteSuccess(ArrayList<Success> successToRemoveList) {
        dbAdapter.removeSuccess(successToRemoveList);
    }

    @Override
    public ArrayList<Success> getSuccessesWithNewSorting(String searchTerm, String sortType, boolean isAscending) {
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

}
