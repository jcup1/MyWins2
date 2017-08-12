package com.example.jakubchmiel.mywins;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.jakubchmiel.mywins.MainActivity.EXTRA_EDIT_SUCCESS_ITEM;
import static com.example.jakubchmiel.mywins.MainActivity.EXTRA_SHOW_SUCCESS_ITEM;
import static com.example.jakubchmiel.mywins.MainActivity.EXTRA_SUCCESS_ITEM;

public class ShowSuccess extends AppCompatActivity {
    private static final String TAG = "ShowSuccess";
    private static final int EDIT_SUCCESS_REQUEST = 3;

    int id;
    int color;

    TextView showTitle, showCategory, showDescription, showDate;
    ImageView showCategoryIv, showImportanceIv;
    DrawableSelector drawableSelector;
    ConstraintLayout showConstraintLayout;

    public ShowSuccess() {
        this.drawableSelector = new DrawableSelector(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_success);
        Toolbar toolbar = (Toolbar) findViewById(R.id.show_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.show_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSuccess();
            }
        });
        initFab(fab);

        initViews();
    }

    private void initFab(FloatingActionButton fab) {

        id = R.drawable.ic_create_black_24dp;
        color = R.color.white;
        Drawable myDrawable = ResourcesCompat.getDrawable(getResources(), id, null);

        fab.setImageDrawable(myDrawable);
        fab.setColorFilter(ContextCompat.getColor(this, color));

    }

    private void editSuccess() {

        Intent editSuccessIntent = new Intent(ShowSuccess.this, EditSuccess.class);

        Success showSuccess = new Success(showTitle.getText().toString(), showCategory.getText().toString(),
                (String) showImportanceIv.getTag(), showDescription.getText().toString(), showDate.getText().toString());
        showSuccess.setId((Integer) showTitle.getTag());

        editSuccessIntent.putExtra(EXTRA_SHOW_SUCCESS_ITEM, showSuccess);

        startActivityForResult(editSuccessIntent, EDIT_SUCCESS_REQUEST);
    }

    private void initViews() {

        showConstraintLayout = (ConstraintLayout) findViewById(R.id.show_constraint_layout);
        showTitle = (TextView) findViewById(R.id.show_title);
        showCategory = (TextView) findViewById(R.id.show_category);
        showCategoryIv = (ImageView) findViewById(R.id.show_category_iv);
        showImportanceIv = (ImageView) findViewById(R.id.show_importance_iv);
        showDescription = (TextView) findViewById(R.id.show_description);
        showDate = (TextView) findViewById(R.id.show_date);


        Success s = getIntent().getParcelableExtra(EXTRA_SUCCESS_ITEM);

        showTitle.setTag(s.getId());
        showTitle.setText(s.getTitle());
        showCategory.setText(s.getCategory());
        showDescription.setText(s.getDescription());
        showDate.setText(s.getDate());
        showImportanceIv.setTag(s.getImportance());

        drawableSelector.selectCategoryImage(showCategoryIv, s.getCategory(), showCategory);
        drawableSelector.selectImportanceImage(showImportanceIv, s.getImportance());

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        showDescription.startAnimation(fadeIn);
        fadeIn.setDuration(375);
        fadeIn.setFillAfter(true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        showDescription.startAnimation(fadeOut);
        fadeOut.setDuration(375);
        fadeOut.setFillAfter(true);
    }

    private void editSuccess(Success showSuccess) {

        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();
        dbAdapter.edit(showSuccess);
        dbAdapter.closeDB();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_SUCCESS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                Success s = data.getExtras().getParcelable(EXTRA_EDIT_SUCCESS_ITEM);

                editSuccess(s);

                if (s != null) {
                    showTitle.setText(s.getTitle());
                    showCategory.setText(s.getCategory());
                    showDate.setText(s.getDate());
                    showDescription.setText(s.getDescription());
                    showImportanceIv.setTag(s.getImportance());
                    drawableSelector.selectCategoryImage(showCategoryIv, s.getCategory(), showCategory);
                    drawableSelector.selectImportanceImage(showImportanceIv, s.getImportance());

                }

                Snackbar.make(showConstraintLayout, "Saved", Snackbar.LENGTH_SHORT).show();
            }
        }


    }
}

