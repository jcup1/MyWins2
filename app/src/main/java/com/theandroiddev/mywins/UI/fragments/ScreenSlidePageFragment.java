package com.theandroiddev.mywins.UI.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theandroiddev.mywins.R;

/**
 * Created by jakub on 27.10.17.
 */

public class ScreenSlidePageFragment extends Fragment {

    TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_show_success, container, false);

        tv = view.findViewById(R.id.item_title);

        tv.setText("asdasd");

        return view;
    }
}
