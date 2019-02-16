package com.theandroiddev.mywins.presentation.insert_success

import com.theandroiddev.mywins.core.mvp.MvpView
import com.theandroiddev.mywins.presentation.successes.SuccessCategory

interface InsertSuccessView : MvpView {

    fun displayInitCategory(category: SuccessCategory)

}