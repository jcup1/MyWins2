package com.theandroiddev.mywins.data.source

import androidx.sqlite.db.SimpleSQLiteQuery
import com.theandroiddev.mywins.data.model.SuccessEntity
import com.theandroiddev.mywins.data.model.toLocal
import com.theandroiddev.mywins.data.repository.SuccessesLocalDataSource
import com.theandroiddev.mywins.local.dao.SuccessDao
import com.theandroiddev.mywins.local.model.toEntity
import com.theandroiddev.mywins.presentation.successes.Importance
import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.Constants.Companion.dummyCategories
import com.theandroiddev.mywins.utils.Constants.Companion.dummyDescriptions
import com.theandroiddev.mywins.utils.Constants.Companion.dummyEndDates
import com.theandroiddev.mywins.utils.Constants.Companion.dummyImportances
import com.theandroiddev.mywins.utils.Constants.Companion.dummyStartDates
import com.theandroiddev.mywins.utils.Constants.Companion.dummyTitles
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class SuccessesLocalDataSourceImpl @Inject constructor(
    private val successDao: SuccessDao
) : SuccessesLocalDataSource {

    override fun getDefaultSuccesses(): MutableList<SuccessEntity> {
        Constants()
        var i = 0
        val defaultSuccesses = mutableListOf<SuccessEntity>()
        while (i < 5) {
            defaultSuccesses.add(
                SuccessEntity(
                    null,
                    dummyTitles[i],
                    dummyCategories[i],
                    dummyDescriptions[i],
                    dummyStartDates[i],
                    dummyStartDates[i],
                    dummyEndDates[i],
                    Importance.values()[dummyImportances[i]]
                )
            )
            i++
        }
        return defaultSuccesses
    }

    override fun addSuccesses(successEntities: List<SuccessEntity>): Completable {
        return successDao.insertAll(successEntities.map { it.toLocal() })
    }

    override fun fetchSuccess(id: Long): Single<SuccessEntity> {
        return successDao.fetchSuccessById(id).map { localSuccess ->
            localSuccess.toEntity()
        }
    }

    override fun getSuccesses(
        searchTerm: String, sortType: String,
        isSortingAscending: Boolean
    ): Single<List<SuccessEntity>> {

        val order = if (isSortingAscending) {
            "ASC"
        } else {
            "DESC"
        }

        var queryString = "SELECT * FROM success WHERE title LIKE '%$searchTerm%'"
        queryString += " ORDER BY $sortType $order"

        val query = SimpleSQLiteQuery(queryString)

        return successDao.getAll(query).map { localSuccesses ->
            localSuccesses.map { localSuccess ->
                localSuccess.toEntity()
            }
        }
    }

    override fun editSuccess(successEntity: SuccessEntity): Completable {
        return successDao.update(successEntity.toLocal())
    }

    override fun removeSuccesses(successEntities: List<SuccessEntity>): Completable {
        return successDao.delete(successEntities.map { it.toLocal() })
    }

    override fun removeAllSuccesses(): Completable {
        //Don't return Completable from DAO because it still doesn't generate proper async function for table nuke
        return Completable.fromAction {
            successDao.removeAll()
        }
    }
}

fun <E> MutableList<E>.hasOne(): Boolean {
    return this.size == 1
}
