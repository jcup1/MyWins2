package com.theandroiddev.mywins.presentation.successes

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.widget.ImageView
import android.widget.TextView
import com.theandroiddev.mywins.core.mvp.MvpView
import com.theandroiddev.mywins.utils.Constants.Companion.Category

/**
 * Created by jakub on 12.11.17.
 */

interface SuccessesView : MvpView {

    fun displayDefaultSuccesses(successList: MutableList<SuccessModel>)

    fun displayNoSuccesses()

    fun displaySuccesses(successes: List<SuccessModel>)

    fun updateAdapterList(successList: MutableList<SuccessModel>)

    fun successRemoved(position: Int)

    fun displaySuccessChanged(position: Int, updatedSuccess: SuccessModel)

    fun displayCategory(category: Category)

    fun hideSearchBar()

    fun displaySearchBar()

    fun displayUpdatedSuccesses()

    fun displaySlider(successes: MutableList<SuccessModel>)

    fun displaySliderAnimation(successes: MutableList<SuccessModel>, success: SuccessModel,
                               position: Int, titleTv: TextView, categoryTv: TextView,
                               dateStartedTv: TextView, dateEndedTv: TextView,
                               categoryIv: ImageView, importanceIv: ImageView,
                               constraintLayout: ConstraintLayout, cardView: CardView)

    fun displaySearch()

    fun restoreSuccess(position: Int, backupSuccess: SuccessModel)

    fun displaySuccessRemoved(position: Int, backupSuccess: SuccessModel)
}

