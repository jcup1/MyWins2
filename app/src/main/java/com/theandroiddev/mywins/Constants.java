package com.theandroiddev.mywins;

/**
 * Created by jakub on 14.08.17.
 */

public class Constants {

    static final int DB_VERSION = 4;

    static final String SUCCESS_ID = "success_id";
    static final String TITLE = "title";
    static final String CATEGORY = "category";
    static final String IMPORTANCE = "importance";
    static final String DESCRIPTION = "description";
    static final String DATE_STARTED = "date_started";
    static final String DATE_ENDED = "date_ended";
    static final String DB_NAME = "success_DB";
    static final String TB_NAME_SUCCESSES = "success_TB";
    static final String IMAGE_ID = "image_id";
    static final String TB_NAME_IMAGES = "image_TB";
    static final String FILENAME = "filename";
    static final String IMAGEDATA = "image_data";
    static final String CREATE_TB_SUCCESSES = "CREATE TABLE " + TB_NAME_SUCCESSES + "(" + SUCCESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TITLE + " VARCHAR NOT NULL," +
            CATEGORY + " VARCHAR NOT NULL," +
            IMPORTANCE + " VARCHAR NOT NULL," +
            DESCRIPTION + " VARCHAR NOT NULL," +
            DATE_STARTED + " VARCHAR NOT NULL," +
            DATE_ENDED + " VARCHAR NOT NULL);";
    static final String DROP_TB_SUCCESSES = "DROP TABLE IF EXISTS " + TB_NAME_SUCCESSES;


    static final String DROP_TB_IMAGES = "DROP TABLE IF EXISTS " + TB_NAME_IMAGES;
    static final String CREATE_TB_IMAGES = "CREATE TABLE " + TB_NAME_IMAGES + "(" + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            SUCCESS_ID + " INTEGER NOT NULL," +
            FILENAME + " TEXT," +
            IMAGEDATA + " BLOB);";



    public static String DATE_STARTED_DESC = Constants.DATE_STARTED + " DESC";
    public static String DATE_STARTED_ASC = Constants.DATE_STARTED + " ASC";

    public static String DATE_ENDED_DESC = Constants.DATE_ENDED + " DESC";
    public static String DATE_ENDED_ASC = Constants.DATE_ENDED + " ASC";

    public static String TITLE_DESC = Constants.TITLE + " DESC";
    public static String TITLE_ASC = Constants.TITLE + " ASC";

    public static String IMPORTANCE_DESC = Constants.IMPORTANCE + " DESC";
    public static String IMPORTANCE_ASC = Constants.IMPORTANCE + " ASC";

    public static String DESCRIPTION_DESC = "LENGTH(" + Constants.DESCRIPTION + ")" + " DESC";
    public static String DESCRIPTION_ASC = "LENGTH(" + Constants.DESCRIPTION + ")" + " ASC";


}
