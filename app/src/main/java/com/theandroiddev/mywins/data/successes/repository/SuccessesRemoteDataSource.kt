package com.theandroiddev.mywins.data.successes.repository

import com.theandroiddev.mywins.data.successes.model.SuccessEntity
import io.reactivex.Single

interface SuccessesRemoteDataSource {

    fun getSuccesses(): Single<List<SuccessEntity>>
}