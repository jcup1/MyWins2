package com.theandroiddev.mywins.data.source

import com.theandroiddev.mywins.local.dao.SuccessDao
import com.theandroiddev.mywins.data.model.SuccessEntity
import com.theandroiddev.mywins.data.model.toLocal
import com.theandroiddev.mywins.data.repository.SuccessesLocalDataSource
import com.theandroiddev.mywins.local.model.toEntity
import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.Constants.Companion.dummyCategories
import com.theandroiddev.mywins.utils.Constants.Companion.dummyDescriptions
import com.theandroiddev.mywins.utils.Constants.Companion.dummyEndDates
import com.theandroiddev.mywins.utils.Constants.Companion.dummyImportances
import com.theandroiddev.mywins.utils.Constants.Companion.dummyStartDates
import com.theandroiddev.mywins.utils.Constants.Companion.dummyTitles
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SuccessesLocalDataSourceImpl @Inject constructor(
        private val successDao: SuccessDao
) : SuccessesLocalDataSource {

    override fun getDefaultSuccesses(): MutableList<SuccessEntity> {
        Constants()
        var i = 0
        val defaultSuccesses = mutableListOf<SuccessEntity>()
        while (i < 5) {
            defaultSuccesses.add(SuccessEntity(null, dummyTitles[i], dummyCategories[i], dummyDescriptions[i],
                    dummyStartDates[i], dummyEndDates[i], Constants.Companion.Importance.values()[dummyImportances[i]]))
            i++
        }
        return defaultSuccesses
    }

    override fun addSuccesses(successEntities: List<SuccessEntity>): Completable {
        return Completable.fromAction {
            successDao.insertAll(successEntities.map { it.toLocal() })
        }

    }

    override fun fetchSuccess(id: Long): Single<SuccessEntity> {
        return Single.fromCallable {
            successDao.fetchSuccessById(id).toEntity()
        }.subscribeOn(Schedulers.io())
    }

    override fun getSuccesses(searchTerm: String, sortType: String,
                              isSortingAscending: Boolean): Flowable<List<SuccessEntity>> {

        return if (isSortingAscending) {
            Flowable.fromCallable {
                successDao.getAllASC("%$searchTerm%", sortType).map { localSuccess ->
                    localSuccess.toEntity()
                }
            }.subscribeOn(Schedulers.io())
        } else {
            Flowable.fromCallable {
                successDao.getAllDESC("%$searchTerm%", sortType).map { localSuccess ->
                    localSuccess.toEntity()
                }
            }.subscribeOn(Schedulers.io())
        }
    }

    override fun editSuccess(successEntity: SuccessEntity): Completable {
        return Completable.fromAction {
            successDao.update(successEntity.toLocal())
        }.subscribeOn(Schedulers.io())
    }

    override fun removeSuccess(successEntity: SuccessEntity): Completable {
        return Completable.fromAction {
            successDao.delete(successEntity.toLocal())
        }

    }

    override fun removeAllSuccesses(): Completable {
        return Completable.fromAction {
            successDao.removeAll()
        }
    }

}

fun <E> MutableList<E>.hasOne(): Boolean {
    return this.size == 1
}
