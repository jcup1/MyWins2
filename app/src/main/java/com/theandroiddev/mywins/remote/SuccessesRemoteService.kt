package com.theandroiddev.mywins.remote

import com.theandroiddev.mywins.remote.model.SuccessRemoteModel
import io.reactivex.Single
import retrofit2.http.GET

interface SuccessesRemoteService {

    @GET("successes.json")
    fun getSuccesses(): Single<SuccessResponse>

    class SuccessResponse {
        lateinit var successes: List<SuccessRemoteModel>
    }
}