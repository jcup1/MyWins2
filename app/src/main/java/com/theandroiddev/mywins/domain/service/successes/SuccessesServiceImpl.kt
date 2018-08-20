package com.theandroiddev.mywins.domain.service.successes

import com.theandroiddev.mywins.data.data_source.SuccessesDataSourceImpl
import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.data.entity.SuccessImageEntity
import com.theandroiddev.mywins.utils.SearchFilter
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by jakub on 04.11.17.
 */

class SuccessesServiceImpl @Inject constructor(
        private var successesDataSourceImpl: SuccessesDataSourceImpl) : SuccessesService {

    override fun addSuccess(s: SuccessEntity): Completable {
        return successesDataSourceImpl.addSuccesses(mutableListOf(s))
    }

    override fun saveSuccesses(defaultSuccesses: MutableList<SuccessEntity>): Completable {
        return successesDataSourceImpl.addSuccesses(defaultSuccesses)
    }

    override fun fetchSuccess(id: Long): Single<SuccessEntity> {
        return successesDataSourceImpl.fetchSuccess(id)
    }

    override fun getSuccesses(searchFilter: SearchFilter): Flowable<MutableList<SuccessEntity>> {
        return successesDataSourceImpl.getSuccesses(searchFilter.searchTerm.orEmpty(),
                searchFilter.sortType.orEmpty(), searchFilter.isSortingAscending)
                .subscribeOn(Schedulers.io())
    }

    override fun getSuccessImages(id: Long): Flowable<MutableList<SuccessImageEntity>> {
        return successesDataSourceImpl.getSuccessImages(id)
    }

    override fun getDefaultSuccesses(): MutableList<SuccessEntity> {
        return successesDataSourceImpl.getDefaultSuccesses()
    }

    override fun updateForDeleteSuccess(
            successToRemoveList: MutableList<SuccessEntity>): Completable {
        return successesDataSourceImpl.removeSuccess(successToRemoveList)
    }

    override fun editSuccess(editSuccess: SuccessEntity): Completable {
        return successesDataSourceImpl.editSuccess(editSuccess)
    }

    override fun editSuccessImages(successImageList: MutableList<SuccessImageEntity>,
                                   successId: Long): Completable {
        return successesDataSourceImpl.editSuccessImages(successImageList, successId)
    }

    override fun removeSuccesses(successToRemoveList: MutableList<SuccessEntity>): Completable {
        return successesDataSourceImpl.removeSuccess(successToRemoveList)
    }

    override fun clearDatabase(): Completable {
        return successesDataSourceImpl.clearDatabase()
    }

}
