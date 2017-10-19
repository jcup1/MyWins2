package com.theandroiddev.mywins.UI.Helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.theandroiddev.mywins.R;

/**
 * Created by jakub on 15.08.17.
 */

public class DrawableSelector {

    private Context context;

    public DrawableSelector(Context context) {

        this.context = context;
    }

    public void selectCategoryImage(ImageView image, String category, TextView categoryTv) {
        int id = R.drawable.ic_other; //default: other
        int color = R.color.other;

        if (category.equalsIgnoreCase(Constants.CATEGORY_VIDEO)) {
            id = R.drawable.ic_video;
            color = R.color.video;
        }
        if (category.equalsIgnoreCase(Constants.CATEGORY_SPORT)) {
            id = R.drawable.ic_sport;
            color = R.color.sport;
        }
        if (category.equalsIgnoreCase(Constants.CATEGORY_MONEY)) {
            id = R.drawable.ic_money;
            color = R.color.money;
        }
        if (category.equalsIgnoreCase(Constants.CATEGORY_JOURNEY)) {
            id = R.drawable.ic_journey;
            color = R.color.journey;
        }
        if (category.equalsIgnoreCase(Constants.CATEGORY_LEARN)) {
            id = R.drawable.ic_learn;
            color = R.color.learn;
        }

        Drawable myDrawable = ResourcesCompat.getDrawable(context.getResources(), id, null);
        image.setImageDrawable(myDrawable);
        image.setColorFilter(ContextCompat.getColor(context, color));
        categoryTv.setTextColor(ContextCompat.getColor(context, color));

    }

    public void selectImportanceImage(ImageView importanceIv, int importance) {

        int id = R.drawable.importance_medium; //default: other

        if (importance == Constants.IMPORTANCE_HUGE_VALUE) {
            id = R.drawable.importance_huge;
        }
        if (importance == Constants.IMPORTANCE_BIG_VALUE) {
            id = R.drawable.importance_big;
        }
        if (importance == Constants.IMPORTANCE_MEDIUM_VALUE) {
            id = R.drawable.importance_medium;
        }
        if (importance == Constants.IMPORTANCE_SMALL_VALUE) {
            id = R.drawable.importance_small;
        }

        Drawable myDrawable = ResourcesCompat.getDrawable(context.getResources(), id, null);
        importanceIv.setImageDrawable(myDrawable);

    }

    public void setImportance(int importance, TextView importanceTv, ImageView importance1Tv, ImageView importance2Tv, ImageView importance3Tv, ImageView importance4Tv) {

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

    public void setHugeImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv, ImageView importance4Iv) {
        importanceTv.setText(R.string.importance_huge);
        int hugeColor = ResourcesCompat.getColor(context.getResources(), R.color.huge, null);
        importance1Iv.setColorFilter(hugeColor);
        importance2Iv.setColorFilter(hugeColor);
        importance3Iv.setColorFilter(hugeColor);
        importance4Iv.setColorFilter(hugeColor);

    }

    public void setBigImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv, ImageView importance4Iv) {
        importanceTv.setText(R.string.importance_big);
        int almostNoColor = ResourcesCompat.getColor(context.getResources(), R.color.white_pressed, null);
        int bigColor = ResourcesCompat.getColor(context.getResources(), R.color.big, null);
        importance1Iv.setColorFilter(bigColor);
        importance2Iv.setColorFilter(bigColor);
        importance3Iv.setColorFilter(bigColor);
        importance4Iv.setColorFilter(almostNoColor);

    }

    public void setMediumImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv, ImageView importance4Iv) {
        importanceTv.setText(R.string.importance_medium);
        int almostNoColor = ResourcesCompat.getColor(context.getResources(), R.color.white_pressed, null);
        int mediumColor = ResourcesCompat.getColor(context.getResources(), R.color.medium, null);
        importance1Iv.setColorFilter(mediumColor);
        importance2Iv.setColorFilter(mediumColor);
        importance3Iv.setColorFilter(almostNoColor);
        importance4Iv.setColorFilter(almostNoColor);

    }

    public void setSmallImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv, ImageView importance4Iv) {
        importanceTv.setText(R.string.importance_small);
        int almostNoColor = ResourcesCompat.getColor(context.getResources(), R.color.white_pressed, null);
        int smallColor = ResourcesCompat.getColor(context.getResources(), R.color.small, null);
        importance1Iv.setColorFilter(smallColor);
        importance2Iv.setColorFilter(almostNoColor);
        importance3Iv.setColorFilter(almostNoColor);
        importance4Iv.setColorFilter(almostNoColor);
    }

    public int getImportance(String s) {

        if (s.equals(Constants.IMPORTANCE_HUGE)) {
            return Constants.IMPORTANCE_HUGE_VALUE;
        }
        if (s.equals(Constants.IMPORTANCE_BIG)) {
            return Constants.IMPORTANCE_BIG_VALUE;
        }
        if (s.equals(Constants.IMPORTANCE_MEDIUM)) {
            return Constants.IMPORTANCE_MEDIUM_VALUE;
        }
        if (s.equals(Constants.IMPORTANCE_SMALL)) {
            return Constants.IMPORTANCE_SMALL_VALUE;
        }
        return 3;
    }


}
