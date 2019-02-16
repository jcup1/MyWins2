package com.theandroiddev.mywins.domain.service.successes

sealed class SuccessesServiceArgument {

    data class Successes(val successes: List<SuccessesServiceModel>) : SuccessesServiceArgument()

}