package com.theandroiddev.mywins.core.mvp

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.theandroiddev.mywins.utils.Alerts
import com.theandroiddev.mywins.utils.KEY_MVP_BUNDLE
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import java.io.Serializable
import javax.inject.Inject

abstract class MvpDaggerAppCompatActivity<V : MvpView, B : Serializable, P : MvpPresenter<V, B>>
    : MvpActivity<V, P>(), MvpView, HasFragmentInjector, HasSupportFragmentInjector {

    val alerts: Alerts by lazy { Alerts(this) }

    @Inject
    lateinit var _supportFragmentInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    @Inject
    lateinit var _frameworkFragmentInjector: DispatchingAndroidInjector<android.app.Fragment>

    @Inject
    lateinit var injectedPresenter: P

    override fun createPresenter(): P = injectedPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        passBundleToPresenter()
        presenter?.onViewCreated()
    }

    override fun supportFragmentInjector(): AndroidInjector<androidx.fragment.app.Fragment>? {
        return _supportFragmentInjector
    }

    override fun fragmentInjector(): AndroidInjector<android.app.Fragment>? {
        return _frameworkFragmentInjector
    }

    private fun passBundleToPresenter() {
        val serializable = intent.extras?.getSerializable(KEY_MVP_BUNDLE) ?: NullBundle()
        presenter?.bundle = serializable as B
    }



}
