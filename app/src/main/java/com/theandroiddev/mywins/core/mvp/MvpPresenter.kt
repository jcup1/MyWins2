package com.theandroiddev.mywins.core.mvp

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.Constants.Companion.Category
import com.theandroiddev.mywins.utils.MvpBundle
import java.io.Serializable


abstract class MvpPresenter<V : MvpView, B : Serializable> : MvpBasePresenter<V>() {

    lateinit var bundle: B

    abstract fun onViewCreated()

}
