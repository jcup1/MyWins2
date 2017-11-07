package com.theandroiddev.mywins.UI.repositories;

import com.theandroiddev.mywins.UI.Models.Success;

import java.util.ArrayList;

/**
 * Created by jakub on 28.10.17.
 */

public interface SuccessesRepository {

//    ArrayList<Success> getDefaultSuccesses();
//
//    ArrayList<Success> getSuccesses(String searchText, String sortType, boolean isAscending);

    void updateForDeleteSuccess(ArrayList<Success> successToRemoveList);

    ArrayList<Success> getSuccessesWithNewSorting(String searchTerm, String sortType, boolean isAscending);

    void registerRepository(DatabaseSuccessesRepository databaseSuccessesRepository);

    void unRegisterRepository();
}
