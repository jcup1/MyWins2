package com.theandroiddev.mywins;

import com.theandroiddev.mywins.UI.Models.Success;

import java.util.ArrayList;

/**
 * Created by jakub on 07.11.17.
 */

public class SuccessListInfo {

    private ArrayList<Success> mSuccessList;
    private ArrayList<Success> mSuccessToRemoveList;

    public SuccessListInfo() {
        mSuccessList = new ArrayList<>();
        mSuccessToRemoveList = new ArrayList<>();
    }

    public void clearAll() {
        mSuccessList.clear();
        mSuccessToRemoveList.clear();
    }

    public ArrayList<Success> getSuccessList() {
        return mSuccessList;
    }

    public void setSuccessList(ArrayList<Success> successList) {
        this.mSuccessList = successList;
    }

    public ArrayList<Success> getSuccessToRemoveList() {
        return mSuccessToRemoveList;
    }

    public void setSuccessToRemoveList(ArrayList<Success> successToRemoveList) {
        this.mSuccessToRemoveList = successToRemoveList;
    }


}
