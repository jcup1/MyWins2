package com.theandroiddev.mywins;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.theandroiddev.mywins.Constants.DATE_ENDED;
import static com.theandroiddev.mywins.Constants.DATE_STARTED;
import static com.theandroiddev.mywins.Constants.EXTRA_INSERT_SUCCESS_ITEM;
import static com.theandroiddev.mywins.Constants.SNACK_DESCRIPTION_TODO;
import static com.theandroiddev.mywins.Constants.dummyImportanceDefault;

public class InsertSuccess extends AppCompatActivity implements View.OnClickListener {


    TextView categoryTv, importanceTv, dateStartedTv, dateEndedTv;
    EditText titleEt, description_et;
    ImageView categoryIv, dateStartedIv, dateEndedIv, descriptionIv, importance1Iv, importance2Iv, importance3Iv, importance4Iv;
    Button addBtn;
    DrawableSelector drawableSelector;
    DateHelper dateHelper;
    private DisplayMetrics displayMetrics;
    private int accentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_success);

        initWindow();
        initViews();
        setFields();

    }

    private void setFields() {

        String category = getIntent().getExtras().getString("categoryName", "other");
        categoryTv.setText(category);

        drawableSelector.selectCategoryImage(categoryIv, category, categoryTv);
        drawableSelector.setImportance(dummyImportanceDefault, importanceTv, importance1Iv, importance2Iv, importance3Iv, importance4Iv);

    }

    private void initViews() {

        drawableSelector = new DrawableSelector(this);
        dateHelper = new DateHelper(this);

        categoryTv = (TextView) findViewById(R.id.insert_category_tv);
        importanceTv = (TextView) findViewById(R.id.insert_importance_tv);
        titleEt = (EditText) findViewById(R.id.instert_title_et);
        dateStartedTv = (TextView) findViewById(R.id.insert_date_started_tv);
        dateEndedTv = (TextView) findViewById(R.id.insert_date_ended_tv);
        description_et = (EditText) findViewById(R.id.insert_description_et);
        categoryIv = (ImageView) findViewById(R.id.insert_category_iv);
        dateStartedIv = (ImageView) findViewById(R.id.insert_date_started_iv);
        dateEndedIv = (ImageView) findViewById(R.id.insert_date_ended_iv);
        descriptionIv = (ImageView) findViewById(R.id.insert_description_iv);
        importance1Iv = (ImageView) findViewById(R.id.insert_importance_iv_1);
        importance2Iv = (ImageView) findViewById(R.id.insert_importance_iv_2);
        importance3Iv = (ImageView) findViewById(R.id.insert_importance_iv_3);
        importance4Iv = (ImageView) findViewById(R.id.insert_importance_iv_4);
        addBtn = (Button) findViewById(R.id.insert_add_btn);


        accentColor = ResourcesCompat.getColor(getResources(), R.color.accent, null);

        dateStartedIv.setColorFilter(accentColor);
        descriptionIv.setColorFilter(accentColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addBtn.setBackgroundTintList(getResources().getColorStateList(R.color.accent, null));
        } else {
            addBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.accent));
        }

        dateEndedIv.setColorFilter(accentColor);
        descriptionIv.setColorFilter(accentColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addBtn.setBackgroundTintList(getResources().getColorStateList(R.color.accent, null));
        } else {
            addBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.accent));
        }

        importance1Iv.setOnClickListener(this);
        importance2Iv.setOnClickListener(this);
        importance3Iv.setOnClickListener(this);
        importance4Iv.setOnClickListener(this);
        dateStartedTv.setOnClickListener(this);
        dateEndedTv.setOnClickListener(this);
        dateStartedIv.setOnClickListener(this);
        dateEndedIv.setOnClickListener(this);
        descriptionIv.setOnClickListener(this);
        addBtn.setOnClickListener(this);

    }

    private void initWindow() {

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .8));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.insert_importance_iv_1:
                drawableSelector.setSmallImportance(importanceTv, importance1Iv, importance2Iv, importance3Iv, importance4Iv);
                break;
            case R.id.insert_importance_iv_2:
                drawableSelector.setMediumImportance(importanceTv, importance1Iv, importance2Iv, importance3Iv, importance4Iv);
                break;
            case R.id.insert_importance_iv_3:
                drawableSelector.setBigImportance(importanceTv, importance1Iv, importance2Iv, importance3Iv, importance4Iv);
                break;
            case R.id.insert_importance_iv_4:
                drawableSelector.setHugeImportance(importanceTv, importance1Iv, importance2Iv, importance3Iv, importance4Iv);
                break;
            case R.id.insert_date_started_iv:
                dateHelper.setDate(DATE_STARTED, dateStartedTv, dateEndedTv);
                break;
            case R.id.insert_date_ended_iv:
                dateHelper.setDate(DATE_ENDED, dateStartedTv, dateEndedTv);
                break;
            case R.id.insert_date_started_tv:
                dateHelper.setDate(DATE_STARTED, dateStartedTv, dateEndedTv);
                break;
            case R.id.insert_date_ended_tv:
                dateHelper.setDate(DATE_ENDED, dateStartedTv, dateEndedTv);
                break;
            case R.id.insert_description_iv:
                setDesc();
                break;
            case R.id.insert_add_btn:
                addSuccess();
                break;

            default:
                break;
        }
    }

    private void addSuccess() {

        if (dateHelper.validateData(titleEt, dateStartedTv, dateEndedTv)) {

            String dateStarted = dateHelper.checkBlankDate(dateStartedTv.getText().toString());
            String dateEnded = dateHelper.checkBlankDate(dateEndedTv.getText().toString());

            sendData(dateStarted, dateEnded);
        }
    }

    private void sendData(String dateStarted, String dateEnded) {

        Intent returnIntent = new Intent();

        Success s = new Success(titleEt.getText().toString(), categoryTv.getText().toString(),
                drawableSelector.getImportance(importanceTv.getText().toString()), description_et.getText().toString(), dateStarted, dateEnded);


        returnIntent.putExtra(EXTRA_INSERT_SUCCESS_ITEM, s);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


    private void setDesc() {
        Toast.makeText(this, SNACK_DESCRIPTION_TODO, Toast.LENGTH_SHORT).show();
    }

}
