package com.theandroiddev.mywins;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.theandroiddev.mywins.Constants.CLICK_LONG;
import static com.theandroiddev.mywins.Constants.CLICK_SHORT;
import static com.theandroiddev.mywins.Constants.DATE_ENDED_EMPTY;
import static com.theandroiddev.mywins.Constants.DATE_STARTED_EMPTY;
import static com.theandroiddev.mywins.Constants.EXTRA_EDIT_SUCCESS_ITEM;
import static com.theandroiddev.mywins.Constants.EXTRA_SHOW_SUCCESS_IMAGES;
import static com.theandroiddev.mywins.Constants.EXTRA_SHOW_SUCCESS_ITEM;
import static com.theandroiddev.mywins.Constants.IMPORTANCE_SUCCESS_REQUEST;
import static com.theandroiddev.mywins.Constants.REQUEST_CODE_GALLERY;
import static com.theandroiddev.mywins.Constants.SNACK_IMAGE_REMOVED;
import static com.theandroiddev.mywins.Constants.SNACK_UNDO;
import static com.theandroiddev.mywins.Constants.TOAST_PERMISSION_DENIED;
import static com.theandroiddev.mywins.Constants.dummyImportanceDefault;

public class EditSuccess extends AppCompatActivity implements SuccessImageAdapter.OnSuccessImageClickListener, View.OnLongClickListener {
    private static final String TAG = "EditSuccess";


    Success editSuccess;
    TextView editCategoryTv, dateStartedTv, dateEndedTv;
    EditText editTitleEt, editDescriptionEt;
    ImageView editCategoryIv, editImportanceIv;

    DrawableSelector drawableSelector;
    DateHelper dateHelper;

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

    private void toRemove(int position) {
        showSnackbar(position);
        successImageAdapter.notifyDataSetChanged();


    }

    private void showSnackbar(final int position) {
        final SuccessImage successImage = successImages.get(position);
        Snackbar snackbar = Snackbar
                .make(recyclerView, SNACK_IMAGE_REMOVED, Snackbar.LENGTH_LONG)
                .setAction(SNACK_UNDO, new View.OnClickListener() {
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });

        initFab(fab);
        initRecycler();
        initViews();

    }

    private void getSuccessImages(int successId, String searchTerm, String sort) {

        successImages = new ArrayList<>();
        successImages.clear();
        dbAdapter.openDB();
        successImages.addAll(dbAdapter.retrieveSuccessImages(successId));
        dbAdapter.closeDB();
        successImages.add(0, addImageIv());

        successImageAdapter = new SuccessImageAdapter(successImages, this, R.layout.success_image_layout);
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

        Intent importanceIntent = new Intent(EditSuccess.this, ImportancePopup.class);

        importanceIntent.putExtra("importance", (int) editImportanceIv.getTag());
        startActivityForResult(importanceIntent, IMPORTANCE_SUCCESS_REQUEST);

    }

    private void saveImages(int successId) {
        dbAdapter.openDB();
        dbAdapter.editSuccessImages(successImages, successId);
        dbAdapter.closeDB();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMPORTANCE_SUCCESS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {

                int importance = data.getIntExtra("importance", dummyImportanceDefault);
                editImportanceIv.setTag(importance);
                drawableSelector.selectImportanceImage(editImportanceIv, importance);
            }
        }

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imagePath = cursor.getString(columnIndex);
                cursor.close();

                if (selectedImageNumber != 0) {
                    successImages.get(selectedImageNumber).setImagePath(imagePath);
                } else {//selected = 0
                    SuccessImage successImage = new SuccessImage(editSuccess.getId());
                    successImage.setImagePath(imagePath);
                    successImages.add(successImage);
                }

                successImageAdapter.notifyDataSetChanged();
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);

            } else {
                Toast.makeText(this, TOAST_PERMISSION_DENIED, Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void openGallery() {

        ActivityCompat.requestPermissions(
                EditSuccess.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_GALLERY
        );
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
        popupMenu = new PopupMenu(EditSuccess.this, cardView);

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
            popupMenu = new PopupMenu(EditSuccess.this, dateStartedTv);
        } else if (s.equals(DATE_ENDED_EMPTY)) {
            popupMenu = new PopupMenu(EditSuccess.this, dateEndedTv);
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
