package com.theandroiddev.mywins.data.successes.data_source.success_images

import com.theandroiddev.mywins.data.successes.model.SuccessImageEntity
import io.reactivex.Completable
import io.reactivex.Single

interface SuccessImagesDataSource {

    fun getSuccessImages(successId: Long): Single<MutableList<SuccessImageEntity>>

    fun editSuccessImages(successImages: List<SuccessImageEntity>, successId: Long): Completable

    fun removeAllSuccessImages(): Completable

}