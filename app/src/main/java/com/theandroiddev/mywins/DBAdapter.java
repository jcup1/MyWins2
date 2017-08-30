package com.theandroiddev.mywins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    public void addSuccessImages(List<SuccessImage> successImage) {

        for (int i = 1; i < successImage.size(); i++) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.IMAGE_PATH, successImage.get(i).getImagePath());
            contentValues.put(Constants.SUCCESS_ID, successImage.get(i).getSuccessId());
            sqLiteDatabase.insert(Constants.TB_NAME_IMAGES, Constants.IMAGE_ID, contentValues);

        }

    }

    public List<SuccessImage> retrieveSuccessImages(int successId) {

        List<SuccessImage> successImages = new ArrayList<>();

        //String[] columns = {Constants.IMAGE_ID, Constants.IMAGE_PATH, Constants.SUCCESS_ID};
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

    public void editSuccessImages(List<SuccessImage> successImages, int successId) {

        deleteImages(successId);
        addSuccessImages(successImages);

    }

    public void deleteImages(int successId) {
        sqLiteDatabase.execSQL("DELETE FROM " + Constants.TB_NAME_IMAGES + " WHERE " + Constants.SUCCESS_ID + " = " + successId);

    }

}
