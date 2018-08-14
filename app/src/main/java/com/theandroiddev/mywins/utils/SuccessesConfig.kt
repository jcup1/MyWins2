package com.theandroiddev.mywins.utils

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v4.content.res.ResourcesCompat

import com.getbase.floatingactionbutton.FloatingActionButton
import com.theandroiddev.mywins.R

/**
 * Created by jakub on 08.12.17.
 */

class SuccessesConfig {

    fun configFABs(context: Context,
                   actionLearn: FloatingActionButton,
                   actionSport: FloatingActionButton,
                   actionJourney: FloatingActionButton,
                   actionBusiness: FloatingActionButton,
                   actionVideo: FloatingActionButton) {

        actionLearn.size = FloatingActionButton.SIZE_MINI
        actionSport.size = FloatingActionButton.SIZE_MINI
        actionJourney.size = FloatingActionButton.SIZE_MINI
        actionBusiness.size = FloatingActionButton.SIZE_MINI
        actionVideo.size = FloatingActionButton.SIZE_MINI

        val colorVideo = ResourcesCompat.getColor(context.resources, R.color.video, null)
        val colorMoney = ResourcesCompat.getColor(context.resources, R.color.money, null)
        val colorJourney = ResourcesCompat.getColor(context.resources, R.color.journey, null)
        val colorSport = ResourcesCompat.getColor(context.resources, R.color.sport, null)
        val colorLearn = ResourcesCompat.getColor(context.resources, R.color.learn, null)
        val colorWhite = ResourcesCompat.getColor(context.resources, R.color.white, null)

        val videoDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_video, null)
        val moneyDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_money, null)
        val journeyDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_journey, null)
        val sportDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_sport, null)
        val learnDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_learn, null)


        if (videoDrawable != null) {
            videoDrawable.colorFilter = PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN)
            actionVideo.setIconDrawable(videoDrawable)
        }
        if (moneyDrawable != null) {
            moneyDrawable.colorFilter = PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN)
            actionBusiness.setIconDrawable(moneyDrawable)
        }
        if (journeyDrawable != null) {
            journeyDrawable.colorFilter = PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN)
            actionJourney.setIconDrawable(journeyDrawable)
        }
        if (sportDrawable != null) {
            sportDrawable.colorFilter = PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN)
            actionSport.setIconDrawable(sportDrawable)
        }
        if (learnDrawable != null) {
            learnDrawable.colorFilter = PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN)
            actionLearn.setIconDrawable(learnDrawable)
        }

        actionVideo.colorNormal = colorVideo
        actionBusiness.colorNormal = colorMoney
        actionJourney.colorNormal = colorJourney
        actionSport.colorNormal = colorSport
        actionLearn.colorNormal = colorLearn


    }
}
