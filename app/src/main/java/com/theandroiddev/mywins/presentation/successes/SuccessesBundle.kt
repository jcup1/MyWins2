package com.theandroiddev.mywins.presentation.successes

import com.theandroiddev.mywins.utils.MvpBundle

data class SuccessesBundle(val position: Int, val successes: MutableList<SuccessModel>) : MvpBundle()