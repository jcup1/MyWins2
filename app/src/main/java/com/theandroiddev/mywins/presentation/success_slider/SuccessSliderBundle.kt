package com.theandroiddev.mywins.presentation.success_slider

import com.theandroiddev.mywins.presentation.successes.SuccessModel
import com.theandroiddev.mywins.utils.MvpBundle
import com.theandroiddev.mywins.utils.SearchFilter

data class SuccessSliderBundle(val position: Int, val successes: MutableList<SuccessModel>) : MvpBundle()