package com.theandroiddev.mywins.successslider;

import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.models.SuccessImage;
import com.theandroiddev.mywins.data.repositories.SuccessesRepository;

import java.util.ArrayList;

/**
 * Created by jakub on 12.11.17.
 */

public class SuccessImageLoader implements SuccessSliderContract.SuccessImageLoader {
    private SuccessesRepository successesRepository;

    @Override
    public ArrayList<SuccessImage> getSuccessImages(String id) {
        return successesRepository.getSuccessImages(id);
    }

    @Override
    public void setRepository(SuccessesRepository successesRepository) {

        this.successesRepository = successesRepository;
    }

    @Override
    public Success getSuccess(String id) {
        return successesRepository.getSuccess(id);
    }
}
