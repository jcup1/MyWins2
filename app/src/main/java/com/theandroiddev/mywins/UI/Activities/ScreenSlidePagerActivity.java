package com.theandroiddev.mywins.UI.Activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.UI.Fragments.ScreenSlidePageFragment;
import com.theandroiddev.mywins.UI.Models.Success;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakub on 27.10.17.
 */

public class ScreenSlidePagerActivity extends AppCompatActivity {
    private static final String TAG = "ScreenSlidePagerActivit";

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static int NUM_PAGES;
    int id;
    int color;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.show_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //editSuccess();
            }
        });

        initFab(fab);

        Bundle extras = getIntent().getExtras();

        ArrayList<Success> successes = extras.getParcelableArrayList("SUCCESSES");
        Log.d(TAG, "onCreate: " + successes);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), successes);
        mPager.setAdapter(mPagerAdapter);
    }

    private void initFab(FloatingActionButton fab) {

        id = R.drawable.ic_create_black_24dp;
        color = R.color.white;
        Drawable myDrawable = ResourcesCompat.getDrawable(getResources(), id, null);

        fab.setImageDrawable(myDrawable);
        fab.setColorFilter(ContextCompat.getColor(this, color));

    }

    private void editSuccess() {

//        Intent editSuccessIntent = new Intent(ShowSuccessActivity.this, EditSuccessActivity.class);
//
//        Success showSuccess = new Success(showTitle.getText().toString(), showCategory.getText().toString(),
//                (int) showImportanceIv.getTag(), showDescription.getText().toString(), showDateStarted.getText().toString(), showDateEnded.getText().toString());
//        showSuccess.setId((Integer) showTitle.getTag());
//
//        editSuccessIntent.putExtra(EXTRA_SHOW_SUCCESS_ITEM, showSuccess);
//        editSuccessIntent.putParcelableArrayListExtra(EXTRA_SHOW_SUCCESS_IMAGES, successImages);
//
//        startActivityForResult(editSuccessIntent, REQUEST_CODE_INSERT);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {


        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    static class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        List<Success> successes;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<Success> successes) {
            super(fm);
            this.successes = successes;

        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment();
        }

        @Override
        public int getCount() {
            return successes.size();
        }
    }
}
