package com.theandroiddev.mywins.remote

import com.theandroiddev.mywins.data.successes.model.SuccessEntity
import com.theandroiddev.mywins.data.successes.repository.SuccessesRemoteDataSource
import com.theandroiddev.mywins.remote.model.toEntity
import io.reactivex.Single
import javax.inject.Inject

class SuccessesRemoteImpl @Inject constructor(
        private val successesRemoteService: SuccessesRemoteService
) : SuccessesRemoteDataSource {
    override fun getSuccesses(): Single<List<SuccessEntity>> {
        return successesRemoteService.getSuccesses()
                .map { successResponse ->
                    successResponse.successes.map { successRemoteModel ->
                        successRemoteModel.toEntity()
                    }
                }
    }


}