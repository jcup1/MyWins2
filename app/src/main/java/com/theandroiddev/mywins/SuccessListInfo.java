package com.theandroiddev.mywins;

import com.theandroiddev.mywins.UI.Models.Success;

import java.util.ArrayList;

/**
 * Created by jakub on 07.11.17.
 */

public class SuccessListInfo {

    private ArrayList<Success> successList;
    private ArrayList<Success> successToRemoveList;

    public SuccessListInfo() {
        successList = new ArrayList<>();
        successToRemoveList = new ArrayList<>();
    }

    public void clearAll() {
        successList.clear();
        successToRemoveList.clear();
    }

    public ArrayList<Success> getSuccessList() {
        return successList;
    }

    public void setSuccessList(ArrayList<Success> successList) {
        this.successList = successList;
    }

    public ArrayList<Success> getSuccessToRemoveList() {
        return successToRemoveList;
    }

    public void setSuccessToRemoveList(ArrayList<Success> successToRemoveList) {
        this.successToRemoveList = successToRemoveList;
    }


}
