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

import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.editsuccess.EditDescriptionActivity;
import com.theandroiddev.mywins.utils.DateHelper;
import com.theandroiddev.mywins.utils.DrawableSelector;

import static com.theandroiddev.mywins.utils.Constants.DATE_ENDED_EMPTY;
import static com.theandroiddev.mywins.utils.Constants.DATE_STARTED_EMPTY;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_DESCRIPTION;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_INSERT_SUCCESS_ITEM;
import static com.theandroiddev.mywins.utils.Constants.REQUEST_CODE_DESCRIPTION;
import static com.theandroiddev.mywins.utils.Constants.dummyImportanceDefault;

public class InsertSuccessActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "asd";
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

        categoryTv = findViewById(R.id.insert_category_tv);
        importanceTv = findViewById(R.id.insert_importance_tv);
        titleEt = findViewById(R.id.instert_title_et);
        dateStartedTv = findViewById(R.id.insert_date_started_tv);
        dateEndedTv = findViewById(R.id.insert_date_ended_tv);
        description_et = findViewById(R.id.insert_description_et);
        categoryIv = findViewById(R.id.insert_category_iv);
        dateStartedIv = findViewById(R.id.insert_date_started_iv);
        dateEndedIv = findViewById(R.id.insert_date_ended_iv);
        descriptionIv = findViewById(R.id.insert_description_iv);
        importance1Iv = findViewById(R.id.insert_importance_iv_1);
        importance2Iv = findViewById(R.id.insert_importance_iv_2);
        importance3Iv = findViewById(R.id.insert_importance_iv_3);
        importance4Iv = findViewById(R.id.insert_importance_iv_4);
        addBtn = findViewById(R.id.insert_add_btn);


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
                dateHelper.setDate(DATE_STARTED_EMPTY, dateStartedTv, dateEndedTv);
                break;
            case R.id.insert_date_ended_iv:
                dateHelper.setDate(DATE_ENDED_EMPTY, dateStartedTv, dateEndedTv);
                break;
            case R.id.insert_date_started_tv:
                dateHelper.setDate(DATE_STARTED_EMPTY, dateStartedTv, dateEndedTv);
                break;
            case R.id.insert_date_ended_tv:
                dateHelper.setDate(DATE_ENDED_EMPTY, dateStartedTv, dateEndedTv);
                break;
            case R.id.insert_description_iv:
                setDesc();
                break;
            case R.id.insert_add_btn:
                insertSuccess();
                break;

            default:
                break;
        }
    }

    private void insertSuccess() {

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

        //s.setId(UUID.randomUUID().toString());
        //Log.e(TAG, "sendData: uuid" + s.getId() );


        returnIntent.putExtra(EXTRA_INSERT_SUCCESS_ITEM, s);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


    private void setDesc() {

        Intent intent = new Intent(InsertSuccessActivity.this, EditDescriptionActivity.class);
        intent.putExtra("description", description_et.getText().toString());
        startActivityForResult(intent, REQUEST_CODE_DESCRIPTION);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DESCRIPTION) {
            if (resultCode == RESULT_OK) {
                String description = data.getExtras().getString(EXTRA_DESCRIPTION);
                description_et.setText(description);

            }
        }
    }
}
