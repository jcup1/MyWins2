package com.theandroiddev.mywins.data.model

sealed class SuccessEntityResult

data class Success(val successEntities: MutableList<SuccessEntity>) : SuccessEntityResult()

data class Error(val error: Throwable?) : SuccessEntityResult()