package com.theandroiddev.mywins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jakub on 14.08.17.
 */

public class DBAdapter {
    private static final String TAG = "DBAdapter";
    Context context;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;

    public DBAdapter(Context context) {

        this.context = context;
        dbHelper = new DBHelper(context);

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

    public void addImages(List<SuccessImage> successImages, int successId) {

        for (int i = 0; i < successImages.size(); i++) {
            Log.d(TAG, "addImages: ITERATE" + i);

            String sql = "INSERT INTO " + Constants.TB_NAME_IMAGES + " VALUES (NULL, ?, ?, ?)";
            SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
            sqLiteStatement.clearBindings();
            sqLiteStatement.bindDouble(1, successId);
            sqLiteStatement.bindString(2, successImages.get(i).getFileName());
            sqLiteStatement.bindBlob(3, successImages.get(i).getImageData());

            sqLiteStatement.executeInsert();

        }

    }

    public Cursor retrieveImage(String sql) {
        return sqLiteDatabase.rawQuery(sql, null);
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

    public ArrayList<SuccessImage> retrieveSuccessImages(int successId, String searchTerm, String sort) {

        String[] columns = {Constants.IMAGE_ID, Constants.SUCCESS_ID, Constants.FILENAME, Constants.IMAGEDATA};

        ArrayList<SuccessImage> successImagesRetrieved = new ArrayList<>();
        String sql = "SELECT * FROM " + Constants.TB_NAME_IMAGES + " WHERE "
                + Constants.SUCCESS_ID + " = " + successId;

        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        try {
            // Move to first row
            if (!cursor.moveToFirst())
                return successImagesRetrieved;
            do {
                Log.d(TAG, "retrieveSuccessImages: " + 1);
                SuccessImage successImage = new SuccessImage();
                successImage.setId(cursor.getInt(0));
                successImage.setSuccessId(cursor.getInt(1));
                successImage.setFileName(cursor.getString(2));
                successImage.setImageData(cursor.getBlob(3));

                successImagesRetrieved.add(successImage);
            } while (cursor.moveToNext());
            cursor.close();

            return successImagesRetrieved;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Cursor retrieveSuccessImage(int successId, String searchTerm, String sort) {

        String[] columns = {Constants.IMAGE_ID, Constants.SUCCESS_ID, Constants.FILENAME, Constants.IMAGEDATA};
        Cursor cursor;
        if (searchTerm != null && searchTerm.length() > 0) {
            String sql = "SELECT * FROM " + Constants.TB_NAME_IMAGES + " WHERE " + Constants.SUCCESS_ID + " LIKE '%" + successId + "%'";
            cursor = sqLiteDatabase.rawQuery(sql, null);
            return cursor;
        }
        cursor = sqLiteDatabase.query(Constants.TB_NAME_SUCCESSES, columns, null, null, null, null, sort);
        return cursor;
    }

    public void removeSuccess(List<Success> successesToRemove) {
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


    public void removeSuccessImage(List<SuccessImage> successImagesToRemove) {
        Log.d(TAG, "toRemove:" + Arrays.toString(successImagesToRemove.toArray()));

        for (int i = 0; i < successImagesToRemove.size(); i++) {
            Log.d(TAG, "removeSuccess: " + successImagesToRemove.get(i).getFileName());
            sqLiteDatabase.delete(Constants.TB_NAME_IMAGES, Constants.IMAGE_ID + "=? and " + Constants.FILENAME + "=?", new String[]{String.valueOf(successImagesToRemove.get(i).getId()), successImagesToRemove.get(i).getFileName()});

        }
        Log.d(TAG, "removeSuccessImage: after");

    }

}
