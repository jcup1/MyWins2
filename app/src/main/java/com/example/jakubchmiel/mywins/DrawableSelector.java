package com.example.jakubchmiel.mywins;

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

    void selectImportanceImage(ImageView importanceIv, String importance) {

        int id = R.drawable.importance_medium; //default: other

        if (importance.equalsIgnoreCase("huge")) {
            id = R.drawable.importance_huge;
        }
        if (importance.equalsIgnoreCase("big")) {
            id = R.drawable.importance_big;
        }
        if (importance.equalsIgnoreCase("medium")) {
            id = R.drawable.importance_medium;
        }
        if (importance.equalsIgnoreCase("small")) {
            return;
        }

        Drawable myDrawable = ResourcesCompat.getDrawable(context.getResources(), id, null);
        importanceIv.setImageDrawable(myDrawable);

    }

    void setImportance(String importance, TextView importanceTv, ImageView importance1Tv, ImageView importance2Tv, ImageView importance3Tv) {

        switch (importance) {

            case "Small":
                setSmallImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv);
                break;
            case "Medium":
                setMediumImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv);
                break;
            case "Big":
                setBigImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv);
                break;
            case "Huge":
                setHugeImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv);
                break;
            default:
                setBigImportance(importanceTv, importance1Tv, importance2Tv, importance3Tv);
                break;

        }

    }

    void setHugeImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv) {
        importanceTv.setText("Huge");
        int hugeColor = ResourcesCompat.getColor(context.getResources(), R.color.huge, null);
        importance1Iv.setColorFilter(hugeColor);
        importance2Iv.setColorFilter(hugeColor);
        importance3Iv.setColorFilter(hugeColor);

    }

    void setBigImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv) {
        importanceTv.setText("Big");
        int almostNoColor = ResourcesCompat.getColor(context.getResources(), R.color.white_pressed, null);
        int bigColor = ResourcesCompat.getColor(context.getResources(), R.color.big, null);
        importance1Iv.setColorFilter(bigColor);
        importance2Iv.setColorFilter(bigColor);
        importance3Iv.setColorFilter(almostNoColor);

    }

    void setMediumImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv) {
        importanceTv.setText("Medium");
        int almostNoColor = ResourcesCompat.getColor(context.getResources(), R.color.white_pressed, null);
        int mediumColor = ResourcesCompat.getColor(context.getResources(), R.color.medium, null);
        importance1Iv.setColorFilter(mediumColor);
        importance2Iv.setColorFilter(almostNoColor);
        importance3Iv.setColorFilter(almostNoColor);

    }

    void setSmallImportance(TextView importanceTv, ImageView importance1Iv, ImageView importance2Iv, ImageView importance3Iv) {
        importanceTv.setText("Small");
        int almostNoColor = ResourcesCompat.getColor(context.getResources(), R.color.white_pressed, null);
        importance1Iv.setColorFilter(almostNoColor);
        importance2Iv.setColorFilter(almostNoColor);
        importance3Iv.setColorFilter(almostNoColor);
    }


}
