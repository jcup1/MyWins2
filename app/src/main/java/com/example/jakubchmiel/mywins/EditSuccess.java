package com.example.jakubchmiel.mywins;

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


    TextView editCategory, editDate;
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
        editDate = (TextView) findViewById(R.id.edit_date);

        Success editSuccess = getIntent().getParcelableExtra(MainActivity.EXTRA_SHOW_SUCCESS_ITEM);

        editTitle.setTag(editSuccess.getId());
        editTitle.setText(editSuccess.getTitle());
        editCategory.setText(editSuccess.getCategory());
        editDescription.setText(editSuccess.getDescription());
        editDate.setText(editSuccess.getDate());
        editImportanceIv.setTag(editSuccess.getImportance());

        drawableSelector.selectCategoryImage(editCategoryIv, editSuccess.getCategory(), editCategory);
        drawableSelector.selectImportanceImage(editImportanceIv, editSuccess.getImportance());

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });

        editImportanceIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImportance();
            }
        });

    }

    private void setImportance() {
        //TODO popup importance

        Intent importanceIntent = new Intent(EditSuccess.this, ImportancePopup.class);

        importanceIntent.putExtra("importance", (CharSequence) editImportanceIv.getTag());

        startActivityForResult(importanceIntent, IMPORTANCE_SUCCESS_REQUEST);

    }

    private void saveChanges() {

        if (validateData()) {

            Intent returnIntent = new Intent();

            Success editSuccess = new Success(editTitle.getText().toString(), editCategory.getText().toString(), (String) editImportanceIv.getTag(), editDescription.getText().toString(),
                    editDate.getText().toString());
            editSuccess.setId((Integer) editTitle.getTag());

            returnIntent.putExtra(MainActivity.EXTRA_EDIT_SUCCESS_ITEM, editSuccess);

            setResult(Activity.RESULT_OK, returnIntent);
            finish();

        }


    }

    private boolean validateData() {

        //TODO EXPORT IT TO CLASS
        int cnt = 0;

        if (TextUtils.isEmpty(editTitle.getText().toString())) {
            editTitle.setError("What's the name of your success?");
            cnt++;

        }
        if (TextUtils.isEmpty(editDate.getText().toString())) {
            editDate.setError("When it happened?");
            cnt++;
        }

        return cnt <= 0;

    }

    private void setDate() {

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        new DatePickerDialog(EditSuccess.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateLabel() {
        String myFormat = "MM.dd.yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {

                String importance = data.getStringExtra("importance");
                editImportanceIv.setTag(importance);
                drawableSelector.selectImportanceImage(editImportanceIv, importance);
            }
        }

    }
}
