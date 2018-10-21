package com.theandroiddev.mywins.utils

import com.theandroiddev.mywins.R
import java.util.*

/**
 * Created by jakub on 14.08.17.
 */

class Constants {
    init {
        setValues()
    }

    private fun setValues() {

        dummyTitles.add("Recorded First Video")
        dummyTitles.add("25.000$ Sale")
        dummyTitles.add("Visited Wroclaw")
        dummyTitles.add("Learned Java")
        dummyTitles.add("20 km marathon")

        dummyCategories.add(Category.VIDEO)
        dummyCategories.add(Category.MONEY)
        dummyCategories.add(Category.JOURNEY)
        dummyCategories.add(Category.LEARN)
        dummyCategories.add(Category.SPORT)

        dummyImportances.add(3)
        dummyImportances.add(4)
        dummyImportances.add(2)
        dummyImportances.add(3)
        dummyImportances.add(4)

        dummyDescriptions.add("I always wanted to create youtube video and finally Hot Shitty Challenge is live...")
        dummyDescriptions.add("It's my first sold company. I grew it in 10 years. I denied first offer which was 5.000$ and it's one of my the best choices in my life. I...")
        dummyDescriptions.add("I travel now and then. Met girl but she introduced me her friend: 'Zoned'. Guess I've got to try again in 2018.")
        dummyDescriptions.add("This language is easier than I thought and now I'm multilingual! :O")
        dummyDescriptions.add("I burned a lot of calories. Fridge is empty tonight...")

        dummyStartDates.add("20-05-17")
        dummyStartDates.add("22-05-17")
        dummyStartDates.add("15-04-16")
        dummyStartDates.add("12-03-15")
        dummyStartDates.add("21-02-16")

        dummyEndDates.add("25-05-17")
        dummyEndDates.add("30-05-17")
        dummyEndDates.add("20-04-16")
        dummyEndDates.add("01-05-15")
        dummyEndDates.add("21-02-16")

    }

    companion object {

        //DUMMY VALUES
        val dummyImportanceDefault = 1
        val dummyTitles: MutableList<String> = ArrayList()
        val dummyCategories: MutableList<Category> = ArrayList()
        val dummyImportances: MutableList<Int> = ArrayList()
        val dummyDescriptions: MutableList<String> = ArrayList()
        val dummyStartDates: MutableList<String> = ArrayList()
        val dummyEndDates: MutableList<String> = ArrayList()

        //REQUESTS
        val REQUEST_CODE_INSERT = 1
        val REQUEST_CODE_IMPORTANCE = 3
        val REQUEST_CODE_GALLERY = 4
        val REQUEST_CODE_DESCRIPTION = 4
        val REQUEST_CODE_SLIDER = 9

        //EXTRA DATA
        val EXTRA_SUCCESS_ITEM = "success_item"
        val EXTRA_INSERT_SUCCESS_ITEM = "insert_success_item"
        val EXTRA_SHOW_SUCCESS_ITEM = "show_success_item"
        val EXTRA_DESCRIPTION = "edit_description"
        val EXTRA_SHOW_SUCCESS_IMAGES = "show_success_images"
        val EXTRA_EDIT_SUCCESS_ITEM = "edit_success_item"
        val EXTRA_SUCCESS_TITLE = "title"
        val EXTRA_SUCCESS_CATEGORY_IV = "category_iv"
        val EXTRA_SUCCESS_CATEGORY = "category"
        val EXTRA_SUCCESS_DATE_STARTED = "date_started"
        val EXTRA_SUCCESS_DATE_ENDED = "date_ended"
        val EXTRA_SUCCESS_IMPORTANCE_IV = "importance_iv"
        val EXTRA_SUCCESS_CARD_VIEW = "success_card_view"


        val NOT_ACTIVE = -1
        var PACKAGE_NAME = "com.theandroiddev.mywins"
        var DATE_FORMAT = "dd-MM-yy"
        val DATE = "Date"

        enum class Category(val res: Int) {
            VIDEO(R.string.category_video),
            SPORT(R.string.category_sport),
            MONEY(R.string.category_money),
            JOURNEY(R.string.category_journey),
            LEARN(R.string.category_learn),
            OTHER(R.string.category_other),
            NONE(R.string.category_none)
        }

        enum class Importance(val value: Int, val res: Int) {
            NONE(0, R.string.importance_none),
            SMALL(1, R.string.importance_small),
            MEDIUM(2, R.string.importance_medium),
            BIG(3, R.string.importance_big),
            HUGE(4, R.string.importance_huge)
        }

        enum class SortType {
            TITLE,
            CATEGORY,
            IMPORTANCE,
            DESCRIPTION,
            DATE_STARTED,
            DATE_ENDED,
            DATE_ADDED
        }

    }
}
