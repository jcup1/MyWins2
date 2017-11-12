package com.theandroiddev.mywins;

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

import com.theandroiddev.mywins.utils.DrawableSelector;

import static com.theandroiddev.mywins.utils.Constants.dummyImportanceDefault;

public class ImportancePopupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ImportancePopupActivity";

    TextView popupImportance;
    Button popupDone;
    ImageView importanceIv1, importanceIv2, importanceIv3, importanceIv4;
    private DisplayMetrics displayMetrics;
    private DrawableSelector drawableSelector;

    public ImportancePopupActivity() {
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
        popupImportance = findViewById(R.id.popup_importance_tv);
        popupDone = findViewById(R.id.popup_add_btn);
        importanceIv1 = findViewById(R.id.popup_importance_iv_1);
        importanceIv2 = findViewById(R.id.popup_importance_iv_2);
        importanceIv3 = findViewById(R.id.popup_importance_iv_3);
        importanceIv4 = findViewById(R.id.popup_importance_iv_4);

        importanceIv1.setOnClickListener(this);
        importanceIv2.setOnClickListener(this);
        importanceIv3.setOnClickListener(this);
        importanceIv4.setOnClickListener(this);
        popupDone.setOnClickListener(this);


        int importance = getIntent().getExtras().getInt("importance", 3);
        popupImportance.setText(String.valueOf(importance));


        drawableSelector.setImportance(dummyImportanceDefault, popupImportance, importanceIv1, importanceIv2, importanceIv3, importanceIv4);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupDone.setBackgroundTintList(getResources().getColorStateList(R.color.accent, null));
        } else {
            popupDone.setBackgroundColor(ContextCompat.getColor(this, R.color.accent));
        }


    }

    private void saveImportance() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("importance", drawableSelector.getImportance(popupImportance.getText().toString()));
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

                drawableSelector.setSmallImportance(popupImportance, importanceIv1, importanceIv2, importanceIv3, importanceIv4);
                break;

            }
            case R.id.popup_importance_iv_2:
                drawableSelector.setMediumImportance(popupImportance, importanceIv1, importanceIv2, importanceIv3, importanceIv4);

                break;
            case R.id.popup_importance_iv_3:
                drawableSelector.setBigImportance(popupImportance, importanceIv1, importanceIv2, importanceIv3, importanceIv4);

                break;
            case R.id.popup_importance_iv_4:
                drawableSelector.setHugeImportance(popupImportance, importanceIv1, importanceIv2, importanceIv3, importanceIv4);
                break;
            case R.id.popup_add_btn:
                saveImportance();
                break;

            default:
                break;
        }

    }
}
