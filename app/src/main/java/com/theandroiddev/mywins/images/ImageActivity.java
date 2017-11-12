package com.theandroiddev.mywins.images;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.theandroiddev.mywins.R;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {
    private static final String TAG = "ImageActivity";

    ViewPager viewPager;
    ImageSwipeAdapter imageSwipeAdapter;
    ArrayList<String> imagePaths;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imagePaths = getIntent().getStringArrayListExtra("imagePaths");
        pos = getIntent().getIntExtra("position", 0);
        viewPager = findViewById(R.id.view_pager);
        imageSwipeAdapter = new ImageSwipeAdapter(this, imagePaths);
        viewPager.setAdapter(imageSwipeAdapter);
        viewPager.setCurrentItem(pos);
        viewPager.setOffscreenPageLimit(2);

    }

}
