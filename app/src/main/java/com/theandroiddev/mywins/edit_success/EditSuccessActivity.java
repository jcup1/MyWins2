package com.theandroiddev.mywins.edit_success;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.camera.CameraModule;
import com.esafirm.imagepicker.features.camera.ImmediateCameraModule;
import com.esafirm.imagepicker.features.camera.OnImageReadyListener;
import com.theandroiddev.mywins.ImportancePopupActivity;
import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.data.db.DBAdapter;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.models.SuccessImage;
import com.theandroiddev.mywins.images.CustomImagePickerAdapter;
import com.theandroiddev.mywins.images.SuccessImageAdapter;
import com.theandroiddev.mywins.mvp.MvpDaggerAppCompatActivity;
import com.theandroiddev.mywins.utils.DateHelper;
import com.theandroiddev.mywins.utils.DrawableSelector;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.theandroiddev.mywins.utils.Constants.CLICK_LONG;
import static com.theandroiddev.mywins.utils.Constants.CLICK_SHORT;
import static com.theandroiddev.mywins.utils.Constants.REQUEST_CODE_IMPORTANCE;
import static com.theandroiddev.mywins.utils.Constants.dummyImportanceDefault;

public class EditSuccessActivity extends MvpDaggerAppCompatActivity<EditSuccessView, EditSuccessPresenter> implements SuccessImageAdapter.OnSuccessImageClickListener, View.OnLongClickListener, EditSuccessView {
    private static final String TAG = "EditSuccessActivity";
    private static final int RC_CODE_PICKER = 2000;
    private static final int RC_CAMERA = 3000;
    Success editSuccess;
    TextView editCategoryTv, dateStartedTv, dateEndedTv;
    EditText editTitleEt, editDescriptionEt;
    ImageView editCategoryIv, editImportanceIv;
    CardView editCardBasics, editCardImages, editCardDescription;
    ConstraintLayout editSuccessLayout;
    DrawableSelector drawableSelector;
    DateHelper dateHelper;
    ImagePicker imagePicker;
    String id;

    private Animation animShow, animHide;
    private ArrayList<com.esafirm.imagepicker.model.Image> imageList = new ArrayList<>();
    private CameraModule cameraModule;
    private boolean noDistractionMode;
    private int selectedImageNumber = -1;
    private RecyclerView recyclerView;
    private ArrayList<SuccessImage> successImageList;
    private SuccessImageAdapter successImageAdapter;
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
    private DBAdapter dbAdapter;
    private Uri mImageUri;


    private void toRemove(int position) {
        showSnackbar(position);
        successImageAdapter.notifyDataSetChanged();
    }

    private void initAnimation() {
        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
    }

