package com.theandroiddev.mywins.successslider;

import com.theandroiddev.mywins.BasePresenter;
import com.theandroiddev.mywins.BaseView;
import com.theandroiddev.mywins.data.models.SearchFilter;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.models.SuccessImage;
import com.theandroiddev.mywins.data.repositories.SuccessesRepository;

import java.util.ArrayList;

/**
 * Created by jakub on 12.11.17.
 */

public interface SuccessSliderContract {

    /**
     * Created by jakub on 12.11.17.
     */

    interface View extends BaseView<Presenter> {

        void displaySuccesses(ArrayList<Success> successes);

        void displayEditSuccessActivity(String id);
    }

    /**
     * Created by jakub on 11.11.17.
     */

    interface Presenter extends BasePresenter<View> {

        void setRepository(SuccessesRepository repository);

        void loadSuccesses(SearchFilter searchFilter);

        void openDB();

        void closeDB();

        void startEditSuccess(int currentItem);
    }


    /**
     * Created by jakub on 12.11.17.
     */

    interface SuccessImageLoader {

        ArrayList<SuccessImage> getSuccessImages(String id);

        void setRepository(SuccessesRepository successesRepository);

        Success getSuccess(String id);
    }
}
