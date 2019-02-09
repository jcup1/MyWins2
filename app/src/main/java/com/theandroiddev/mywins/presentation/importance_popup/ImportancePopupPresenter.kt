package com.theandroiddev.mywins.presentation.importance_popup

import android.os.Build
import androidx.core.content.ContextCompat
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.core.mvp.MvpPresenter
import kotlinx.android.synthetic.main.activity_popup_importance.*
import javax.inject.Inject

class ImportancePopupPresenter @Inject constructor() :
        MvpPresenter<ImportancePopupView, ImportancePopupBundle>() {

    override fun onViewCreated() {

    }

    fun onAfterCreate() {
        ifViewAttached { view ->
            view.displayInitialImportance(bundle.importance)
        }
    }
}