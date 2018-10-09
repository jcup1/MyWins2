package com.theandroiddev.mywins.presentation.insert_success

import com.theandroiddev.mywins.core.mvp.MvpPresenter
import com.theandroiddev.mywins.utils.Constants
import kotlinx.android.synthetic.main.activity_insert_success.*
import javax.inject.Inject

class InsertSuccessPresenter @Inject constructor() : MvpPresenter<InsertSuccessView, InsertSuccessBundle>() {

    override fun onViewCreated() {

        ifViewAttached { view ->

        }
    }

    fun onAfterCreate() {
        ifViewAttached { view ->
            view.displayInitCategory(bundle.category)
        }

    }

}