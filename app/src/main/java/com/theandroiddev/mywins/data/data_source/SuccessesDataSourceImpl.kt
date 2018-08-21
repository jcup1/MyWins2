package com.theandroiddev.mywins.data.data_source

import com.theandroiddev.mywins.data.dao.SuccessDao
import com.theandroiddev.mywins.data.dao.SuccessImageDao
import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.data.entity.SuccessImageEntity
import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.Constants.Companion.dummyCategory
import com.theandroiddev.mywins.utils.Constants.Companion.dummyDescription
import com.theandroiddev.mywins.utils.Constants.Companion.dummyEndDate
import com.theandroiddev.mywins.utils.Constants.Companion.dummyImportance
import com.theandroiddev.mywins.utils.Constants.Companion.dummyStartDate
import com.theandroiddev.mywins.utils.Constants.Companion.dummyTitle
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jakub on 14.08.17.
 */

interface SuccessesDataSource {

    fun addSuccesses(successes: MutableList<SuccessEntity>): Completable

    fun fetchSuccess(id: Long): Single<SuccessEntity>

    fun getSuccesses(searchTerm: String, sortType: String, isSortingAscending: Boolean):
            Flowable<MutableList<SuccessEntity>>

    fun getSuccessImages(successId: Long): Flowable<MutableList<SuccessImageEntity>>

    fun getDefaultSuccesses(): MutableList<SuccessEntity>

    fun editSuccess(showSuccess: SuccessEntity): Completable

    fun editSuccessImages(successImages: MutableList<SuccessImageEntity>, successId: Long): Completable

    fun removeSuccess(successesToRemove: MutableList<SuccessEntity>): Completable

    fun clearDatabase(): Completable
}

class SuccessesDataSourceImpl @Inject constructor(
        private val successDao: SuccessDao,
        private val successImageDao: SuccessImageDao
) : SuccessesDataSource {

    override fun getDefaultSuccesses(): MutableList<SuccessEntity> {
        Constants()
        var i = 0
        val defaultSuccesses = mutableListOf<SuccessEntity>()
        while (i < 5) {
            defaultSuccesses.add(SuccessEntity(null, dummyTitle[i], dummyCategory[i], dummyDescription[i],
                    dummyStartDate[i], dummyEndDate[i], dummyImportance[i]))
            i++
        }
        return defaultSuccesses
    }

    override fun addSuccesses(successes: MutableList<SuccessEntity>): Completable {
        return Completable.fromCallable {
            successDao.insertAll(successes)
        }
    }

    override fun fetchSuccess(id: Long): Single<SuccessEntity> {
        return Single.fromCallable { successDao.fetchSuccessById(id) }.subscribeOn(Schedulers.io())
    }

    override fun getSuccesses(searchTerm: String, sortType: String,
                              isSortingAscending: Boolean): Flowable<MutableList<SuccessEntity>> {

        return if (isSortingAscending) {
            Flowable.fromCallable {
                successDao.getAllASC("%$searchTerm%", sortType)
            }.subscribeOn(Schedulers.io())
        } else {
            Flowable.fromCallable {
                successDao.getAllDESC("%$searchTerm%", sortType)
            }.subscribeOn(Schedulers.io())
        }
    }

    override fun getSuccessImages(successId: Long): Flowable<MutableList<SuccessImageEntity>> {
        return Flowable.fromCallable {
            successImageDao.getAll(successId)
        }.subscribeOn(Schedulers.io())
    }

    override fun editSuccess(showSuccess: SuccessEntity): Completable {
        return Completable.fromCallable {
            successDao.update(showSuccess)
        }.subscribeOn(Schedulers.io())
    }

    override fun editSuccessImages(successImages: MutableList<SuccessImageEntity>, successId: Long): Completable {
        return Completable.fromCallable {
            successImageDao.deleteSuccessImages(successId)
            successImageDao.insertAll(successImages)
        }
    }

    override fun removeSuccess(successesToRemove: MutableList<SuccessEntity>): Completable {
        return Completable.fromCallable {
            for (success in successesToRemove) {
                successDao.delete(success)

            }
        }

    }

    override fun clearDatabase(): Completable {
        return Completable.fromCallable {
            successDao.removeAll()
            successImageDao.removeAll()
        }
    }

}
