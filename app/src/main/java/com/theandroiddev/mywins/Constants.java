package com.theandroiddev.mywins;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakub on 14.08.17.
 */

class Constants {

    static final int NOT_ACTIVE = -1;

    //DUMMY VALUES
    static final int dummyImportanceDefault = 3;
    static final int dummySuccessesSize = 5;
    static final List<String> dummyTitle = new ArrayList<>();
    static final List<String> dummyCategory = new ArrayList<>();
    static final List<Integer> dummyImportance = new ArrayList<>();
    static final List<String> dummyDescription = new ArrayList<>();
    static final List<String> dummyStartDate = new ArrayList<>();
    static final List<String> dummyEndDate = new ArrayList<>();

    static final int REQUEST_CODE_INSERT = 1;
    static final int REQUEST_CODE_EDIT = 2;
    static final int REQUEST_CODE_IMPORTANCE = 3;
    static final int REQUEST_CODE_GALLERY = 4;
    static final int REQUEST_CODE_DESCRIPTION = 4;

    //REQUESTS
    static final String EXTRA_SUCCESS_ITEM = "success_item";
    static final String EXTRA_INSERT_SUCCESS_ITEM = "insert_success_item";
    static final String EXTRA_SHOW_SUCCESS_ITEM = "show_success_item";
    static final String EXTRA_DESCRIPTION = "edit_description";

    //EXTRA DATA
    static final String EXTRA_SHOW_SUCCESS_IMAGES = "show_success_images";
    static final String EXTRA_EDIT_SUCCESS_ITEM = "edit_success_item";
    static final String EXTRA_SUCCESS_TITLE = "title";
    static final String EXTRA_SUCCESS_CATEGORY_IV = "category_iv";
    static final String EXTRA_SUCCESS_CATEGORY = "category";
    static final String EXTRA_SUCCESS_DATE_STARTED = "date_started";
    static final String EXTRA_SUCCESS_DATE_ENDED = "date_ended";
    static final String EXTRA_SUCCESS_IMPORTANCE_IV = "importance_iv";
    static final String EXTRA_SUCCESS_CARD_VIEW = "success_card_view";


    //DATABASE
    static final int DB_VERSION = 8;

    static final String SUCCESS_ID = "success_id";
    static final String TITLE = "title";
    static final String CATEGORY = "category";
    static final String IMPORTANCE = "importance";
    static final String DESCRIPTION = "description";
    static final String DATE_STARTED = "date_started";
    static final String DATE_ENDED = "date_ended";

    static final int SUCCESS_ID_VALUE = 0;
    static final int TITLE_VALUE = 1;
    static final int CATEGORY_VALUE = 2;
    static final int IMPORTANCE_VALUE = 3;
    static final int DESCRIPTION_VALUE = 4;
    static final int DATE_STARTED_VALUE = 5;
    static final int DATE_ENDED_VALUE = 6;

    static final int ADD_ON_TOP = 0;


    static final String DB_NAME = "success_DB";
    static final String TB_NAME_SUCCESSES = "success_TB";
    static final String CREATE_TB_SUCCESSES = "CREATE TABLE " + TB_NAME_SUCCESSES + "(" + SUCCESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            TITLE + " VARCHAR NOT NULL," +
            CATEGORY + " VARCHAR NOT NULL," +
            IMPORTANCE + " VARCHAR NOT NULL," +
            DESCRIPTION + " VARCHAR NOT NULL," +
            DATE_STARTED + " VARCHAR NOT NULL," +
            DATE_ENDED + " VARCHAR NOT NULL);";
    static final String IMAGE_ID = "image_id";
    static final String IMAGE_PATH = "path";
    static final String TB_NAME_IMAGES = "image_TB";
    static final String CREATE_TB_IMAGES = "CREATE TABLE " + TB_NAME_IMAGES + "(" + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            IMAGE_PATH + " TEXT NOT NULL," +
            SUCCESS_ID + " INTEGER NOT NULL);";
    static final String DROP_TB_SUCCESSES = "DROP TABLE IF EXISTS " + TB_NAME_SUCCESSES;
    static final String DROP_TB_IMAGES = "DROP TABLE IF EXISTS " + TB_NAME_IMAGES;
    static String PACKAGE_NAME = "com.theandroiddev.mywins";
    static String SORT_DATE_ADDED = "";
    static String SORT_DATE_STARTED = Constants.DATE_STARTED;


