package com.example.jakubchmiel.mywins;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ImportancePopup extends AppCompatActivity implements View.OnClickListener {

    TextView popupImportance;
    Button popupDone;
    ImageView importanceIv1, importanceIv2, importanceIv3;
    private DisplayMetrics displayMetrics;
    private DrawableSelector drawableSelector;
    private String dummyImportance = "BIG";

    public ImportancePopup() {
        this.drawableSelector = new DrawableSelector(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_importance);

        initWindow();
        initViews();
    }

    private void initViews() {
        popupImportance = (TextView) findViewById(R.id.popup_importance_tv);
        popupDone = (Button) findViewById(R.id.popup_add_btn);
        importanceIv1 = (ImageView) findViewById(R.id.popup_importance_iv_1);
        importanceIv2 = (ImageView) findViewById(R.id.popup_importance_iv_2);
        importanceIv3 = (ImageView) findViewById(R.id.popup_importance_iv_3);

        importanceIv1.setOnClickListener(this);
        importanceIv2.setOnClickListener(this);
        importanceIv3.setOnClickListener(this);
        popupDone.setOnClickListener(this);


        String importance = getIntent().getExtras().getString("importance", "big");
        popupImportance.setText(importance);


        drawableSelector.setImportance(dummyImportance, popupImportance, importanceIv1, importanceIv2, importanceIv3);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupDone.setBackgroundTintList(getResources().getColorStateList(R.color.accent, null));
        } else {
            popupDone.setBackgroundColor(ContextCompat.getColor(this, R.color.accent));
        }


    }

    private void saveImportance() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("importance", popupImportance.getText().toString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void initWindow() {

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .6));

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.popup_importance_iv_1: {

                if (popupImportance.getText().toString().equals("Medium")) {
                    drawableSelector.setSmallImportance(popupImportance, importanceIv1, importanceIv2, importanceIv3);
                    break;
                } else {
                    drawableSelector.setMediumImportance(popupImportance, importanceIv1, importanceIv2, importanceIv3);
                    break;
                }
            }
            case R.id.popup_importance_iv_2:
                drawableSelector.setBigImportance(popupImportance, importanceIv1, importanceIv2, importanceIv3);
                break;
            case R.id.popup_importance_iv_3:
                drawableSelector.setHugeImportance(popupImportance, importanceIv1, importanceIv2, importanceIv3);
                break;
            case R.id.popup_add_btn:
                saveImportance();
                break;

            default:
                break;
        }

    }
}
