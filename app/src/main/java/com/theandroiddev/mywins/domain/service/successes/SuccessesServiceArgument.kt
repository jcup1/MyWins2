package com.theandroiddev.mywins.domain.service.successes

import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.Constants.Companion.Category
import com.theandroiddev.mywins.utils.Constants.Companion.Importance
import java.io.Serializable

sealed class SuccessesServiceArgument {

    data class Successes(val successes: List<SuccessesServiceModel>) : SuccessesServiceArgument()

}