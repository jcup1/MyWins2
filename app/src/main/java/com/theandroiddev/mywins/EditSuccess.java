package com.theandroiddev.mywins;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditSuccess extends AppCompatActivity implements SuccessImageAdapter.OnSuccessImageClickListener {
    private static final String TAG = "EditSuccess";

    private static final int IMPORTANCE_SUCCESS_REQUEST = 4;
    private final int REQUEST_CODE_GALLERY = 5;
    DBAdapter dbAdapter;
    Success editSuccess;
    TextView editCategory, editDateStarted, editDateEnded;
    EditText editTitle, editDescription;
    ImageView editCategoryIv, editImportanceIv;
    DrawableSelector drawableSelector;
    private int selectedImageNumber = 100;
    private RecyclerView recyclerView;
    private List<SuccessImage> successImages;
    private List<SuccessImage> successImagesToRemove;
    private List<SuccessImage> successImagesToAdd;
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
    private Calendar myCalendar;
    private boolean[] activeImage;
    private int imagesActive;
    public EditSuccess() {
        this.drawableSelector = new DrawableSelector(this);
    }

    private void toRemove(int position) {
        showSnackbar(position);
        successImageAdapter.notifyDataSetChanged();


    }

    private void showSnackbar(final int position) {
        final SuccessImage successImage = successImages.get(position);
        Snackbar snackbar = Snackbar
                .make(recyclerView, "IMAGE REMOVED", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
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
        successImagesToRemove.add(successImage);
    }

    private void undoToRemove(SuccessImage successImage, int position) {
        successImages.add(position, successImage);
        successImageAdapter.notifyItemInserted(position);
        recyclerView.scrollToPosition(position);
        successImagesToRemove.remove(successImage);
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

    private void initImages() {
//        SuccessImage successImage = new SuccessImage();
//        successImage.setFileName("default");
//        successImage.setImageDataBitmap(BitmapFactory.decodeResource(getResources(),
//                R.drawable.ic_done));
//        successImages.add(success
// Image);

        dbAdapter = new DBAdapter(this);
        successImagesToRemove = new ArrayList<>();
        successImages.clear();

        //retrieveSuccesses(successId, searchTerm, sort);

        successImageAdapter = new SuccessImageAdapter(successImages, this, R.layout.success_image_layout, getApplicationContext(), drawableSelector);
        recyclerView.setAdapter(successImageAdapter);
        successImageAdapter.notifyDataSetChanged();
        successImageAdapter.notifyDataSetChanged();
    }

    private void getSuccessImages(int successId, String searchTerm, String sort) {

        dbAdapter = new DBAdapter(this);
        successImages = new ArrayList<>();
        successImagesToRemove = new ArrayList<>();
        successImagesToAdd = new ArrayList<>();
        successImages.clear();
        dbAdapter.openDB();
        successImages.addAll(dbAdapter.retrieveSuccessImages(successId));
        successImages.add(0, addImageIv());
        dbAdapter.closeDB();
        successImageAdapter = new SuccessImageAdapter(successImages, this, R.layout.success_image_layout, getApplicationContext(), drawableSelector);
        recyclerView.setAdapter(successImageAdapter);
        successImageAdapter.notifyDataSetChanged();
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

        editTitle = (EditText) findViewById(R.id.edit_title);
        editCategory = (TextView) findViewById(R.id.edit_category);
        editCategoryIv = (ImageView) findViewById(R.id.edit_category_iv);
        editImportanceIv = (ImageView) findViewById(R.id.edit_importance_iv);
        editDescription = (EditText) findViewById(R.id.edit_description);
        editDateStarted = (TextView) findViewById(R.id.edit_date_started);
        editDateEnded = (TextView) findViewById(R.id.edit_date_ended);

        editSuccess = getIntent().getParcelableExtra(MainActivity.EXTRA_SHOW_SUCCESS_ITEM);
        successImages = getIntent().getParcelableArrayListExtra(MainActivity.EXTRA_SHOW_SUCCESS_IMAGES);

        Log.d(TAG, "initViews: " + successImages);

        editTitle.setTag(editSuccess.getId());
        editTitle.setText(editSuccess.getTitle());
        editCategory.setText(editSuccess.getCategory());
        editDescription.setText(editSuccess.getDescription());
        editDateStarted.setText(editSuccess.getDateStarted());
        checkDateEnded(editSuccess.getDateEnded());

        editImportanceIv.setTag(editSuccess.getImportance());

        drawableSelector.selectCategoryImage(editCategoryIv, editSuccess.getCategory(), editCategory);
        drawableSelector.selectImportanceImage(editImportanceIv, editSuccess.getImportance());

        getSuccessImages(editSuccess.getId(), null, "");
        Log.e(TAG, "initViews: " + editSuccess.getId());

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

    private void retrieveSuccesses(int successId, String searchTerm, String sort) {
        dbAdapter.openDB();
        successImages.addAll(dbAdapter.retrieveSuccessImages(successId));
        dbAdapter.closeDB();

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

            saveImages(editSuccess.getId());

            returnIntent.putExtra(MainActivity.EXTRA_EDIT_SUCCESS_ITEM, editSuccess);

            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    private void saveImages(int successId) {
        dbAdapter.openDB();
        dbAdapter.editSuccessImages(successImages, successId);
        dbAdapter.closeDB();
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

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();


            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                if (selectedImageNumber != 0) {
                    successImages.get(selectedImageNumber).setImagePath(imagePath);
                } else {//selected = 0
                    SuccessImage successImage = new SuccessImage(editSuccess.getId());
                    successImage.setImagePath(imagePath);
                    successImages.add(successImage);
                    successImagesToAdd.add(successImage);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            successImageAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);

            } else {
                Toast.makeText(this, "Access file: permission denied", Toast.LENGTH_SHORT).show();
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

    private void handleImage(int i, String clickLength) {

        selectedImageNumber = i;

        if (clickLength.equals("short")) {
            openGallery();
        }
        if (clickLength.equals("long")) {
            //TODO HANDLE LOONG LICK
        }
    }

    @Override
    public void onSuccessImageClick(SuccessImage successImage, ImageView successImageIv, int position, ConstraintLayout constraintLayout, CardView cardView) {

        handleImage(position, "short");

    }

    @Override
    public void onSuccessImageLongClick(SuccessImage successImage, ImageView successImageIv, int position, ConstraintLayout constraintLayout, CardView cardView) {

        handleImage(position, "long");
    }
}
