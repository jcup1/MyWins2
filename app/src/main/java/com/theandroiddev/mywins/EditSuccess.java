package com.theandroiddev.mywins;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditSuccess extends AppCompatActivity {
    private static final String TAG = "EditSuccess";

    private static final int IMPORTANCE_SUCCESS_REQUEST = 4;


    TextView editCategory, editDateStarted, editDateEnded;
    EditText editTitle, editDescription;
    ImageView editCategoryIv, editImportanceIv;
    DrawableSelector drawableSelector;
    private Calendar myCalendar;

    public EditSuccess() {
        this.drawableSelector = new DrawableSelector(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_success);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });

        initFab(fab);
        initViews();
    }

    private void initFab(FloatingActionButton fab) {
        int id = R.drawable.ic_done;
        int color = R.color.white;
        Drawable myDrawable = ResourcesCompat.getDrawable(getResources(), id, null);

        fab.setImageDrawable(myDrawable);
        fab.setColorFilter(ContextCompat.getColor(this, color));

    }

    private void initViews() {

        editTitle = (EditText) findViewById(R.id.edit_title);
        editCategory = (TextView) findViewById(R.id.edit_category);
        editCategoryIv = (ImageView) findViewById(R.id.edit_category_iv);
        editImportanceIv = (ImageView) findViewById(R.id.edit_importance_iv);
        editDescription = (EditText) findViewById(R.id.edit_description);
        editDateStarted = (TextView) findViewById(R.id.edit_date_started);
        editDateEnded = (TextView) findViewById(R.id.edit_date_ended);

        Success editSuccess = getIntent().getParcelableExtra(MainActivity.EXTRA_SHOW_SUCCESS_ITEM);

        editTitle.setTag(editSuccess.getId());
        Log.e(TAG, "initViews: " + editSuccess.getImportance());
        editTitle.setText(editSuccess.getTitle());
        editCategory.setText(editSuccess.getCategory());
        editDescription.setText(editSuccess.getDescription());
        editDateStarted.setText(editSuccess.getDateStarted());
        checkDateEnded(editSuccess.getDateEnded());

        editImportanceIv.setTag(editSuccess.getImportance());

        drawableSelector.selectCategoryImage(editCategoryIv, editSuccess.getCategory(), editCategory);
        drawableSelector.selectImportanceImage(editImportanceIv, editSuccess.getImportance());

        editDateStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate("started");
            }
        });

        editDateEnded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate("ended");
            }
        });

        editImportanceIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImportance();
            }
        });

    }

    private void checkDateEnded(String dateEnded) {

        if (dateEnded.equals("")) {
            editDateEnded.setText("Set End Date");
        } else {
            editDateEnded.setText(dateEnded);
        }

    }

    private void setImportance() {
        //TODO popup importance

        Intent importanceIntent = new Intent(EditSuccess.this, ImportancePopup.class);

        importanceIntent.putExtra("importance", (int) editImportanceIv.getTag());
        Log.e(TAG, "setImportance: " + (int) editImportanceIv.getTag());

        startActivityForResult(importanceIntent, IMPORTANCE_SUCCESS_REQUEST);

    }

    private void saveChanges() {

        if (validateData()) {

            String dateEnded = getConvertedDateEnded(editDateEnded.getText().toString());

            Intent returnIntent = new Intent();

            Success editSuccess = new Success(editTitle.getText().toString(), editCategory.getText().toString(), (int) editImportanceIv.getTag(), editDescription.getText().toString(),
                    editDateStarted.getText().toString(), dateEnded);
            editSuccess.setId((Integer) editTitle.getTag());

            returnIntent.putExtra(MainActivity.EXTRA_EDIT_SUCCESS_ITEM, editSuccess);

            setResult(Activity.RESULT_OK, returnIntent);
            finish();

        }


    }

    private String getConvertedDateEnded(String s) {
        if (s.equals("Set End Date")) {
            return "";
        } else return s;
    }

    private boolean validateData() {

        //TODO EXPORT IT TO CLASS
        int cnt = 0;

        if (TextUtils.isEmpty(editTitle.getText().toString())) {
            editTitle.setError("What's the name of your success?");
            cnt++;

        }
        if (TextUtils.isEmpty(editDateStarted.getText().toString())) {
            editDateStarted.setError("When it started?");
            cnt++;
        }

        return cnt <= 0;

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

        new DatePickerDialog(EditSuccess.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateLabel(String d) {
        String myFormat = "MM-dd-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (d.equals("started")) {
            editDateStarted.setText(sdf.format(myCalendar.getTime()));
        }
        if (d.equals("ended")) {
            editDateEnded.setText(sdf.format(myCalendar.getTime()));

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {

                int importance = data.getIntExtra("importance", 3);
                editImportanceIv.setTag(importance);
                drawableSelector.selectImportanceImage(editImportanceIv, importance);
            }
        }

    }
}
