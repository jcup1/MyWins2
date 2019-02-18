package com.theandroiddev.mywins.domain.service.successes

sealed class SuccessesServiceResult {

    data class Successes(val successes: List<SuccessesServiceModel>) : SuccessesServiceResult()

    class Error : SuccessesServiceResult()
}
