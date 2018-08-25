package com.theandroiddev.mywins.presentation.insert_success

import com.theandroiddev.mywins.core.mvp.MvpView
import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.Constants.Companion.Category

interface InsertSuccessView : MvpView {

    fun displayInitCategory(category: Category)

}