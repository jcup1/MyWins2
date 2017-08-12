package com.example.jakubchmiel.mywins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public void add(Success success) {
        Log.d(TAG, "add: " + success);

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TITLE, success.getTitle());
        contentValues.put(Constants.CATEGORY, success.getCategory());
        contentValues.put(Constants.IMPORTANCE, success.getImportance());
        contentValues.put(Constants.DESCRIPTION, success.getDescription());
        contentValues.put(Constants.DATE, success.getDate());

        sqLiteDatabase.insert(Constants.TB_NAME, Constants.ROW_ID, contentValues);
    }

    public Cursor retrieve(String searchTerm, String sort) {

        String[] columns = {Constants.ROW_ID, Constants.TITLE, Constants.CATEGORY, Constants.IMPORTANCE, Constants.DESCRIPTION, Constants.DATE};
        Cursor cursor;
        if (searchTerm != null && searchTerm.length() > 0) {
            String sql = "SELECT * FROM " + Constants.TB_NAME + " WHERE " + Constants.TITLE + " LIKE '%" + searchTerm + "%'";
            cursor = sqLiteDatabase.rawQuery(sql, null);
            return cursor;
        }
        cursor = sqLiteDatabase.query(Constants.TB_NAME, columns, null, null, null, null, sort);
        return cursor;
    }

    public void remove(List<Success> successesToRemove) {
        Log.d(TAG, "toRemove:" + Arrays.toString(successesToRemove.toArray()));


        for (int i = 0; i < successesToRemove.size(); i++) {
            Log.d(TAG, "remove: " + successesToRemove.get(i).getTitle());
            sqLiteDatabase.delete(Constants.TB_NAME, Constants.ROW_ID + "=? and " + Constants.TITLE + "=?", new String[]{String.valueOf(successesToRemove.get(i).getId()), successesToRemove.get(i).getTitle()});

        }
        Log.d(TAG, "remove: after");

    }

    public void edit(Success showSuccess) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.TITLE, showSuccess.getTitle());
        contentValues.put(Constants.CATEGORY, showSuccess.getCategory());
        contentValues.put(Constants.IMPORTANCE, showSuccess.getImportance());
        contentValues.put(Constants.DESCRIPTION, showSuccess.getDescription());
        contentValues.put(Constants.DATE, showSuccess.getDate());

        sqLiteDatabase.update(Constants.TB_NAME, contentValues, Constants.ROW_ID + "=" + showSuccess.getId(), null);
    }
}