    private void showSnackbar(final int position) {
        final SuccessImage successImage = successImageList.get(position);


        Snackbar snackbar = Snackbar
                .make(editSuccessLayout, getString(R.string.snack_image_removed), Snackbar.LENGTH_LONG);

        snackbar.setAction(getString(R.string.snack_undo), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoToRemove(successImage, position);
            }
        });
        snackbar.show();

        sendToRemoveQueue(successImage, position);
    }

    private void sendToRemoveQueue(SuccessImage successImage, int position) {
        successImageList.remove(position);
        successImageAdapter.notifyItemRemoved(position);
    }

    private void undoToRemove(SuccessImage successImage, int position) {
        successImageList.add(position, successImage);
        successImageAdapter.notifyItemInserted(position);
        recyclerView.scrollToPosition(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_success);
        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);

        //TODO maybe refactor to editMode
        noDistractionMode = false;
        initAnimation();

        FloatingActionButton fab = findViewById(R.id.edit_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noDistractionMode) {
                    editDescriptionEt.clearFocus();

                } else {
                    saveChanges();
                }
            }
        });

        initFab(fab);
        initRecycler();
        initViews();

    }

    @Override
    public void onBackPressed() {
        if (editDescriptionEt.hasFocus()) {
            editDescriptionEt.clearFocus();
        } else {
            super.onBackPressed();

        }
    }

    private void getSuccessImages(String successId) {

        successImageList = new ArrayList<>();
        successImageList.clear();
        successImageList.addAll(dbAdapter.getSuccessImages(successId));
        successImageList.add(0, addImageIv());

        successImageAdapter = new SuccessImageAdapter(successImageList, this, R.layout.success_image_layout, this);
        recyclerView.setAdapter(successImageAdapter);
        successImageAdapter.notifyDataSetChanged();
    }

    private void saveChanges() {

        String dateStarted;
        String dateEnded;


        if (dateHelper.validateData(editTitleEt, dateStartedTv, dateEndedTv)) {

            dateStarted = dateHelper.checkBlankDate(dateStartedTv.getText().toString());
            dateEnded = dateHelper.checkBlankDate(dateEndedTv.getText().toString());

            editSuccess = new Success(editTitleEt.getText().toString(), editCategoryTv.getText().toString(), (int) editImportanceIv.getTag(), editDescriptionEt.getText().toString(),
                    dateStarted, dateEnded);
            editSuccess.setId(id);

            presenter.editSuccess(editSuccess, successImageList);

        }

    }

    private SuccessImage addImageIv() {

        return new SuccessImage(editSuccess.getId());
    }

    private void initFab(FloatingActionButton fab) {
        int id = R.drawable.ic_done;
        int color = R.color.white;
        Drawable myDrawable = ResourcesCompat.getDrawable(getResources(), id, null);

        fab.setImageDrawable(myDrawable);
        fab.setColorFilter(ContextCompat.getColor(this, color));

    }

    private void initRecycler() {

        recyclerView = findViewById(R.id.edit_image_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void initViews() {

        this.drawableSelector = new DrawableSelector(this);
        this.dateHelper = new DateHelper(this);
        this.dbAdapter = new DBAdapter(this);

        editTitleEt = findViewById(R.id.edit_title);
        editCategoryTv = findViewById(R.id.edit_category);
        editCategoryIv = findViewById(R.id.edit_category_iv);
        editImportanceIv = findViewById(R.id.edit_importance_iv);
        editDescriptionEt = findViewById(R.id.edit_description);
        dateStartedTv = findViewById(R.id.edit_date_started);
        dateEndedTv = findViewById(R.id.edit_date_ended);

        editCardBasics = findViewById(R.id.edit_card_basic);
        editCardImages = findViewById(R.id.edit_card_images);
        editSuccessLayout = findViewById(R.id.edit_success_layout);

        id = getIntent().getStringExtra("id");

        editSuccess = presenter.getSuccess(id);
        successImageList = presenter.getSuccessImages(id);

        editTitleEt.setTag(editSuccess.getId());
        editTitleEt.setText(editSuccess.getTitle());
        editCategoryTv.setText(editSuccess.getCategory());
        editDescriptionEt.setText(editSuccess.getDescription());
        dateStartedTv.setText(editSuccess.getDateStarted());
        checkDate(editSuccess.getDateStarted(), editSuccess.getDateEnded());

        editImportanceIv.setTag(editSuccess.getImportance());

        drawableSelector.selectCategoryImage(editCategoryIv, editSuccess.getCategory(), editCategoryTv);
        drawableSelector.selectImportanceImage(editImportanceIv, editSuccess.getImportance());

        //presenter.loadSuccessImages(editSuccess.getId());
        getSuccessImages(editSuccess.getId());

        dateStartedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateHelper.setDate(getString(R.string.date_started_empty), dateStartedTv, dateEndedTv);
            }
        });

        dateEndedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateHelper.setDate(getString(R.string.date_ended_empty), dateStartedTv, dateEndedTv);
            }
        });

        editDescriptionEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {

                    editCardBasics.startAnimation(animHide);
                    editCardBasics.setVisibility(View.GONE);
                    editCardImages.startAnimation(animHide);
                    editCardImages.setVisibility(View.GONE);
                    noDistractionMode = true;

                } else {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    editCardImages.setVisibility(View.VISIBLE);
                    editCardImages.startAnimation(animShow);
                    editCardBasics.setVisibility(View.VISIBLE);
                    editCardBasics.startAnimation(animShow);

                    noDistractionMode = false;
                }
            }
        });

        editImportanceIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImportance();
            }
        });

        dateStartedTv.setOnLongClickListener(this);
        dateEndedTv.setOnLongClickListener(this);

    }


    private void checkDate(String dateStarted, String dateEnded) {

        if (dateStarted.equals("")) {
            dateStartedTv.setText(getString(R.string.date_started_empty));
        } else {
            dateStartedTv.setText(dateStarted);
        }

        if (dateEnded.equals("")) {
            dateEndedTv.setText(getString(R.string.date_ended_empty));
        } else {
            dateEndedTv.setText(dateEnded);
        }

    }

    private void setImportance() {

        Intent importanceIntent = new Intent(EditSuccessActivity.this, ImportancePopupActivity.class);

        importanceIntent.putExtra("importance", (int) editImportanceIv.getTag());
        startActivityForResult(importanceIntent, REQUEST_CODE_IMPORTANCE);

    }

    @Override
    protected void onDestroy() {
        presenter.closeDB();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMPORTANCE) {
            if (resultCode == Activity.RESULT_OK) {

                int importance = data.getIntExtra("importance", dummyImportanceDefault);
                editImportanceIv.setTag(importance);
                drawableSelector.selectImportanceImage(editImportanceIv, importance);
            }
        }

        if (requestCode == RC_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            imageList = (ArrayList<com.esafirm.imagepicker.model.Image>) ImagePicker.getImages(data);
            printImages(imageList);
            CustomImagePickerAdapter imagePickerAdapter = new CustomImagePickerAdapter(this);
            imagePickerAdapter.removeAllSelectedSingleClick();
            return;
        }

        if (requestCode == RC_CAMERA && resultCode == RESULT_OK) {
            getCameraModule().getImage(this, data, new OnImageReadyListener() {
                @Override
                public void onImageReady(List<com.esafirm.imagepicker.model.Image> resultImages) {
                    imageList = (ArrayList<com.esafirm.imagepicker.model.Image>) resultImages;
                    printImages(imageList);
                }
            });
        }

    }

    private void captureImage() {
        startActivityForResult(
                getCameraModule().getCameraIntent(EditSuccessActivity.this), RC_CAMERA);
    }

    private ImmediateCameraModule getCameraModule() {
        if (cameraModule == null) {
            cameraModule = new ImmediateCameraModule();
        }
        return (ImmediateCameraModule) cameraModule;
    }

    private void printImages(ArrayList<com.esafirm.imagepicker.model.Image> images) {
        if (images == null) return;

        if (selectedImageNumber != 0) {
            successImageList.get(selectedImageNumber).setImagePath(images.get(0).getPath());


        } else {//selected = 0
            if (imagePicker != null)
                for (com.esafirm.imagepicker.model.Image i : images) {

                    SuccessImage successImage = new SuccessImage(editSuccess.getId());
                    successImage.setImagePath(i.getPath());
                    successImageList.add(1, successImage);
                }

            successImageAdapter.notifyDataSetChanged();
        }
        successImageAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == RC_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mImageUri != null) {
            outState.putString("cameraImageUri", mImageUri.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("cameraImageUri")) {
            mImageUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
        }
    }

    public void openGallery() {
        imagePicker = ImagePicker.create(this)
                .theme(R.style.ImagePickerTheme)
                .returnAfterFirst(true) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(false) // set folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle("Tap to select")
                .single();
        //TODO make it .multi() without selection bug
        imagePicker.limit(10) // max imageList can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .imageFullDirectory(Environment.getExternalStorageDirectory().getPath()) // can be full path
                .origin(imageList) // original selected imageList, used in multi mode
                .start(RC_CODE_PICKER); // start image picker activity with request code
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void handleImage(int i, String clickLength, CardView cardView) {

        selectedImageNumber = i;

        if (clickLength.equals(CLICK_SHORT)) {
            openGallery();
        }
        if (clickLength.equals(CLICK_LONG) && i != 0) {
            openDeleteMenu(i, cardView);
        }
    }

    private void openDeleteMenu(final int position, CardView cardView) {

        PopupMenu popupMenu;
        popupMenu = new PopupMenu(EditSuccessActivity.this, cardView);

        popupMenu.getMenuInflater().inflate(R.menu.menu_images, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.remove_image_menu) {

                    toRemove(position);

                }

                return true;
            }
        });
    }


    @Override
    public void onSuccessImageClick(SuccessImage successImage, ImageView successImageIv, int position, ConstraintLayout constraintLayout, CardView cardView) {

        handleImage(position, CLICK_SHORT, cardView);

    }

    @Override
    public void onSuccessImageLongClick(SuccessImage successImage, ImageView successImageIv, int position, ConstraintLayout constraintLayout, CardView cardView) {

        handleImage(position, CLICK_LONG, cardView);

    }

    @Override
    public boolean onLongClick(View view) {

        switch (view.getId()) {
            case R.id.edit_date_started:
                openDatePopupMenu(getString(R.string.date_started_empty));
                break;
            case R.id.edit_date_ended:
                openDatePopupMenu(getString(R.string.date_ended_empty));
                break;
            default:
                break;
        }
        return true;
    }

    private void openDatePopupMenu(final String s) {


        PopupMenu popupMenu = null;
        if (s.equals(getString(R.string.date_started_empty))) {
            popupMenu = new PopupMenu(EditSuccessActivity.this, dateStartedTv);
        } else if (s.equals(getString(R.string.date_ended_empty))) {
            popupMenu = new PopupMenu(EditSuccessActivity.this, dateEndedTv);
        }

        if (popupMenu != null) {
            popupMenu.getMenuInflater().inflate(R.menu.menu_date, popupMenu.getMenu());
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.remove_date_menu) {


                        if (s.equals(getString(R.string.date_started_empty))) {
                            dateStartedTv.setText(getString(R.string.date_started_empty));
                        } else if (s.equals(getString(R.string.date_ended_empty))) {
                            dateEndedTv.setText(getString(R.string.date_ended_empty));
                        }

                    }

                    return true;
                }
            });
        }


    }

    @Override
    public void displaySlider() {

        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void displaySuccessImages(ArrayList<SuccessImage> successImageList) {

        successImageAdapter = new SuccessImageAdapter(successImageList, this, R.layout.success_image_layout, this);
        recyclerView.setAdapter(successImageAdapter);
        successImageAdapter.notifyDataSetChanged();
    }
}
