package com.theandroiddev.mywins.success_slider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.data.models.SearchFilter;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.repositories.DatabaseSuccessesRepository;
import com.theandroiddev.mywins.edit_success.EditSuccessActivity;
import com.theandroiddev.mywins.mvp.MvpDaggerAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static com.theandroiddev.mywins.utils.Constants.REQUEST_CODE_INSERT;

/**
 * Created by jakub on 27.10.17.
 */

public class SuccessSliderActivity extends MvpDaggerAppCompatActivity<SuccessSliderView, SuccessSliderPresenter> implements SuccessSliderView, ActionHandler {

    private int id;
    private int color;
    private int position;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private ConstraintLayout sliderConstraint;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        presenter.closeDB();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        FloatingActionButton fab = findViewById(R.id.slider_fab);
        sliderConstraint = findViewById(R.id.slider_constraint);
        mPager = findViewById(R.id.slider_pager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSuccess();
            }
        });

        initFab(fab);

        presenter.setRepository(new DatabaseSuccessesRepository(this));
        presenter.setView(this);
        presenter.openDB();

        Bundle extras = getIntent().getExtras();
        SearchFilter searchFilter;
        if (extras != null) {
            searchFilter = extras.getParcelable("searchfilter");
            position = extras.getInt("position");
            presenter.loadSuccesses(searchFilter);
        }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_INSERT) {
            if (resultCode == Activity.RESULT_OK) {

                // Snackbar hides photos
                // makeSnackbar(SNACK_SAVED);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            }
        }

    }


    public void makeSnackbar(String s) {

        Snackbar snackbar = Snackbar.make(sliderConstraint, s, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("position", mPager.getCurrentItem());
        setResult(RESULT_OK, returnIntent);
        finish();

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

    @Override
    public void onAddImage() {
        editSuccess();
    }

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
