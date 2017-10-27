package com.theandroiddev.mywins.UI.Activities;

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
import android.support.v7.app.AppCompatActivity;
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
import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.Storage.DBAdapter;
import com.theandroiddev.mywins.UI.Adapters.CustomImagePickerAdapter;
import com.theandroiddev.mywins.UI.Adapters.SuccessImageAdapter;
import com.theandroiddev.mywins.UI.Helpers.DateHelper;
import com.theandroiddev.mywins.UI.Helpers.DrawableSelector;
import com.theandroiddev.mywins.UI.Models.Success;
import com.theandroiddev.mywins.UI.Models.SuccessImage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.theandroiddev.mywins.UI.Helpers.Constants.CLICK_LONG;
import static com.theandroiddev.mywins.UI.Helpers.Constants.CLICK_SHORT;
import static com.theandroiddev.mywins.UI.Helpers.Constants.DATE_ENDED_EMPTY;
import static com.theandroiddev.mywins.UI.Helpers.Constants.DATE_STARTED_EMPTY;
import static com.theandroiddev.mywins.UI.Helpers.Constants.EXTRA_EDIT_SUCCESS_ITEM;
import static com.theandroiddev.mywins.UI.Helpers.Constants.EXTRA_SHOW_SUCCESS_IMAGES;
import static com.theandroiddev.mywins.UI.Helpers.Constants.EXTRA_SHOW_SUCCESS_ITEM;
import static com.theandroiddev.mywins.UI.Helpers.Constants.REQUEST_CODE_GALLERY;
import static com.theandroiddev.mywins.UI.Helpers.Constants.REQUEST_CODE_IMPORTANCE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.SNACK_IMAGE_REMOVED;
import static com.theandroiddev.mywins.UI.Helpers.Constants.SNACK_UNDO;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummyImportanceDefault;

public class EditSuccessActivity extends AppCompatActivity implements SuccessImageAdapter.OnSuccessImageClickListener, View.OnLongClickListener {
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
    private Animation animShow, animHide;
    private ArrayList<com.esafirm.imagepicker.model.Image> images = new ArrayList<>();
    private CameraModule cameraModule;


    private boolean noDistractionMode;
    private int selectedImageNumber = -1;
    private RecyclerView recyclerView;
    private List<SuccessImage> successImages;
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
    private String mCurrentPhotoPath;
    private Uri contentUri;

    private void toRemove(int position) {
        showSnackbar(position);
        successImageAdapter.notifyDataSetChanged();
    }

    private void initAnimation() {
        animShow = AnimationUtils.loadAnimation(this, R.anim.view_show);
        animHide = AnimationUtils.loadAnimation(this, R.anim.view_hide);
    }

