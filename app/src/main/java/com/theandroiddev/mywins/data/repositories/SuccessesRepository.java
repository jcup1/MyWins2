package com.theandroiddev.mywins.data.repositories;

import com.theandroiddev.mywins.data.models.SearchFilter;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.models.SuccessImage;

import java.util.ArrayList;

/**
 * Created by jakub on 28.10.17.
 */

public interface SuccessesRepository {

    void updateForDeleteSuccess(ArrayList<Success> successToRemoveList);

    ArrayList<Success> getSuccesses(SearchFilter searchFilter);

    void removeSuccesses(ArrayList<Success> successToRemoveList);

    Success getSuccess(Long id);

    ArrayList<Success> getDefaultSuccesses();

    void addSuccess(Success s);

    void closeDB();

    void openDB();

    ArrayList<SuccessImage> getSuccessImages(Long id);

    void editSuccess(Success editSuccess);

    void editSuccessImages(ArrayList<SuccessImage> successImageList, Long successId);

    void saveSuccesses(ArrayList<Success> defaultSuccesses);

    void clearDatabase();
}
