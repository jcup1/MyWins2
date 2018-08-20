package com.theandroiddev.mywins.domain.service.successes

import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.data.entity.SuccessImageEntity
import com.theandroiddev.mywins.utils.SearchFilter
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by jakub on 28.10.17.
 */

interface SuccessesService {

    fun getDefaultSuccesses(): MutableList<SuccessEntity>

    fun updateForDeleteSuccess(successToRemoveList: MutableList<SuccessEntity>): Completable

    fun getSuccesses(searchFilter: SearchFilter): Flowable<MutableList<SuccessEntity>>

    fun removeSuccesses(successToRemoveList: MutableList<SuccessEntity>): Completable

    fun fetchSuccess(id: Long): Single<SuccessEntity>

    fun addSuccess(s: SuccessEntity): Completable

    fun getSuccessImages(id: Long): Flowable<MutableList<SuccessImageEntity>>

    fun editSuccess(editSuccess: SuccessEntity): Completable

    fun editSuccessImages(successImageList: MutableList<SuccessImageEntity>, successId: Long): Completable

    fun saveSuccesses(defaultSuccesses: MutableList<SuccessEntity>): Completable

    fun clearDatabase(): Completable
}
