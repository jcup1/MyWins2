package com.theandroiddev.mywins.utils;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.theandroiddev.mywins.R;

/**
 * Created by jakub on 08.12.17.
 */

public class SuccessesConfig {

    public void configFABs(Context context,
                           FloatingActionButton actionLearn,
                           FloatingActionButton actionSport,
                           FloatingActionButton actionJourney,
                           FloatingActionButton actionBusiness,
                           FloatingActionButton actionVideo) {

        actionLearn.setSize(FloatingActionButton.SIZE_MINI);
        actionSport.setSize(FloatingActionButton.SIZE_MINI);
        actionJourney.setSize(FloatingActionButton.SIZE_MINI);
        actionBusiness.setSize(FloatingActionButton.SIZE_MINI);
        actionVideo.setSize(FloatingActionButton.SIZE_MINI);

        int color_video = ResourcesCompat.getColor(context.getResources(), R.color.video, null);
        int color_money = ResourcesCompat.getColor(context.getResources(), R.color.money, null);
        int color_journey = ResourcesCompat.getColor(context.getResources(), R.color.journey, null);
        int color_sport = ResourcesCompat.getColor(context.getResources(), R.color.sport, null);
        int color_learn = ResourcesCompat.getColor(context.getResources(), R.color.learn, null);
        int color_white = ResourcesCompat.getColor(context.getResources(), R.color.white, null);

//        videoDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_video, null);
//        moneyDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_money, null);
//        journeyDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_journey, null);
//        sportDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_sport, null);
//        learnDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_learn, null);
//
//
//        if (videoDrawable != null) {
//            videoDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
//            actionVideo.setIconDrawable(videoDrawable);
//        }
//        if (moneyDrawable != null) {
//            moneyDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
//            actionBusiness.setIconDrawable(moneyDrawable);
//        }
//        if (journeyDrawable != null) {
//            journeyDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
//            actionJourney.setIconDrawable(journeyDrawable);
//        }
//        if (sportDrawable != null) {
//            sportDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
//            actionSport.setIconDrawable(sportDrawable);
//        }
//        if (learnDrawable != null) {
//            learnDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
//            actionLearn.setIconDrawable(learnDrawable);
//        }

//        actionVideo.setColorNormal(color_video);
//        actionBusiness.setColorNormal(color_money);
//        actionJourney.setColorNormal(color_journey);
//        actionSport.setColorNormal(color_sport);
//        actionLearn.setColorNormal(color_learn);


    }
}
