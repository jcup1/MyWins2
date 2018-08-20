package com.theandroiddev.mywins.presentation.successes

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.widget.ImageView
import android.widget.TextView
import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.mvp.MvpView

/**
 * Created by jakub on 12.11.17.
 */

interface SuccessesView : MvpView {

    fun displayDefaultSuccesses(successList: MutableList<SuccessEntity>)

    fun displayNoSuccesses()

    fun displaySuccesses(successList: MutableList<SuccessEntity>)

    fun updateAdapterList(successList: MutableList<SuccessEntity>)

    fun successRemoved(position: Int)

    fun displaySuccessChanged(position: Int, updatedSuccess: SuccessEntity)

    fun displayCategory(category: String)

    fun hideSearchBar()

    fun displaySearchBar()

    fun displayUpdatedSuccesses()

    fun displaySlider(successList: MutableList<SuccessEntity>)

    fun displaySliderAnimation(successes: MutableList<SuccessEntity>, success: SuccessEntity,
                               position: Int, titleTv: TextView, categoryTv: TextView,
                               dateStartedTv: TextView, dateEndedTv: TextView,
                               categoryIv: ImageView, importanceIv: ImageView,
                               constraintLayout: ConstraintLayout, cardView: CardView)

    fun displaySearch()

    fun restoreSuccess(position: Int, backupSuccess: SuccessEntity)

    fun displaySuccessRemoved(position: Int, backupSuccess: SuccessEntity)
}

