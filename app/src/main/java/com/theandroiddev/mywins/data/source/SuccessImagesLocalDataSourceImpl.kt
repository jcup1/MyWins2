package com.theandroiddev.mywins.data.source

import com.theandroiddev.mywins.local.dao.SuccessImageDao
import com.theandroiddev.mywins.data.model.SuccessImageEntity
import com.theandroiddev.mywins.data.model.toLocal
import com.theandroiddev.mywins.data.repository.SuccessImagesLocalDataSource
import com.theandroiddev.mywins.local.model.toEntity
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SuccessImagesLocalDataSourceImpl @Inject constructor(
        private val successImageDao: SuccessImageDao
): SuccessImagesLocalDataSource {

    override fun getSuccessImages(successId: Long): Single<List<SuccessImageEntity>> {
        return Single.fromCallable {
            successImageDao.getAll(successId).map { it.toEntity() }
        }.subscribeOn(Schedulers.io())
    }

    override fun editSuccessImages(successImages: List<SuccessImageEntity>, successId: Long): Completable {
        return Completable.fromAction {
            successImageDao.deleteSuccessImages(successId)
            successImageDao.insertAll(successImages.map { it.toLocal() })
        }.subscribeOn(Schedulers.io())
    }

    override fun removeAllSuccessImages(): Completable {
        return Completable.fromAction {
            successImageDao.removeAll()
        }
    }
}