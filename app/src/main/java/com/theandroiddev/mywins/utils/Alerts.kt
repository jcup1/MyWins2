package com.theandroiddev.mywins.utils

import android.app.Activity
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import com.theandroiddev.mywins.R

class Alerts(val activity: Activity) {

    fun displayUnexpectedError() {
        activity.getString(R.string.unexpected_error)?.let { displaySnackbar(it) }
    }

    fun displaySnackbar(
            message: String,
            length: Int = Snackbar.LENGTH_LONG,
            f: (Snackbar.() -> Unit)? = null
    ) {
        activity.findViewById<View>(activity.window.decorView.rootView.id)?.snack(message, length, f)
    }
}

fun View.snack(
        message: String,
        length: Int = Snackbar.LENGTH_LONG,
        f: (Snackbar.() -> Unit)? = null
) {
    val snack = Snackbar.make(this, message, length)
    if(f != null) {
        snack.f()
    }
    val actionColor = ContextCompat.getColor(context, R.color.accent)
    snack.setActionTextColor(actionColor)
    snack.show()
}