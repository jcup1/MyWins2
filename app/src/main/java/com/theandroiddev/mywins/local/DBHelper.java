package com.theandroiddev.mywins.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.theandroiddev.mywins.UI.Activities.SuccessesActivity;
import com.theandroiddev.mywins.UI.Helpers.Constants;

/**
 * Created by jakub on 14.08.17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constants.CREATE_TB_SUCCESSES);
        sqLiteDatabase.execSQL(Constants.CREATE_TB_IMAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(Constants.DROP_TB_SUCCESSES);
        sqLiteDatabase.execSQL(Constants.DROP_TB_IMAGES);
        onCreate(sqLiteDatabase);

        SuccessesActivity.dbUpdate = true;

    }


}
