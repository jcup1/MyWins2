package com.theandroiddev.mywins.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.theandroiddev.mywins.UI.Helpers.Constants;
import com.theandroiddev.mywins.UI.Models.Success;
import com.theandroiddev.mywins.UI.Models.SuccessImage;

import java.util.ArrayList;
import java.util.Arrays;

import static com.theandroiddev.mywins.UI.Helpers.Constants.ADD_ON_TOP;
import static com.theandroiddev.mywins.UI.Helpers.Constants.CATEGORY_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.DATE_ENDED_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.DATE_STARTED_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.DESCRIPTION_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.IMPORTANCE_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.SUCCESS_ID_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.TITLE_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummyCategory;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummyDescription;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummyEndDate;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummyImportance;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummyStartDate;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummySuccessesSize;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummyTitle;

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
        Log.d(TAG, "addSuccess: " + success);

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TITLE, success.getTitle());
        contentValues.put(Constants.CATEGORY, success.getCategory());
        contentValues.put(Constants.IMPORTANCE, success.getImportance());
        contentValues.put(Constants.DESCRIPTION, success.getDescription());


        contentValues.put(Constants.DATE_STARTED, success.getDateStarted());
        contentValues.put(Constants.DATE_ENDED, success.getDateEnded());

        sqLiteDatabase.insert(Constants.TB_NAME_SUCCESSES, Constants.SUCCESS_ID, contentValues);
    }

    public Cursor retrieveSuccess(String searchTerm, String sort) {

        String[] columns = {Constants.SUCCESS_ID, Constants.TITLE, Constants.CATEGORY, Constants.IMPORTANCE, Constants.DESCRIPTION, Constants.DATE_STARTED, Constants.DATE_ENDED};
        Cursor cursor;
        if (searchTerm != null && searchTerm.length() > 0) {
            String sql = "SELECT * FROM " + Constants.TB_NAME_SUCCESSES + " WHERE " + Constants.TITLE + " LIKE '%" + searchTerm + "%'";
            cursor = sqLiteDatabase.rawQuery(sql, null);
            return cursor;
        }
        cursor = sqLiteDatabase.query(Constants.TB_NAME_SUCCESSES, columns, null, null, null, null, sort);
        return cursor;
    }

    public void removeSuccess(ArrayList<Success> successesToRemove) {
        Log.d(TAG, "toRemove:" + Arrays.toString(successesToRemove.toArray()));


        for (int i = 0; i < successesToRemove.size(); i++) {
            Log.d(TAG, "removeSuccess: " + successesToRemove.get(i).getTitle());
            sqLiteDatabase.delete(Constants.TB_NAME_SUCCESSES, Constants.SUCCESS_ID + "=? and " + Constants.TITLE + "=?", new String[]{String.valueOf(successesToRemove.get(i).getId()), successesToRemove.get(i).getTitle()});

        }
        Log.d(TAG, "removeSuccess: after");

    }

    public void editSuccess(Success showSuccess) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TITLE, showSuccess.getTitle());
        contentValues.put(Constants.CATEGORY, showSuccess.getCategory());
        contentValues.put(Constants.IMPORTANCE, showSuccess.getImportance());
        contentValues.put(Constants.DESCRIPTION, showSuccess.getDescription());
        contentValues.put(Constants.DATE_STARTED, showSuccess.getDateStarted());
        contentValues.put(Constants.DATE_ENDED, showSuccess.getDateEnded());

        sqLiteDatabase.update(Constants.TB_NAME_SUCCESSES, contentValues, Constants.SUCCESS_ID + "=" + showSuccess.getId(), null);
    }

    private void addSuccessImages(ArrayList<SuccessImage> successImage) {

        for (int i = 1; i < successImage.size(); i++) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.IMAGE_PATH, successImage.get(i).getImagePath());
            contentValues.put(Constants.SUCCESS_ID, successImage.get(i).getSuccessId());
            sqLiteDatabase.insert(Constants.TB_NAME_IMAGES, Constants.IMAGE_ID, contentValues);

        }

    }

    public ArrayList<SuccessImage> retrieveSuccessImages(int successId) {

        ArrayList<SuccessImage> successImages = new ArrayList<>();

        String sql = "SELECT * FROM " + Constants.TB_NAME_IMAGES + " WHERE " + Constants.SUCCESS_ID + "=" + successId;
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
        sqLiteDatabase.execSQL("DELETE FROM " + Constants.TB_NAME_IMAGES + " WHERE " + Constants.SUCCESS_ID + " = " + successId);

    }

    public Success getSuccess(int id) {

        String sql = "SELECT * FROM " + Constants.TB_NAME_SUCCESSES + " WHERE " + Constants.SUCCESS_ID + " = " + id;
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

}
