package com.theandroiddev.mywins.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.models.SuccessImage;
import com.theandroiddev.mywins.utils.Constants;

import java.util.ArrayList;

import static com.theandroiddev.mywins.utils.Constants.ADD_ON_TOP;
import static com.theandroiddev.mywins.utils.Constants.CATEGORY;
import static com.theandroiddev.mywins.utils.Constants.CATEGORY_VALUE;
import static com.theandroiddev.mywins.utils.Constants.DATE_ENDED;
import static com.theandroiddev.mywins.utils.Constants.DATE_ENDED_VALUE;
import static com.theandroiddev.mywins.utils.Constants.DATE_STARTED;
import static com.theandroiddev.mywins.utils.Constants.DATE_STARTED_VALUE;
import static com.theandroiddev.mywins.utils.Constants.DESCRIPTION;
import static com.theandroiddev.mywins.utils.Constants.DESCRIPTION_VALUE;
import static com.theandroiddev.mywins.utils.Constants.IMAGE_ID;
import static com.theandroiddev.mywins.utils.Constants.IMAGE_PATH;
import static com.theandroiddev.mywins.utils.Constants.IMPORTANCE;
import static com.theandroiddev.mywins.utils.Constants.IMPORTANCE_VALUE;
import static com.theandroiddev.mywins.utils.Constants.SUCCESS_ID;
import static com.theandroiddev.mywins.utils.Constants.SUCCESS_ID_VALUE;
import static com.theandroiddev.mywins.utils.Constants.TB_NAME_IMAGES;
import static com.theandroiddev.mywins.utils.Constants.TB_NAME_SUCCESSES;
import static com.theandroiddev.mywins.utils.Constants.TITLE;
import static com.theandroiddev.mywins.utils.Constants.TITLE_VALUE;
import static com.theandroiddev.mywins.utils.Constants.dummyCategory;
import static com.theandroiddev.mywins.utils.Constants.dummyDescription;
import static com.theandroiddev.mywins.utils.Constants.dummyEndDate;
import static com.theandroiddev.mywins.utils.Constants.dummyImportance;
import static com.theandroiddev.mywins.utils.Constants.dummyStartDate;
import static com.theandroiddev.mywins.utils.Constants.dummySuccessesSize;
import static com.theandroiddev.mywins.utils.Constants.dummyTitle;

/**
 * Created by jakub on 14.08.17.
 */

public class DBAdapter {
    private static final String TAG = "DBAdapter";
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private DBHelper dbHelper;

    public DBAdapter(Context context) {

        this.context = context;
        dbHelper = new DBHelper(context);
        openDB();

    }

    public void openDB() {

        sqLiteDatabase = dbHelper.getWritableDatabase();

    }

    public void closeDB() {
        dbHelper.close();
    }

    public void addSuccess(Success success) {

        sqLiteDatabase.insert(TB_NAME_SUCCESSES, SUCCESS_ID, getSuccessContentValues(success));
    }

    public Cursor retrieveSuccess(String searchTerm, String sort) {

        String[] columns = {SUCCESS_ID, TITLE, CATEGORY, IMPORTANCE, DESCRIPTION, DATE_STARTED, DATE_ENDED};
        Cursor cursor;
        if (searchTerm != null && searchTerm.length() > 0) {
            String sql = "SELECT * FROM " + TB_NAME_SUCCESSES + " WHERE " + TITLE + " LIKE '%" + searchTerm + "%'";
            cursor = sqLiteDatabase.rawQuery(sql, null);
            return cursor;
        }
        cursor = sqLiteDatabase.query(TB_NAME_SUCCESSES, columns, null, null, null, null, sort);
        return cursor;
    }

    public void removeSuccess(ArrayList<Success> successesToRemove) {


        for (int i = 0; i < successesToRemove.size(); i++) {
            sqLiteDatabase.delete(TB_NAME_SUCCESSES, SUCCESS_ID + "=? and " + TITLE + "=?", new String[]{String.valueOf(successesToRemove.get(i).getId()), successesToRemove.get(i).getTitle()});

        }

    }

