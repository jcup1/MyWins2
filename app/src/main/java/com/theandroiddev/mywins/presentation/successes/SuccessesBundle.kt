package com.theandroiddev.mywins.presentation.successes

import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.utils.MvpBundle

data class SuccessesBundle(val position: Int, val successes: MutableList<SuccessEntity>) : MvpBundle()