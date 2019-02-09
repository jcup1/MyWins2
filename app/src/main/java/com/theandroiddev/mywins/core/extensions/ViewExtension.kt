package com.theandroiddev.mywins.core.extensions

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE

fun View?.visibleOrInvisible(isVisible: Boolean) {
    if (isVisible) {
        this?.visibility = VISIBLE
    } else {
        this?.visibility = INVISIBLE
    }
}