package com.theandroiddev.mywins.data.repositories;

import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.models.SuccessImage;
import com.theandroiddev.mywins.successes.SearchFilter;

import java.util.ArrayList;

/**
 * Created by jakub on 28.10.17.
 */

public interface SuccessesRepository {

    void updateForDeleteSuccess(ArrayList<Success> successToRemoveList);

    ArrayList<Success> getSuccesses(SearchFilter searchFilter);

    void registerRepository(DatabaseSuccessesRepository databaseSuccessesRepository);

    void unRegisterRepository();

    void removeSuccesses(ArrayList<Success> successToRemoveList);

    Success getSuccess(String id);

    ArrayList<Success> getDefaultSuccesses();

    void addSuccess(Success s);

    void closeDB();

    void openDB();

    ArrayList<SuccessImage> getSuccessImages(String id);
}
