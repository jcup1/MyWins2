package com.theandroiddev.mywins.core.extensions

import android.content.Context
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

fun Context.getSelectableBackgroundResource(): Int {
    val attrs = intArrayOf(com.theandroiddev.mywins.R.attr.selectableItemBackground)
    val typedArray = this.obtainStyledAttributes(attrs)
    val backgroundResource = typedArray.getResourceId(0, 0)
    typedArray.recycle()
    return backgroundResource
}