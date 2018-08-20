package com.theandroiddev.mywins.presentation.success_slider

import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.data.entity.SuccessImageEntity
import io.reactivex.Flowable
import io.reactivex.Single

interface SuccessImageLoader {

    fun getSuccessImages(id: Long): Flowable<MutableList<SuccessImageEntity>>

    fun fetchSuccess(id: Long): Single<SuccessEntity>

}