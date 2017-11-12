package com.theandroiddev.mywins.successslider;

import android.content.Context;
import android.content.Intent;
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
import android.view.View;

import com.theandroiddev.mywins.MyWinsApplication;
import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.UI.activities.EditSuccessActivity;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.repositories.DatabaseSuccessesRepository;
import com.theandroiddev.mywins.successes.SearchFilter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.theandroiddev.mywins.utils.Constants.REQUEST_CODE_INSERT;

/**
 * Created by jakub on 27.10.17.
 */

public class SuccessSliderActivity extends AppCompatActivity implements SuccessSliderContract.View {
    private static final String TAG = "ScreenSlidePagerActivit";
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static int NUM_PAGES;
    @Inject
    public SuccessSliderContract.Presenter presenter;
    public SuccessSliderContract.SuccessImageLoader loader;
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
    private int position;


    @Override
    protected void onResume() {
        super.onResume();
        presenter.openDB();

    }

    @Override
    protected void onDestroy() {
        presenter.closeDB();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        ((MyWinsApplication) getApplication()).getAppComponent().inject(this);


        FloatingActionButton fab = findViewById(R.id.show_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSuccess();
            }
        });

        initFab(fab);

        presenter.setRepository(new DatabaseSuccessesRepository(this));
        presenter.setView(this);
        Bundle extras = getIntent().getExtras();
        SearchFilter searchFilter = extras.getParcelable("searchfilter");
        position = extras.getInt("position");
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.pager);
        presenter.loadSuccesses(searchFilter);

    }

    private void initFab(FloatingActionButton fab) {

        id = R.drawable.ic_create_black_24dp;
        color = R.color.white;
        Drawable myDrawable = ResourcesCompat.getDrawable(getResources(), id, null);

        fab.setImageDrawable(myDrawable);
        fab.setColorFilter(ContextCompat.getColor(this, color));

    }

    private void editSuccess() {

        presenter.startEditSuccess(mPager.getCurrentItem());

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {


        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    @Override
    public void displaySuccesses(ArrayList<Success> successes) {

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), successes);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(position);
    }

    @Override
    public void displayEditSuccessActivity(String id) {
        Intent editSuccessIntent = new Intent(SuccessSliderActivity.this, EditSuccessActivity.class);

        editSuccessIntent.putExtra("id", id);

        startActivityForResult(editSuccessIntent, REQUEST_CODE_INSERT);

    }

    /**
     * A simple pager adapter that represents 5 SuccessSliderFragment objects, in
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
            Bundle bundle = new Bundle();
            bundle.putString("id", successes.get(position).getId());
            SuccessSliderFragment frag = new SuccessSliderFragment();
            frag.setArguments(bundle);
            return frag;
        }

        @Override
        public int getCount() {
            return successes.size();
        }
    }
}
