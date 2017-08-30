package com.theandroiddev.mywins;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.theandroiddev.mywins.MainActivity.EXTRA_EDIT_SUCCESS_ITEM;
import static com.theandroiddev.mywins.MainActivity.EXTRA_SHOW_SUCCESS_IMAGES;
import static com.theandroiddev.mywins.MainActivity.EXTRA_SHOW_SUCCESS_ITEM;
import static com.theandroiddev.mywins.MainActivity.EXTRA_SUCCESS_ITEM;

public class ShowSuccess extends AppCompatActivity implements SuccessImageAdapter.OnSuccessImageClickListener {
    private static final String TAG = "ShowSuccess";
    private static final int EDIT_SUCCESS_REQUEST = 3;
    private final int REQUEST_CODE_FROM_GALLERY = 01;
    private final int REQUEST_CODE_CLICK_IMAGE = 02;
    int id;
    int color;
    Success s;
    TextView showTitle, showCategory, showDescription, showDateStarted, showDateEnded;
    ImageView showCategoryIv, showImportanceIv;
    DrawableSelector drawableSelector;
    ConstraintLayout showConstraintLayout;
    DBAdapter dbAdapter;
    ImageLoadingUtils imageLoadingUtils;
    private Cursor cursor;
    private LruCache<String, Bitmap> memoryCache;
    private int selectedImageNumber = 100;
    private RecyclerView recyclerView;
    private ArrayList<SuccessImage> successImages;
    private ArrayList<SuccessImage> successImagesToRemove;
    private SuccessImageAdapter successImageAdapter;
    private boolean isImageFitToScreen;

    public ShowSuccess() {
        this.drawableSelector = new DrawableSelector(this);
    }

    private void toRemove(int position) {
        //showSnackbar(position);

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
        initRecycler();
        initViews();
    }

    private void initImages(int successId, String searchTerm, String sortTerm) {
        getSuccessImages(successId, searchTerm, sortTerm);
        //SuccessImage successImage = new SuccessImage();
//        successImage.setImageDataBitmap(BitmapFactory.decodeResource(getResources(),
//                R.drawable.ic_done));
//        successImages.add(successImage);
    }

    private void initRecycler() {

        recyclerView = (RecyclerView) findViewById(R.id.show_image_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

    }

    private void getSuccessImages(int successId, String searchTerm, String sort) {

        dbAdapter = new DBAdapter(this);
        imageLoadingUtils = new ImageLoadingUtils(this);
        int cachesize = 60 * 1024 * 1024;
        memoryCache = new LruCache<String, Bitmap>(cachesize) {
            @SuppressLint("NewApi")
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };


        successImages = new ArrayList<>();
        successImagesToRemove = new ArrayList<>();
        successImages.clear();
        dbAdapter.openDB();
        Log.d(TAG, "getSuccessImages: " + dbAdapter.retrieveSuccessImages(successId));
        successImages.addAll(dbAdapter.retrieveSuccessImages(successId));
        //retrieveSuccesses(successId, searchTerm, sort);
        dbAdapter.closeDB();
        successImageAdapter = new SuccessImageAdapter(successImages, this, R.layout.success_image_layout, getApplicationContext(), drawableSelector);
        recyclerView.setAdapter(successImageAdapter);
        successImageAdapter.notifyDataSetChanged();
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
                (int) showImportanceIv.getTag(), showDescription.getText().toString(), showDateStarted.getText().toString(), showDateEnded.getText().toString());
        showSuccess.setId((Integer) showTitle.getTag());

        editSuccessIntent.putExtra(EXTRA_SHOW_SUCCESS_ITEM, showSuccess);
        editSuccessIntent.putParcelableArrayListExtra(EXTRA_SHOW_SUCCESS_IMAGES, successImages);

        startActivityForResult(editSuccessIntent, EDIT_SUCCESS_REQUEST);
    }

    private void initViews() {

        showConstraintLayout = (ConstraintLayout) findViewById(R.id.show_constraint_layout);
        showTitle = (TextView) findViewById(R.id.show_title);
        showCategory = (TextView) findViewById(R.id.show_category);
        showCategoryIv = (ImageView) findViewById(R.id.show_category_iv);
        showImportanceIv = (ImageView) findViewById(R.id.show_importance_iv);
        showDescription = (TextView) findViewById(R.id.show_description);
        showDateStarted = (TextView) findViewById(R.id.show_date_started);
        showDateEnded = (TextView) findViewById(R.id.show_date_ended);

        s = getIntent().getParcelableExtra(EXTRA_SUCCESS_ITEM);

        showTitle.setTag(s.getId());
        showTitle.setText(s.getTitle());
        showCategory.setText(s.getCategory());
        showDescription.setText(s.getDescription());
        showDateStarted.setText(s.getDateStarted());
        showDateEnded.setText(s.getDateEnded());
        showImportanceIv.setTag(s.getImportance());

        drawableSelector.selectCategoryImage(showCategoryIv, s.getCategory(), showCategory);
        drawableSelector.selectImportanceImage(showImportanceIv, s.getImportance());

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        showDescription.startAnimation(fadeIn);
        fadeIn.setDuration(375);
        fadeIn.setFillAfter(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initImages(s.getId(), "", "");
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
        dbAdapter.editSuccess(showSuccess);
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
                    showDateStarted.setText(s.getDateStarted());
                    showDateEnded.setText(s.getDateEnded());
                    showDescription.setText(s.getDescription());
                    showImportanceIv.setTag(s.getImportance());
                    drawableSelector.selectCategoryImage(showCategoryIv, s.getCategory(), showCategory);
                    drawableSelector.selectImportanceImage(showImportanceIv, s.getImportance());

                }

//                retrieveSuccesses(successId, searchTerm, sort);


                Snackbar.make(showConstraintLayout, "Saved", Snackbar.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onSuccessImageClick(SuccessImage successImage, ImageView successImageIv, int position, ConstraintLayout constraintLayout, CardView cardView) {
        //TODO HANDLE
        Intent intent = new Intent(this, ImageActivity.class);
        ArrayList<String> imagePaths = new ArrayList<>();

        for (int i = 0; i < successImages.size(); i++) {
            imagePaths.add(successImages.get(i).getImagePath());
        }
        Log.e(TAG, "onSuccessImageClick: " + imagePaths);
        intent.putStringArrayListExtra("imagePaths", imagePaths);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onSuccessImageLongClick(SuccessImage successImage, ImageView successImageIv, int position, ConstraintLayout constraintLayout, CardView cardView) {
        //TODO HANDLE
    }
//    private void retrieveSuccesses(int successId, String searchTerm, String sort) {
//        dbAdapter.openDB();
//        successImages.addAll(dbAdapter.retrieveSuccessImages(successId, searchTerm, sort));
//        dbAdapter.closeDB();
//
//    }
}

