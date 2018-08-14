package com.theandroiddev.mywins.utils

import java.util.*

/**
 * Created by jakub on 14.08.17.
 */

class Constants {
    init {
        setValues()
    }

    private fun setValues() {

        dummyTitle.add("Recorded First Video")
        dummyTitle.add("25.000$ Sale")
        dummyTitle.add("Visited Wroclaw")
        dummyTitle.add("Learned Java")
        dummyTitle.add("20 km marathon")

        dummyCategory.add("Video")
        dummyCategory.add("Money")
        dummyCategory.add("Journey")
        dummyCategory.add("Learn")
        dummyCategory.add("Sport")

        dummyImportance.add(3)
        dummyImportance.add(4)
        dummyImportance.add(2)
        dummyImportance.add(3)
        dummyImportance.add(4)

        dummyDescription.add("I always wanted to create youtube video and finally Hot Shitty Challenge is live...")
        dummyDescription.add("It's my first sold company. I grew it in 10 years. I denied first offer which was 5.000$ and it's one of my the best choices in my life. I...")
        dummyDescription.add("I travel now and then. Met girl but she introduced me her friend: 'Zoned'. Guess I've got to try again in 2018.")
        dummyDescription.add("This language is easier than I thought and now I'm multilingual! :O")
        dummyDescription.add("I burned a lot of calories. Fridge is empty tonight...")

        dummyStartDate.add("17-05-20")
        dummyStartDate.add("17-05-22")
        dummyStartDate.add("16-04-15")
        dummyStartDate.add("15-03-12")
        dummyStartDate.add("16-02-21")

        dummyEndDate.add("17-05-25")
        dummyEndDate.add("17-05-30")
        dummyEndDate.add("16-04-20")
        dummyEndDate.add("15-05-01")
        dummyEndDate.add("16-02-21")


    }

    companion object {

        val NOT_ACTIVE = -1

        //DUMMY VALUES
        val dummyImportanceDefault = 3
        val dummySuccessesSize = 5
        val dummyTitle: MutableList<String> = ArrayList()
        val dummyCategory: MutableList<String> = ArrayList()
        val dummyImportance: MutableList<Int> = ArrayList()
        val dummyDescription: MutableList<String> = ArrayList()
        val dummyStartDate: MutableList<String> = ArrayList()
        val dummyEndDate: MutableList<String> = ArrayList()

        val REQUEST_CODE_INSERT = 1
        val REQUEST_CODE_IMPORTANCE = 3
        val REQUEST_CODE_GALLERY = 4
        val REQUEST_CODE_DESCRIPTION = 4
        val DATE = "Date"
        //REQUESTS
        val EXTRA_SUCCESS_ITEM = "success_item"
        val EXTRA_INSERT_SUCCESS_ITEM = "insert_success_item"
        val EXTRA_SHOW_SUCCESS_ITEM = "show_success_item"
        val EXTRA_DESCRIPTION = "edit_description"
        //EXTRA DATA
        val EXTRA_SHOW_SUCCESS_IMAGES = "show_success_images"
        val EXTRA_EDIT_SUCCESS_ITEM = "edit_success_item"
        val EXTRA_SUCCESS_TITLE = "title"
        val EXTRA_SUCCESS_CATEGORY_IV = "category_iv"
        val EXTRA_SUCCESS_CATEGORY = "category"
        val EXTRA_SUCCESS_DATE_STARTED = "date_started"
        val EXTRA_SUCCESS_DATE_ENDED = "date_ended"
        val EXTRA_SUCCESS_IMPORTANCE_IV = "importance_iv"
        val EXTRA_SUCCESS_CARD_VIEW = "success_card_view"
        //DATABASE
        val DB_VERSION = 9
        val SUCCESS_ID = "success_id"
        val TITLE = "title"
        val CATEGORY = "category"
        val IMPORTANCE = "importance"
        val DESCRIPTION = "description"
        val DATE_STARTED = "date_started"
        val DATE_ENDED = "date_ended"
        val SUCCESS_ID_VALUE = 0
        val TITLE_VALUE = 1
        val CATEGORY_VALUE = 2
        val IMPORTANCE_VALUE = 3
        val DESCRIPTION_VALUE = 4
        val DATE_STARTED_VALUE = 5
        val DATE_ENDED_VALUE = 6
        val ADD_ON_TOP = 0
        val DB_NAME = "success_DB"
        val TB_NAME_SUCCESSES = "success_TB"
        val CREATE_TB_SUCCESSES = "CREATE TABLE " + TB_NAME_SUCCESSES + "(" + SUCCESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TITLE + " VARCHAR NOT NULL," +
                CATEGORY + " VARCHAR NOT NULL," +
                IMPORTANCE + " VARCHAR NOT NULL," +
                DESCRIPTION + " VARCHAR NOT NULL," +
                DATE_STARTED + " VARCHAR NOT NULL," +
                DATE_ENDED + " VARCHAR NOT NULL);"
        val IMAGE_ID = "image_id"
        val IMAGE_PATH = "path"
        val TB_NAME_IMAGES = "image_TB"
        val CREATE_TB_IMAGES = "CREATE TABLE " + TB_NAME_IMAGES + "(" + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                IMAGE_PATH + " TEXT NOT NULL," +
                SUCCESS_ID + " INTEGER NOT NULL);"
        val DROP_TB_SUCCESSES = "DROP TABLE IF EXISTS $TB_NAME_SUCCESSES"
        val DROP_TB_IMAGES = "DROP TABLE IF EXISTS $TB_NAME_IMAGES"
        val REQUEST_CODE_SLIDER = 9
        internal val REQUEST_CODE_EDIT = 2
        var CATEGORY_VIDEO = "video"
        var CATEGORY_SPORT = "sport"
        var CATEGORY_MONEY = "money"
        var CATEGORY_JOURNEY = "journey"
        var CATEGORY_LEARN = "learn"
        var PACKAGE_NAME = "com.theandroiddev.mywins"
        var SORT_DATE_ADDED = ""
        var SORT_DATE_STARTED = Constants.DATE_STARTED
        var SORT_DATE_ENDED = Constants.DATE_ENDED
        var SORT_TITLE = Constants.TITLE
        var SORT_IMPORTANCE = Constants.IMPORTANCE
        var SORT_DESCRIPTION = "LENGTH(" + Constants.DESCRIPTION + ")"
        var CLICK_SHORT = "short"
        var CLICK_LONG = "long"
        var IMPORTANCE_HUGE = "Huge"
        var IMPORTANCE_BIG = "Big"
        var IMPORTANCE_MEDIUM = "Medium"
        var IMPORTANCE_SMALL = "Small"
        //SORTING
        var IMPORTANCE_HUGE_VALUE = 4
        var IMPORTANCE_BIG_VALUE = 3
        var IMPORTANCE_MEDIUM_VALUE = 2
        var IMPORTANCE_SMALL_VALUE = 1
        var DATE_FORMAT = "yy-MM-dd"
    }

}
