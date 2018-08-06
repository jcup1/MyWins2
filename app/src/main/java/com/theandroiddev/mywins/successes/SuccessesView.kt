package com.theandroiddev.mywins.successes

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.widget.ImageView
import android.widget.TextView
import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.mvp.MvpView
import java.util.*

/**
 * Created by jakub on 12.11.17.
 */

interface SuccessesView : MvpView {

    fun displayDefaultSuccesses(successList: ArrayList<Success>)

    fun displayNoSuccesses()

    fun displaySuccesses(successList: ArrayList<Success>)

    fun updateAdapterList(successList: ArrayList<Success>)

    fun undoToRemove(position: Int)

    fun successRemoved(position: Int)

    fun displaySuccessChanged(position: Int, updatedSuccess: Success)

    fun displayCategory(category: String)

    fun hideSearchBar()

    fun displaySearchBar()

    fun displayUpdatedSuccesses()

    fun displaySlider(successList: ArrayList<Success>)

    fun displaySliderAnimation(successes: ArrayList<Success>, success: Success, position: Int, titleTv: TextView, categoryTv: TextView, dateStartedTv: TextView, dateEndedTv: TextView, categoryIv: ImageView, importanceIv: ImageView, constraintLayout: ConstraintLayout, cardView: CardView)

    fun displaySearch()

    fun restoreSuccess(position: Int, backupSuccess: Success)

    fun displaySuccessRemoved(position: Int, backupSuccess: Success)
}

