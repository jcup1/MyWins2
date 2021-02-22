package com.theandroiddev.mywins.data.successes.repository

import com.theandroiddev.mywins.data.successes.model.SuccessEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface SuccessesLocalDataSource {

    fun addSuccesses(successEntities: List<SuccessEntity>): Completable

    fun fetchSuccess(id: Long): Single<SuccessEntity>

    fun getSuccesses(searchTerm: String, sortType: String, isSortingAscending: Boolean):
            Flowable<List<SuccessEntity>>

    fun getDefaultSuccesses(): MutableList<SuccessEntity>

    fun editSuccess(successEntity: SuccessEntity): Completable

    fun removeSuccess(successEntities: List<SuccessEntity>): Completable

    fun removeAllSuccesses(): Completable
}