package com.theandroiddev.mywins;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jakub on 15.08.17.
 */

public class DrawableSelector {

    private Context context;

    public DrawableSelector(Context context) {

        this.context = context;
    }

    void selectCategoryImage(ImageView image, String category, TextView categoryTv) {
        int id = R.drawable.ic_other; //default: other
        int color = R.color.other;

        if (category.equalsIgnoreCase("video")) {
            id = R.drawable.ic_video;
            color = R.color.video;
        }
        if (category.equalsIgnoreCase("sport")) {
            id = R.drawable.ic_sport;
            color = R.color.sport;
        }
        if (category.equalsIgnoreCase("money")) {
            id = R.drawable.ic_money;
            color = R.color.money;
        }
        if (category.equalsIgnoreCase("journey")) {
            id = R.drawable.ic_journey;
            color = R.color.journey;
        }
        if (category.equalsIgnoreCase("learn")) {
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

        if (importance == 4) {
            id = R.drawable.importance_huge;
        }
        if (importance == 3) {
            id = R.drawable.importance_big;
        }
        if (importance == 2) {
            id = R.drawable.importance_medium;
        }
        if (importance == 1) {
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
        importanceTv.setText("Huge");
        int hugeColor = ResourcesCompat.getColor(context.getResources(), R.color.huge, null);
        importance1Iv.setColorFilter(hugeColor);
        importance2Iv.setColorFilter(hugeColor);
        importance3Iv.setColorFilter(hugeColor);
        importance4Iv.setColorFilter(hugeColor);

    }

    void setBigImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv, ImageView importance4Iv) {
        importanceTv.setText("Big");
        int almostNoColor = ResourcesCompat.getColor(context.getResources(), R.color.white_pressed, null);
        int bigColor = ResourcesCompat.getColor(context.getResources(), R.color.big, null);
        importance1Iv.setColorFilter(bigColor);
        importance2Iv.setColorFilter(bigColor);
        importance3Iv.setColorFilter(bigColor);
        importance4Iv.setColorFilter(almostNoColor);

    }

    void setMediumImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv, ImageView importance4Iv) {
        importanceTv.setText("Medium");
        int almostNoColor = ResourcesCompat.getColor(context.getResources(), R.color.white_pressed, null);
        int mediumColor = ResourcesCompat.getColor(context.getResources(), R.color.medium, null);
        importance1Iv.setColorFilter(mediumColor);
        importance2Iv.setColorFilter(mediumColor);
        importance3Iv.setColorFilter(almostNoColor);
        importance4Iv.setColorFilter(almostNoColor);

    }

    void setSmallImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv, ImageView importance4Iv) {
        importanceTv.setText("Small");
        int almostNoColor = ResourcesCompat.getColor(context.getResources(), R.color.white_pressed, null);
        int smallColor = ResourcesCompat.getColor(context.getResources(), R.color.small, null);
        importance1Iv.setColorFilter(smallColor);
        importance2Iv.setColorFilter(almostNoColor);
        importance3Iv.setColorFilter(almostNoColor);
        importance4Iv.setColorFilter(almostNoColor);
    }

    public int getImportance(String s) {

        if (s.equals("Huge")) {
            return 4;
        }
        if (s.equals("Big")) {
            return 3;
        }
        if (s.equals("Medium")) {
            return 2;
        }
        if (s.equals("Small")) {
            return 1;
        }
        return 3;
    }


}
