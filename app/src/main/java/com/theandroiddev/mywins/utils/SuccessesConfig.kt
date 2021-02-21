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
                   actionEducation: FloatingActionButton,
                   actionSport: FloatingActionButton,
                   actionJourney: FloatingActionButton,
                   actionBusiness: FloatingActionButton,
                   actionMedia: FloatingActionButton,
                   actionHobby: FloatingActionButton) {

        actionEducation.size = FloatingActionButton.SIZE_MINI
        actionSport.size = FloatingActionButton.SIZE_MINI
        actionJourney.size = FloatingActionButton.SIZE_MINI
        actionBusiness.size = FloatingActionButton.SIZE_MINI
        actionMedia.size = FloatingActionButton.SIZE_MINI

        val colorMedia = ResourcesCompat.getColor(context.resources, R.color.video, null)
        val colorBusiness = ResourcesCompat.getColor(context.resources, R.color.money, null)
        val colorJourney = ResourcesCompat.getColor(context.resources, R.color.journey, null)
        val colorSport = ResourcesCompat.getColor(context.resources, R.color.sport, null)
        val colorEducation = ResourcesCompat.getColor(context.resources, R.color.learn, null)
        val colorHobby = ResourcesCompat.getColor(context.resources, R.color.hobby, null)
        val colorWhite = ResourcesCompat.getColor(context.resources, R.color.white, null)

        val videoDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_video, null)
        val moneyDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_money, null)
        val journeyDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_journey, null)
        val sportDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_sport, null)
        val learnDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_learn, null)
        val hobbyDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_hobby, null)


        if (videoDrawable != null) {
            videoDrawable.colorFilter = PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN)
            actionMedia.setIconDrawable(videoDrawable)
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
            actionEducation.setIconDrawable(learnDrawable)
        }

        if (hobbyDrawable != null) {
            hobbyDrawable.colorFilter = PorterDuffColorFilter(colorWhite, PorterDuff.Mode.SRC_IN)
            actionHobby.setIconDrawable(hobbyDrawable)
        }

        actionMedia.colorNormal = colorMedia
        actionBusiness.colorNormal = colorBusiness
        actionJourney.colorNormal = colorJourney
        actionSport.colorNormal = colorSport
        actionEducation.colorNormal = colorEducation
        actionHobby.colorNormal = colorHobby


    }
}
