package com.theandroiddev.mywins.edit_success;

import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.models.SuccessImage;
import com.theandroiddev.mywins.data.repositories.SuccessesRepository;
import com.theandroiddev.mywins.mvp.MvpPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by jakub on 12.11.17.
 */

public class EditSuccessPresenter extends MvpPresenter<EditSuccessView> {

    private EditSuccessView view;
    private SuccessesRepository repository;
    private ArrayList<SuccessImage> successImageList;

    @Inject
    public EditSuccessPresenter() {
        successImageList = new ArrayList<>();
    }

    public void setView(EditSuccessView view) {

        this.view = view;
    }

    public void setRepository(SuccessesRepository repository) {
        this.repository = repository;
    }

    public void dropView() {
        view = null;
    }

    public void editSuccess(Success editSuccess, ArrayList<SuccessImage> successImageList) {
        repository.editSuccess(editSuccess);
        repository.editSuccessImages(successImageList, editSuccess.getId());

        view.displaySlider();
    }

    public void loadSuccessImages(String id) {

        successImageList = new ArrayList<>();
        successImageList.clear();
        successImageList.addAll(repository.getSuccessImages(id));
        successImageList.add(0, addImageIv(id));

    }

    public void closeDB() {
        repository.closeDB();
    }

    public void openDB() {
        repository.openDB();
    }

    private SuccessImage addImageIv(String id) {

        return new SuccessImage(id);
    }


}
