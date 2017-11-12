package com.theandroiddev.mywins.editsuccess;

import com.theandroiddev.mywins.BasePresenter;
import com.theandroiddev.mywins.BaseView;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.models.SuccessImage;
import com.theandroiddev.mywins.data.repositories.SuccessesRepository;

import java.util.ArrayList;

/**
 * Created by jakub on 12.11.17.
 */

public class EditSuccessContract {

    interface View extends BaseView<Presenter> {

        void displaySlider();
    }

    public interface Presenter extends BasePresenter<View> {

        void setRepository(SuccessesRepository repository);

        void editSuccess(Success editSuccess, ArrayList<SuccessImage> successImageList);
    }
}
