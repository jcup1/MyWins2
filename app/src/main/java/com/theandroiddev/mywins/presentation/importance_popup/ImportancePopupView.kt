package com.theandroiddev.mywins.presentation.importance_popup

import com.theandroiddev.mywins.core.mvp.MvpView
import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.Constants.Companion.Importance

interface ImportancePopupView: MvpView {

    fun displayInitialImportance(importance: Importance)

}