    private void showSnackbar(final int position) {
        final SuccessImage successImage = successImages.get(position);


        Snackbar snackbar = Snackbar
                .make(editSuccessLayout, SNACK_IMAGE_REMOVED, Snackbar.LENGTH_LONG);
//        View view = snackbar.getView();
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
//        params.gravity = TOP;
//        view.setLayoutParams(params);
        snackbar.setAction(SNACK_UNDO, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        undoToRemove(successImage, position);
                    }
                });
        snackbar.show();

        sendToRemoveQueue(successImage, position);
    }

    private void sendToRemoveQueue(SuccessImage successImage, int position) {
        successImages.remove(position);
        successImageAdapter.notifyItemRemoved(position);
    }

    private void undoToRemove(SuccessImage successImage, int position) {
        successImages.add(position, successImage);
        successImageAdapter.notifyItemInserted(position);
        recyclerView.scrollToPosition(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_success);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        noDistractionMode = false;
        initAnimation();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit_fab);
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

    private void getSuccessImages(int successId, String searchTerm, String sort) {

        successImages = new ArrayList<>();
        successImages.clear();
        dbAdapter.openDB();
        successImages.addAll(dbAdapter.retrieveSuccessImages(successId));
        dbAdapter.closeDB();
        successImages.add(0, addImageIv());

        successImageAdapter = new SuccessImageAdapter(successImages, this, R.layout.success_image_layout, this);
        recyclerView.setAdapter(successImageAdapter);
        successImageAdapter.notifyDataSetChanged();
    }

    private void saveChanges() {

        String dateStarted;
        String dateEnded;

        Intent returnIntent = new Intent();

        if (dateHelper.validateData(editTitleEt, dateStartedTv, dateEndedTv)) {

            dateStarted = dateHelper.checkBlankDate(dateStartedTv.getText().toString());
            dateEnded = dateHelper.checkBlankDate(dateEndedTv.getText().toString());

            Success editSuccess = new Success(editTitleEt.getText().toString(), editCategoryTv.getText().toString(), (int) editImportanceIv.getTag(), editDescriptionEt.getText().toString(),
                    dateStarted, dateEnded);
            editSuccess.setId((Integer) editTitleEt.getTag());

            saveImages(editSuccess.getId());

            returnIntent.putExtra(EXTRA_EDIT_SUCCESS_ITEM, editSuccess);

            setResult(Activity.RESULT_OK, returnIntent);
            finish();

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

        recyclerView = (RecyclerView) findViewById(R.id.edit_image_recycler_view);
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

        editTitleEt = (EditText) findViewById(R.id.edit_title);
        editCategoryTv = (TextView) findViewById(R.id.edit_category);
        editCategoryIv = (ImageView) findViewById(R.id.edit_category_iv);
        editImportanceIv = (ImageView) findViewById(R.id.edit_importance_iv);
        editDescriptionEt = (EditText) findViewById(R.id.edit_description);
        dateStartedTv = (TextView) findViewById(R.id.edit_date_started);
        dateEndedTv = (TextView) findViewById(R.id.edit_date_ended);

        editCardBasics = (CardView) findViewById(R.id.edit_card_basic);
        editCardImages = (CardView) findViewById(R.id.edit_card_images);
        editSuccessLayout = findViewById(R.id.edit_success_layout);

        editSuccess = getIntent().getParcelableExtra(EXTRA_SHOW_SUCCESS_ITEM);
        successImages = getIntent().getParcelableArrayListExtra(EXTRA_SHOW_SUCCESS_IMAGES);

        editTitleEt.setTag(editSuccess.getId());
        editTitleEt.setText(editSuccess.getTitle());
        editCategoryTv.setText(editSuccess.getCategory());
        editDescriptionEt.setText(editSuccess.getDescription());
        dateStartedTv.setText(editSuccess.getDateStarted());
        checkDate(editSuccess.getDateStarted(), editSuccess.getDateEnded());

        editImportanceIv.setTag(editSuccess.getImportance());

        drawableSelector.selectCategoryImage(editCategoryIv, editSuccess.getCategory(), editCategoryTv);
        drawableSelector.selectImportanceImage(editImportanceIv, editSuccess.getImportance());

        getSuccessImages(editSuccess.getId(), null, "");

        dateStartedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateHelper.setDate(DATE_STARTED_EMPTY, dateStartedTv, dateEndedTv);
            }
        });

        dateEndedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateHelper.setDate(DATE_ENDED_EMPTY, dateStartedTv, dateEndedTv);
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
            dateStartedTv.setText(DATE_STARTED_EMPTY);
        } else {
            dateStartedTv.setText(dateStarted);
        }

        if (dateEnded.equals("")) {
            dateEndedTv.setText(DATE_ENDED_EMPTY);
        } else {
            dateEndedTv.setText(dateEnded);
        }

    }

    private void setImportance() {

        Intent importanceIntent = new Intent(EditSuccessActivity.this, ImportancePopupActivity.class);

        importanceIntent.putExtra("importance", (int) editImportanceIv.getTag());
        startActivityForResult(importanceIntent, REQUEST_CODE_GALLERY);

    }

    private void saveImages(int successId) {
        dbAdapter.openDB();
        dbAdapter.editSuccessImages(successImages, successId);
        dbAdapter.closeDB();
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
            images = (ArrayList<com.esafirm.imagepicker.model.Image>) ImagePicker.getImages(data);
            printImages(images);
            CustomImagePickerAdapter imagePickerAdapter = new CustomImagePickerAdapter(this);
            imagePickerAdapter.removeAllSelectedSingleClick();
            return;
        }

        if (requestCode == RC_CAMERA && resultCode == RESULT_OK) {
            getCameraModule().getImage(this, data, new OnImageReadyListener() {
                @Override
                public void onImageReady(List<com.esafirm.imagepicker.model.Image> resultImages) {
                    images = (ArrayList<com.esafirm.imagepicker.model.Image>) resultImages;
                    printImages(images);
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

    private void printImages(List<com.esafirm.imagepicker.model.Image> images) {
        if (images == null) return;

        if (selectedImageNumber != 0) {
            successImages.get(selectedImageNumber).setImagePath(images.get(0).getPath());


        } else {//selected = 0
            if (imagePicker != null)
                for (com.esafirm.imagepicker.model.Image i : images) {

                    SuccessImage successImage = new SuccessImage(editSuccess.getId());
                    successImage.setImagePath(i.getPath());
                    successImages.add(1, successImage);
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
        imagePicker.limit(10) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .imageFullDirectory(Environment.getExternalStorageDirectory().getPath()) // can be full path
                .origin(images) // original selected images, used in multi mode
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
                openDatePopupMenu(DATE_STARTED_EMPTY);
                break;
            case R.id.edit_date_ended:
                openDatePopupMenu(DATE_ENDED_EMPTY);
                break;
            default:
                break;
        }
        return true;
    }

    private void openDatePopupMenu(final String s) {


        PopupMenu popupMenu = null;
        if (s.equals(DATE_STARTED_EMPTY)) {
            popupMenu = new PopupMenu(EditSuccessActivity.this, dateStartedTv);
        } else if (s.equals(DATE_ENDED_EMPTY)) {
            popupMenu = new PopupMenu(EditSuccessActivity.this, dateEndedTv);
        }

        if (popupMenu != null) {
            popupMenu.getMenuInflater().inflate(R.menu.menu_date, popupMenu.getMenu());
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.remove_date_menu) {


                        if (s.equals(DATE_STARTED_EMPTY)) {
                            dateStartedTv.setText(DATE_STARTED_EMPTY);
                        } else if (s.equals(DATE_ENDED_EMPTY)) {
                            dateEndedTv.setText(DATE_ENDED_EMPTY);
                        }

                    }

                    return true;
                }
            });
        }


    }
}
