package com.theandroiddev.mywins.utils

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import android.widget.ImageView
import android.widget.TextView

import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.utils.Constants.Companion.Category
import com.theandroiddev.mywins.utils.Constants.Companion.Importance

class DrawableSelector(private val context: Context) {

    fun selectCategoryImage(image: ImageView, category: Category, categoryTv: TextView) {
        val id: Int
        val color: Int

        when(category) {
            Category.MEDIA -> {
                id = R.drawable.ic_video
                color = R.color.video
            }
            Category.SPORT -> {
                id = R.drawable.ic_sport
                color = R.color.sport
            }
            Category.BUSINESS -> {
                id = R.drawable.ic_money
                color = R.color.money
            }
            Category.JOURNEY -> {
                id = R.drawable.ic_journey
                color = R.color.journey
            }
            Category.KNOWLEDGE -> {
                id = R.drawable.ic_learn
                color = R.color.learn
            }
            //TODO change data for last 2
            Constants.Companion.Category.OTHER -> {
                id = R.drawable.ic_other
                color = R.color.learn
            }
            Constants.Companion.Category.NONE -> {
                id = R.drawable.ic_other
                color = R.color.learn
            }
        }

        val myDrawable = ResourcesCompat.getDrawable(context.resources, id, null)
        image.setImageDrawable(myDrawable)
        image.setColorFilter(ContextCompat.getColor(context, color))
        categoryTv.setTextColor(ContextCompat.getColor(context, color))

    }

    fun selectImportanceImage(importanceIv: ImageView, importance: Importance) {

        val id = when (importance) {
            Importance.HUGE -> R.drawable.importance_huge
            Importance.BIG -> R.drawable.importance_big
            Importance.MEDIUM -> R.drawable.importance_medium
            Importance.SMALL -> R.drawable.importance_small
            Importance.NONE -> R.drawable.importance_none
        }

        val myDrawable = ResourcesCompat.getDrawable(context.resources, id, null)
        importanceIv.setImageDrawable(myDrawable)

    }

    fun setImportance(importance: Int, importanceTv: TextView, importance1Tv: ImageView, importance2Tv: ImageView, importance3Tv: ImageView, importance4Tv: ImageView) {

        when (importance) {

            1 -> setSmallImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv, importance4Tv)
            2 -> setMediumImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv, importance4Tv)
            3 -> setBigImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv, importance4Tv)
            4 -> setHugeImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv, importance4Tv)
            else -> setMediumImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv, importance4Tv)
        }

    }

    fun setHugeImportance(importanceTv: TextView, importance1Iv: ImageView, importance2Iv: ImageView, importance3Iv: ImageView, importance4Iv: ImageView) {
        importanceTv.setText(R.string.importance_huge)
        val hugeColor = ResourcesCompat.getColor(context.resources, R.color.huge, null)
        importance1Iv.setColorFilter(hugeColor)
        importance2Iv.setColorFilter(hugeColor)
        importance3Iv.setColorFilter(hugeColor)
        importance4Iv.setColorFilter(hugeColor)

    }

    fun setBigImportance(importanceTv: TextView, importance1Iv: ImageView, importance2Iv: ImageView, importance3Iv: ImageView, importance4Iv: ImageView) {
        importanceTv.setText(R.string.importance_big)
        val almostNoColor = ResourcesCompat.getColor(context.resources, R.color.white_pressed, null)
        val bigColor = ResourcesCompat.getColor(context.resources, R.color.big, null)
        importance1Iv.setColorFilter(bigColor)
        importance2Iv.setColorFilter(bigColor)
        importance3Iv.setColorFilter(bigColor)
        importance4Iv.setColorFilter(almostNoColor)

    }

    fun setMediumImportance(importanceTv: TextView, importance1Iv: ImageView, importance2Iv: ImageView, importance3Iv: ImageView, importance4Iv: ImageView) {
        importanceTv.setText(R.string.importance_medium)
        val almostNoColor = ResourcesCompat.getColor(context.resources, R.color.white_pressed, null)
        val mediumColor = ResourcesCompat.getColor(context.resources, R.color.medium, null)
        importance1Iv.setColorFilter(mediumColor)
        importance2Iv.setColorFilter(mediumColor)
        importance3Iv.setColorFilter(almostNoColor)
        importance4Iv.setColorFilter(almostNoColor)

    }

    fun setSmallImportance(importanceTv: TextView, importance1Iv: ImageView, importance2Iv: ImageView, importance3Iv: ImageView, importance4Iv: ImageView) {
        importanceTv.setText(R.string.importance_small)
        val almostNoColor = ResourcesCompat.getColor(context.resources, R.color.white_pressed, null)
        val smallColor = ResourcesCompat.getColor(context.resources, R.color.small, null)
        importance1Iv.setColorFilter(smallColor)
        importance2Iv.setColorFilter(almostNoColor)
        importance3Iv.setColorFilter(almostNoColor)
        importance4Iv.setColorFilter(almostNoColor)
    }

}
