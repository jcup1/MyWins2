package com.theandroiddev.mywins.data.repository

import com.theandroiddev.mywins.data.model.SuccessImageEntity
import io.reactivex.Completable
import io.reactivex.Single

interface SuccessImagesLocalDataSource {

    fun getSuccessImages(successId: Long): Single<List<SuccessImageEntity>>

    fun editSuccessImages(successImages: List<SuccessImageEntity>, successId: Long): Completable

    fun removeAllSuccessImages(): Completable

}