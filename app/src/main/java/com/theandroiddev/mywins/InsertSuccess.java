package com.theandroiddev.mywins;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InsertSuccess extends AppCompatActivity implements View.OnClickListener {


    TextView categoryTv, importanceTv, dateStartedTv, dateEndedTv;
    EditText titleEt, description_et;
    ImageView categoryIv, dateStartedIv, dateEndedIv, descriptionIv, importance1Iv, importance2Iv, importance3Iv, importance4Iv;
    Button addBtn;
    DrawableSelector drawableSelector;
    private DisplayMetrics displayMetrics;
    private int accentColor;
    private Calendar myCalendar;
    private int dummyImportance = 3;

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
        drawableSelector.setImportance(dummyImportance, importanceTv, importance1Iv, importance2Iv, importance3Iv, importance4Iv);

    }

    private void initViews() {

        drawableSelector = new DrawableSelector(this);

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
                setDate("started");
                break;
            case R.id.insert_date_ended_iv:
                setDate("ended");
                break;
            case R.id.insert_date_started_tv:
                setDate("started");
                break;
            case R.id.insert_date_ended_tv:
                setDate("ended");
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

        if (validateData()) {

            if (!isLegalDate(dateEndedTv.getText().toString())) {
                dateEndedTv.setText("");
            }

            sendData();
        }
    }

    private void sendData() {

        Intent returnIntent = new Intent();

        Success s = new Success(titleEt.getText().toString(), categoryTv.getText().toString(),
                drawableSelector.getImportance(importanceTv.getText().toString()), description_et.getText().toString(), dateStartedTv.getText().toString(), dateEndedTv.getText().toString());


        returnIntent.putExtra(MainActivity.EXTRA_INSERT_SUCCESS_ITEM, s);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private boolean validateData() {

        int cnt = 0;

        if (TextUtils.isEmpty(titleEt.getText().toString())) {
            titleEt.setError("What's the name of your success?");
            cnt++;

        }
        if (!isLegalDate(dateStartedTv.getText().toString())) {
            dateStartedTv.setError("When it started?");
            cnt++;
        }

        if (!TextUtils.isEmpty(dateStartedTv.getText().toString()) && !TextUtils.isEmpty(dateEndedTv.getText().toString())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
            try {
                Date date1 = sdf.parse(dateStartedTv.getText().toString());
                Date date2 = sdf.parse(dateEndedTv.getText().toString());
                if (date1.after(date2)) {
                    dateEndedTv.setError("You ended before started!");
                    cnt++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return cnt <= 0;

    }

    private void setDesc() {
        Toast.makeText(this, "Not active", Toast.LENGTH_SHORT).show();
    }

    private void setDate(final String d) {

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(d);
            }

        };

        new DatePickerDialog(InsertSuccess.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateLabel(String d) {
        String myFormat = "yy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (d.equals("started")) {
            dateStartedTv.setText(sdf.format(myCalendar.getTime()));
            dateStartedTv.setError(null);
        }
        if (d.equals("ended")) {
            dateEndedTv.setText(sdf.format(myCalendar.getTime()));
            dateEndedTv.setError(null);
        }

    }

    boolean isLegalDate(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        sdf.setLenient(false);
        return sdf.parse(s, new ParsePosition(0)) != null;
    }


}
