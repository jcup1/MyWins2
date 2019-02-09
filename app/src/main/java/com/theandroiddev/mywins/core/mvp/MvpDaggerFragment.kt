package com.theandroiddev.mywins.core.mvp

import android.content.Context
import android.os.Bundle
import android.view.View
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import com.theandroiddev.mywins.utils.Alerts
import com.theandroiddev.mywins.utils.KEY_MVP_BUNDLE
import com.theandroiddev.mywins.utils.MvpBundle
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import java.io.Serializable
import javax.inject.Inject

abstract class MvpDaggerFragment<V : MvpView, B: Serializable, P : MvpPresenter<V, B>>
    : MvpFragment<V, P>(), MvpView, HasSupportFragmentInjector {

    override val alerts: Alerts? by lazy { activity?.let { Alerts(it) } }

    @Inject
    lateinit var _childFragmentInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    @Inject
    lateinit var _injectedPresenter: P

    override fun createPresenter(): P = _injectedPresenter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passBundleToPresenter()
        presenter?.onViewCreated()
    }

    override fun supportFragmentInjector(): AndroidInjector<androidx.fragment.app.Fragment>? {
        return _childFragmentInjector
    }

    fun setSerializableArgument(serializable: B): androidx.fragment.app.Fragment {
        val bundle = Bundle()
        bundle.putSerializable(KEY_MVP_BUNDLE, serializable)
        this.arguments = bundle
        return this
    }

    private fun passBundleToPresenter() {
        val serializable =
                arguments?.getSerializable(KEY_MVP_BUNDLE) ?: MvpBundle()
        presenter?.bundle = serializable as B
    }

    override fun finish() {
        this.activity?.finish()
    }

}
