package com.theandroiddev.mywins;

/**
 * Created by jakub on 14.08.17.
 */

public class Constants {

    static final String ROW_ID = "id";
    static final String TITLE = "title";
    static final String CATEGORY = "category";
    static final String IMPORTANCE = "importance";
    static final String DESCRIPTION = "description";
    static final String DATE_STARTED = "date_started";
    static final String DATE_ENDED = "date_ended";
    static final String DB_NAME = "success_DB";
    static final String TB_NAME = "success_TB";
    static final int DB_VERSION = 3;
    static final String CREATE_TB = "CREATE TABLE " + TB_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "title VARCHAR NOT NULL," +
            "category VARCHAR NOT NULL," +
            "importance VARCHAR NOT NULL," +
            "description VARCHAR NOT NULL," +
            "date_started VARCHAR NOT NULL," +
            "date_ended VARCHAR NOT NULL);";
    static final String DROP_TB = "DROP TABLE IF EXISTS " + TB_NAME;

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
