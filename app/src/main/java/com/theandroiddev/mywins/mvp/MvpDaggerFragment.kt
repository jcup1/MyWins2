package com.theandroiddev.mywins.mvp

import android.content.Context
import android.support.v4.app.Fragment
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class MvpDaggerFragment<V : MvpView, P : MvpPresenter<V>>
    : MvpFragment<V, P>(), MvpView, HasSupportFragmentInjector {

    @Inject
    lateinit var _childFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var _injectedPresenter: P

    override fun createPresenter(): P = _injectedPresenter

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return _childFragmentInjector
    }

}