    public void editSuccess(Success showSuccess) {

        sqLiteDatabase.update(TB_NAME_SUCCESSES, getSuccessContentValues(showSuccess), SUCCESS_ID + "=" + showSuccess.getId(), null);
    }

    private void addSuccessImages(ArrayList<SuccessImage> successImage) {

        for (int i = 1; i < successImage.size(); i++) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(IMAGE_PATH, successImage.get(i).getImagePath());
            contentValues.put(SUCCESS_ID, successImage.get(i).getSuccessId());
            sqLiteDatabase.insert(TB_NAME_IMAGES, IMAGE_ID, contentValues);

        }

    }

    public ArrayList<SuccessImage> getSuccessImages(int successId) {

        ArrayList<SuccessImage> successImages = new ArrayList<>();

        String sql = "SELECT * FROM " + TB_NAME_IMAGES + " WHERE " + SUCCESS_ID + "=" + successId;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                SuccessImage sI = new SuccessImage(successId);
                sI.setImagePath(cursor.getString(1));

                successImages.add(sI);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return successImages;
    }

    public void editSuccessImages(ArrayList<SuccessImage> successImages, int successId) {

        deleteImages(successId);
        addSuccessImages(successImages);

    }

    private void deleteImages(int successId) {
        sqLiteDatabase.execSQL("DELETE FROM " + TB_NAME_IMAGES + " WHERE " + SUCCESS_ID + " = " + successId);

    }

    public ArrayList<Success> getDefaultSuccesses() {

        ArrayList<Success> successList = new ArrayList<>();
        new Constants();

        for (int i = 0; i < dummySuccessesSize; i++) {
            successList.add(new Success(dummyTitle.get(i), dummyCategory.get(i), dummyImportance.get(i), dummyDescription.get(i),
                    dummyStartDate.get(i), dummyEndDate.get(i)));
            Log.e(TAG, "insertDummyData: " + i);
        }

        return successList;
    }

    public Success getSuccess(int id) {

        String sql = "SELECT * FROM " + TB_NAME_SUCCESSES + " WHERE " + SUCCESS_ID + " = " + id;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            String title = cursor.getString(1);
            String category = cursor.getString(2);
            int importance = cursor.getInt(3);
            String description = cursor.getString(4);
            String dateStarted = cursor.getString(5);
            String dateEnded = cursor.getString(6);
            cursor.close();
            Success s = new Success(title, category, importance, description, dateStarted, dateEnded);
            s.setId(id);
            s.setId(id);
            return s;
        }


        return null;
    }

    public ArrayList<Success> getSuccesses(String searchTerm, String sortType,
                                           boolean isSortingAscending) {

        Success success;
        ArrayList<Success> successList = new ArrayList<>();
        Cursor cursor = retrieveSuccess(searchTerm, sortType);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(SUCCESS_ID_VALUE);
            String title = cursor.getString(TITLE_VALUE);
            String category = cursor.getString(CATEGORY_VALUE);
            int importance = cursor.getInt(IMPORTANCE_VALUE);
            String description = cursor.getString(DESCRIPTION_VALUE);
            String dateStarted = cursor.getString(DATE_STARTED_VALUE);
            String dateEnded = cursor.getString(DATE_ENDED_VALUE);

            success = new Success(title, category, importance, description, dateStarted, dateEnded);
            success.setId(id);
            if (isSortingAscending) {
                successList.add(ADD_ON_TOP, success);
            } else {
                successList.add(success); //default = add on bottom
            }

        }

        cursor.close();

        return successList;
    }

    private ContentValues getSuccessContentValues(Success success) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, success.getTitle());
        contentValues.put(CATEGORY, success.getCategory());
        contentValues.put(IMPORTANCE, success.getImportance());
        contentValues.put(DESCRIPTION, success.getDescription());
        contentValues.put(DATE_STARTED, success.getDateStarted());
        contentValues.put(DATE_ENDED, success.getDateEnded());
        return contentValues;
    }


}
