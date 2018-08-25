package com.theandroiddev.mywins.core.mvp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.theandroiddev.mywins.utils.KEY_MVP_BUNDLE
import com.theandroiddev.mywins.utils.MvpBundle

inline fun <reified T : AppCompatActivity> FragmentActivity.intent(bundle: MvpBundle = MvpBundle()): Intent {
    val intent = Intent(this, T::class.java)
    val extras = Bundle()
    extras.putSerializable(KEY_MVP_BUNDLE, bundle)
    intent.putExtras(extras)
    return intent
}

inline fun <reified T : AppCompatActivity> FragmentActivity.startActivity(bundle: MvpBundle = MvpBundle()) {
    val intent = this.intent<T>(bundle)
    this.startActivity(intent)
}

inline fun <reified T : AppCompatActivity> FragmentActivity.startActivity(
        bundle: MvpBundle = MvpBundle(),
        requestCode: Int
) {
    val intent = this.intent<T>(bundle)
    this.startActivityForResult(intent, requestCode)
}
