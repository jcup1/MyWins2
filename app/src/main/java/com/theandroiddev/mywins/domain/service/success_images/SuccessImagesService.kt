package com.theandroiddev.mywins.domain.service.success_images

import io.reactivex.Completable
import io.reactivex.Single

interface SuccessImagesService {

    fun getSuccessImages(id: Long): Single<SuccessImagesServiceResult>

    fun editSuccessImages(argument: SuccessImagesServiceArgument):
            Completable

    fun removeAllSuccessImages(): Completable

}