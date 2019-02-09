package com.theandroiddev.mywins.domain.service.successes

import com.theandroiddev.mywins.utils.SearchFilter
import io.reactivex.Completable
import io.reactivex.Single

interface SuccessesService {

    fun getDefaultSuccesses(): SuccessesServiceResult

    fun getSuccesses(searchFilter: SearchFilter): Single<SuccessesServiceResult>

    fun removeSuccesses(argument: SuccessesServiceArgument): Completable

    fun fetchSuccess(id: Long): Single<SuccessesServiceResult>

    fun editSuccess(argument: SuccessesServiceArgument): Completable

    fun saveSuccesses(argument: SuccessesServiceArgument): Completable

    fun removeAllSuccesses(): Completable
}
