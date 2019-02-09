package com.theandroiddev.mywins.presentation.successes

import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.theandroiddev.mywins.core.mvp.MvpView
import com.theandroiddev.mywins.utils.Constants.Companion.Category

/**
 * Created by jakub on 12.11.17.
 */

interface SuccessesView : MvpView {

    var isSuccessListVisible: Boolean

    fun displayDefaultSuccesses(successList: MutableList<SuccessModel>)

    fun displaySuccesses(successes: List<SuccessModel>)

    fun updateAdapterList(successList: MutableList<SuccessModel>)

    fun successRemoved(position: Int)

    fun clearSuccessesToRemove()

    fun displaySuccessChanged(position: Int, updatedSuccess: SuccessModel)

    fun displayCategory(category: Category)

    fun hideSearchBar()

    fun displaySearchBar()

    fun displayUpdatedSuccesses()

    fun displaySliderAnimation(
        successes: MutableList<SuccessModel>, success: SuccessModel,
        position: Int, titleTv: TextView, categoryTv: TextView,
        dateStartedTv: TextView, dateEndedTv: TextView,
        categoryIv: ImageView, importanceIv: ImageView,
        constraintLayout: ConstraintLayout, cardView: CardView
    )

    fun displaySearch()

    fun restoreSuccess(position: Int, backupSuccess: SuccessModel)

    fun removeSuccess(position: Int, backupSuccess: SuccessModel)

}

