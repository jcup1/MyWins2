package com.example.jakubchmiel.mywins;

/**
 * Created by jakub on 14.08.17.
 */

public class Constants {

    static final String ROW_ID = "id";
    static final String TITLE = "title";
    static final String CATEGORY = "category";
    static final String IMPORTANCE = "importance";
    static final String DESCRIPTION = "description";
    static final String DATE = "date";
    static final String DB_NAME = "success_DB";
    static final String TB_NAME = "success_TB";
    static final int DB_VERSION = 1;
    static final String CREATE_TB = "CREATE TABLE " + TB_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "title VARCHAR NOT NULL," +
            "category VARCHAR NOT NULL," +
            "importance VARCHAR NOT NULL," +
            "description VARCHAR NOT NULL," +
            "date VARCHAR NOT NULL);";
    static final String DROP_TB = "DROP TABLE IF EXISTS " + TB_NAME;
    public static String DATEDESC = Constants.DATE + " DESC";
    public static String DATEASC = Constants.DATE + " ASC";
    public static String TITLEDESC = Constants.TITLE + " ASC";
    public static String TITLEASC = Constants.TITLE + " DESC";
}
