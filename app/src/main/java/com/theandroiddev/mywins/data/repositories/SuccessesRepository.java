package com.theandroiddev.mywins.data.repositories;

import com.theandroiddev.mywins.UI.models.Success;

import java.util.ArrayList;

/**
 * Created by jakub on 28.10.17.
 */

public interface SuccessesRepository {

    void updateForDeleteSuccess(ArrayList<Success> successToRemoveList);

    ArrayList<Success> getSuccesses(String searchTerm, String sortType, boolean isAscending);

    void registerRepository(DatabaseSuccessesRepository databaseSuccessesRepository);

    void unRegisterRepository();

    void removeSuccesses(ArrayList<Success> successToRemoveList);

    Success getSuccess(int id);

    ArrayList<Success> getDefaultSuccesses();

    void addSuccess(Success s);

    void closeDB();

    void openDB();
}
