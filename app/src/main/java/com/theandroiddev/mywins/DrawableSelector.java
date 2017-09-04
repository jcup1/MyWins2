package com.theandroiddev.mywins;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.ImageView;
import android.widget.TextView;

import static com.theandroiddev.mywins.Constants.CATEGORY_JOURNEY;
import static com.theandroiddev.mywins.Constants.CATEGORY_LEARN;
import static com.theandroiddev.mywins.Constants.CATEGORY_MONEY;
import static com.theandroiddev.mywins.Constants.CATEGORY_SPORT;
import static com.theandroiddev.mywins.Constants.CATEGORY_VIDEO;
import static com.theandroiddev.mywins.Constants.IMPORTANCE_BIG;
import static com.theandroiddev.mywins.Constants.IMPORTANCE_BIG_VALUE;
import static com.theandroiddev.mywins.Constants.IMPORTANCE_HUGE;
import static com.theandroiddev.mywins.Constants.IMPORTANCE_HUGE_VALUE;
import static com.theandroiddev.mywins.Constants.IMPORTANCE_MEDIUM;
import static com.theandroiddev.mywins.Constants.IMPORTANCE_MEDIUM_VALUE;
import static com.theandroiddev.mywins.Constants.IMPORTANCE_SMALL;
import static com.theandroiddev.mywins.Constants.IMPORTANCE_SMALL_VALUE;

/**
 * Created by jakub on 15.08.17.
 */

class DrawableSelector {

    private Context context;

    DrawableSelector(Context context) {

        this.context = context;
    }

    void selectCategoryImage(ImageView image, String category, TextView categoryTv) {
        int id = R.drawable.ic_other; //default: other
        int color = R.color.other;

        if (category.equalsIgnoreCase(CATEGORY_VIDEO)) {
            id = R.drawable.ic_video;
            color = R.color.video;
        }
        if (category.equalsIgnoreCase(CATEGORY_SPORT)) {
            id = R.drawable.ic_sport;
            color = R.color.sport;
        }
        if (category.equalsIgnoreCase(CATEGORY_MONEY)) {
            id = R.drawable.ic_money;
            color = R.color.money;
        }
        if (category.equalsIgnoreCase(CATEGORY_JOURNEY)) {
            id = R.drawable.ic_journey;
            color = R.color.journey;
        }
        if (category.equalsIgnoreCase(CATEGORY_LEARN)) {
            id = R.drawable.ic_learn;
            color = R.color.learn;
        }

        Drawable myDrawable = ResourcesCompat.getDrawable(context.getResources(), id, null);
        image.setImageDrawable(myDrawable);
        image.setColorFilter(ContextCompat.getColor(context, color));
        categoryTv.setTextColor(ContextCompat.getColor(context, color));

    }

    void selectImportanceImage(ImageView importanceIv, int importance) {

        int id = R.drawable.importance_medium; //default: other

        if (importance == IMPORTANCE_HUGE_VALUE) {
            id = R.drawable.importance_huge;
        }
        if (importance == IMPORTANCE_BIG_VALUE) {
            id = R.drawable.importance_big;
        }
        if (importance == IMPORTANCE_MEDIUM_VALUE) {
            id = R.drawable.importance_medium;
        }
        if (importance == IMPORTANCE_SMALL_VALUE) {
            id = R.drawable.importance_small;
        }

        Drawable myDrawable = ResourcesCompat.getDrawable(context.getResources(), id, null);
        importanceIv.setImageDrawable(myDrawable);

    }

    void setImportance(int importance, TextView importanceTv, ImageView importance1Tv, ImageView importance2Tv, ImageView importance3Tv, ImageView importance4Tv) {

        switch (importance) {

            case 1:
                setSmallImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv, importance4Tv);
                break;
            case 2:
                setMediumImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv, importance4Tv);
                break;
            case 3:
                setBigImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv, importance4Tv);
                break;
            case 4:
                setHugeImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv, importance4Tv);
                break;
            default:
                setMediumImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv, importance4Tv);
                break;

        }

    }

    void setHugeImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv, ImageView importance4Iv) {
        importanceTv.setText(R.string.importance_huge);
        int hugeColor = ResourcesCompat.getColor(context.getResources(), R.color.huge, null);
        importance1Iv.setColorFilter(hugeColor);
        importance2Iv.setColorFilter(hugeColor);
        importance3Iv.setColorFilter(hugeColor);
        importance4Iv.setColorFilter(hugeColor);

    }

    void setBigImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv, ImageView importance4Iv) {
        importanceTv.setText(R.string.importance_big);
        int almostNoColor = ResourcesCompat.getColor(context.getResources(), R.color.white_pressed, null);
        int bigColor = ResourcesCompat.getColor(context.getResources(), R.color.big, null);
        importance1Iv.setColorFilter(bigColor);
        importance2Iv.setColorFilter(bigColor);
        importance3Iv.setColorFilter(bigColor);
        importance4Iv.setColorFilter(almostNoColor);

    }

    void setMediumImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv, ImageView importance4Iv) {
        importanceTv.setText(R.string.importance_medium);
        int almostNoColor = ResourcesCompat.getColor(context.getResources(), R.color.white_pressed, null);
        int mediumColor = ResourcesCompat.getColor(context.getResources(), R.color.medium, null);
        importance1Iv.setColorFilter(mediumColor);
        importance2Iv.setColorFilter(mediumColor);
        importance3Iv.setColorFilter(almostNoColor);
        importance4Iv.setColorFilter(almostNoColor);

    }

    void setSmallImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv, ImageView importance4Iv) {
        importanceTv.setText(R.string.importance_small);
        int almostNoColor = ResourcesCompat.getColor(context.getResources(), R.color.white_pressed, null);
        int smallColor = ResourcesCompat.getColor(context.getResources(), R.color.small, null);
        importance1Iv.setColorFilter(smallColor);
        importance2Iv.setColorFilter(almostNoColor);
        importance3Iv.setColorFilter(almostNoColor);
        importance4Iv.setColorFilter(almostNoColor);
    }

    int getImportance(String s) {

        if (s.equals(IMPORTANCE_HUGE)) {
            return IMPORTANCE_HUGE_VALUE;
        }
        if (s.equals(IMPORTANCE_BIG)) {
            return IMPORTANCE_BIG_VALUE;
        }
        if (s.equals(IMPORTANCE_MEDIUM)) {
            return IMPORTANCE_MEDIUM_VALUE;
        }
        if (s.equals(IMPORTANCE_SMALL)) {
            return IMPORTANCE_SMALL_VALUE;
        }
        return 3;
    }


}
