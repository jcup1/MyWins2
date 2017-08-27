package com.theandroiddev.mywins;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.theandroiddev.mywins.MainActivity.EXTRA_EDIT_SUCCESS_ITEM;
import static com.theandroiddev.mywins.MainActivity.EXTRA_SHOW_SUCCESS_ITEM;
import static com.theandroiddev.mywins.MainActivity.EXTRA_SUCCESS_ITEM;

public class ShowSuccess extends AppCompatActivity implements SuccessImageAdapter.OnSuccessImageClickListener {
    private static final String TAG = "ShowSuccess";
    private static final int EDIT_SUCCESS_REQUEST = 3;
    final int REQUEST_CODE_GALLERY = 999;

    int id;
    int color;

    TextView showTitle, showCategory, showDescription, showDateStarted, showDateEnded;
    ImageView showCategoryIv, showImportanceIv;
    DrawableSelector drawableSelector;
    ConstraintLayout showConstraintLayout;
    DBAdapter dbAdapter;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP | ItemTouchHelper.DOWN) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            int pos = viewHolder.getAdapterPosition();
            toRemove(pos);

        }
    };
    private int selectedImageNumber = 100;
    private RecyclerView recyclerView;
    private List<SuccessImage> successImages;
    private List<SuccessImage> successImagesToRemove;
    private SuccessImageAdapter successImageAdapter;

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
        initImages();
    }

    private void initImages() {
        SuccessImage successImage = new SuccessImage();
        successImage.setImageDataBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_done));
        successImages.add(successImage);
        successImageAdapter.notifyDataSetChanged();
    }

    private void initRecycler() {

        recyclerView = (RecyclerView) findViewById(R.id.show_image_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void getSuccessImages(int successId, String searchTerm, String sort) {

        dbAdapter = new DBAdapter(this);
        successImages = new ArrayList<>();
        successImagesToRemove = new ArrayList<>();
        successImages.clear();

        //retrieveSuccesses(successId, searchTerm, sort);

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

        Success s = getIntent().getParcelableExtra(EXTRA_SUCCESS_ITEM);

        showTitle.setTag(s.getId());
        showTitle.setText(s.getTitle());
        showCategory.setText(s.getCategory());
        showDescription.setText(s.getDescription());
        showDateStarted.setText(s.getDateStarted());
        showDateEnded.setText(s.getDateEnded());
        showImportanceIv.setTag(s.getImportance());

        drawableSelector.selectCategoryImage(showCategoryIv, s.getCategory(), showCategory);
        drawableSelector.selectImportanceImage(showImportanceIv, s.getImportance());

        //getSuccessImages(s.getId(), null, "");

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

