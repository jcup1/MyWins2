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

        dummyCategories.add(Category.MEDIA)
        dummyCategories.add(Category.BUSINESS)
        dummyCategories.add(Category.JOURNEY)
        dummyCategories.add(Category.KNOWLEDGE)
        dummyCategories.add(Category.SPORT)

        dummyImportances.add(3)
        dummyImportances.add(4)
        dummyImportances.add(2)
        dummyImportances.add(3)
        dummyImportances.add(4)

        dummyDescriptions.add("I had always wanted to create my own youtube video and finally I finished my Hot Icy Challenge!")
        dummyDescriptions.add("It's my biggest deal ever. I just sold company that I developed for 2 years. I'm glad that I rejected all the tempting offers that I received earlier.")
        dummyDescriptions.add("I travel from time to time but this is a place that I always wanted to visit.")
        dummyDescriptions.add("This language is easier and more useful than I thought. Now I'm multilingual! :O")
        dummyDescriptions.add("I just burned 50 calories. I better go eat something...")

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
            MEDIA(R.string.category_media),
            SPORT(R.string.category_sport),
            BUSINESS(R.string.category_business),
            JOURNEY(R.string.category_journey),
            KNOWLEDGE(R.string.category_learn),
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
