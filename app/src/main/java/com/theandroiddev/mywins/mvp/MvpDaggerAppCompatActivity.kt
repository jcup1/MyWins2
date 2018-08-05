package com.theandroiddev.mywins.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class MvpDaggerAppCompatActivity<V : MvpView, P : MvpPresenter<V>>
    : MvpActivity<V, P>(), MvpView, HasFragmentInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var _supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var _frameworkFragmentInjector: DispatchingAndroidInjector<android.app.Fragment>

    @Inject
    lateinit var injectedPresenter: P

    override fun createPresenter(): P = injectedPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return _supportFragmentInjector
    }

    override fun fragmentInjector(): AndroidInjector<android.app.Fragment>? {
        return _frameworkFragmentInjector
    }

}
