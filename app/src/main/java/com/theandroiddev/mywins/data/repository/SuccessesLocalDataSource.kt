package com.theandroiddev.mywins.data.repository

import com.theandroiddev.mywins.data.model.SuccessEntity
import io.reactivex.Completable
import io.reactivex.Single

interface SuccessesLocalDataSource {

    fun addSuccesses(successEntities: List<SuccessEntity>): Completable

    fun fetchSuccess(id: Long): Single<SuccessEntity>

    fun getSuccesses(searchTerm: String, sortType: String, isSortingAscending: Boolean):
            Single<List<SuccessEntity>>

    fun getDefaultSuccesses(): MutableList<SuccessEntity>


    fun editSuccess(successEntity: SuccessEntity): Completable

    fun removeSuccesses(successEntities: List<SuccessEntity>): Completable

    fun removeAllSuccesses(): Completable
}