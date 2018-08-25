package com.theandroiddev.mywins.data.repository

import com.theandroiddev.mywins.data.model.SuccessEntity
import io.reactivex.Single

interface SuccessesRemoteDataSource {

    fun getSuccesses(): Single<List<SuccessEntity>>
}