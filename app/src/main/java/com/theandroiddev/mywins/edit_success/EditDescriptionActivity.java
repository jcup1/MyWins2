package com.theandroiddev.mywins.edit_success;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.theandroiddev.mywins.R;

import static com.theandroiddev.mywins.utils.Constants.EXTRA_DESCRIPTION;

public class EditDescriptionActivity extends AppCompatActivity {

    EditText descriptionEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_description);

        initFab();
        initFields();
        initValues();
    }

    private void initFields() {
        descriptionEt = findViewById(R.id.edit_description_et);
    }

    private void initFab() {

        FloatingActionButton fab = findViewById(R.id.edit_description_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });

        int id = R.drawable.ic_done;
        int color = R.color.white;
        Drawable myDrawable = ResourcesCompat.getDrawable(getResources(), id, null);

        fab.setImageDrawable(myDrawable);
        fab.setColorFilter(ContextCompat.getColor(this, color));
    }

    private void saveChanges() {

        String desc = descriptionEt.getText().toString();
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_DESCRIPTION, desc);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }

    private void initValues() {

        String description = getIntent().getExtras().getString("description", "");
        descriptionEt.setText(description);

    }
}
