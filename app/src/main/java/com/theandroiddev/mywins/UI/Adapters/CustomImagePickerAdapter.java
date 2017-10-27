package com.theandroiddev.mywins.UI.Adapters;

import android.content.Context;

import com.esafirm.imagepicker.adapter.ImagePickerAdapter;

/**
 * Created by jakub on 27.10.17.
 */

public class CustomImagePickerAdapter extends ImagePickerAdapter {
    public CustomImagePickerAdapter(Context context) {
        super(context, null, null, null);
    }

    @Override
    public void removeAllSelectedSingleClick() {
        super.removeAllSelectedSingleClick();
    }
}
