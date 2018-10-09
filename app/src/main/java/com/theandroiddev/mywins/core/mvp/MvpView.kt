package com.theandroiddev.mywins.core.mvp

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.theandroiddev.mywins.utils.Alerts

interface MvpView : MvpView {

    val alerts: Alerts?
}