    //SORTING
    static String SORT_DATE_ENDED = Constants.DATE_ENDED;
    static String SORT_TITLE = Constants.TITLE;
    static String SORT_IMPORTANCE = Constants.IMPORTANCE;
    static String SORT_DESCRIPTION = "LENGTH(" + Constants.DESCRIPTION + ")";

    static String IMPORTANCE_HUGE = "Huge";
    static String IMPORTANCE_BIG = "Big";
    static String IMPORTANCE_MEDIUM = "Medium";
    static String IMPORTANCE_SMALL = "Small";

    static String CATEGORY_VIDEO = "video";
    static String CATEGORY_SPORT = "sport";
    static String CATEGORY_MONEY = "money";
    static String CATEGORY_JOURNEY = "journey";
    static String CATEGORY_LEARN = "learn";

    static int IMPORTANCE_HUGE_VALUE = 4;
    static int IMPORTANCE_BIG_VALUE = 3;
    static int IMPORTANCE_MEDIUM_VALUE = 2;
    static int IMPORTANCE_SMALL_VALUE = 1;

    static String ERROR_TITLE = "What's the name of your success?";
    static String ERROR_DATE_ENDED = "You ended before started!";
    static String DATE_FORMAT = "yy-MM-dd";
    static String NOT_DATE_BUT_TEXT = "Date";
    static String SNACK_DESCRIPTION_TODO = "Not active!";
    static String SNACK_SUCCESS_NOT_ADDED = "Not Added!";
    static String SNACK_SUCCESS_REMOVED = "Success Removed!";
    static String SNACK_UNDO = "UNDO";
    static String SNACK_REFRESH_NEEDED = "Successes may not be synced";
    static String SNACK_IMAGE_REMOVED = "Image Removed!";
    static String SNACK_SAVED = "Saved";
    static String TOAST_PERMISSION_DENIED = "Access file: permission denied";
    static String CLICK_SHORT = "short";
    static String CLICK_LONG = "long";
    static String DATE_STARTED_EMPTY = "Start Date";
    static String DATE_ENDED_EMPTY = "End Date";

    Constants() {
        setValues();
    }

    private void setValues() {

        dummyTitle.add("Recorded First Video");
        dummyTitle.add("25.000$ Sale");
        dummyTitle.add("Visited Wroclaw");
        dummyTitle.add("Learned Java");
        dummyTitle.add("20 km marathon");

        dummyCategory.add("Video");
        dummyCategory.add("Money");
        dummyCategory.add("Journey");
        dummyCategory.add("Learn");
        dummyCategory.add("Sport");

        dummyImportance.add(3);
        dummyImportance.add(4);
        dummyImportance.add(2);
        dummyImportance.add(3);
        dummyImportance.add(4);

        dummyDescription.add("I always wanted to create youtube video and finally Hot Shitty Challenge is live...");
        dummyDescription.add("It's my first sold company. I grew it in 10 years. I denied first offer which was 5.000$ and it's one of my the best choices in my life. I...");
        dummyDescription.add("I travel now and then. Met girl but she introduced me her friend: 'Zoned'. Guess I've got to try again in 2018.");
        dummyDescription.add("This language is easier than I thought and now I'm multilingual! :O");
        dummyDescription.add("I burned a lot of calories. Fridge is empty tonight...");

        dummyStartDate.add("17-05-20");
        dummyStartDate.add("17-05-22");
        dummyStartDate.add("16-04-15");
        dummyStartDate.add("15-03-12");
        dummyStartDate.add("16-02-21");

        dummyEndDate.add("17-05-25");
        dummyEndDate.add("17-05-30");
        dummyEndDate.add("16-04-20");
        dummyEndDate.add("15-05-01");
        dummyEndDate.add("16-02-21");


    }

}
