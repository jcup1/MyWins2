package com.theandroiddev.mywins.domain.service.successes

import com.theandroiddev.mywins.data.model.toServiceModel
import com.theandroiddev.mywins.data.repository.SuccessesLocalDataSource
import com.theandroiddev.mywins.domain.service.common.InvalidArgumentException
import com.theandroiddev.mywins.domain.service.shared_preferences.SharedPreferencesService
import com.theandroiddev.mywins.presentation.edit_success.hasOne
import com.theandroiddev.mywins.presentation.successes.SortType
import com.theandroiddev.mywins.utils.SearchFilter
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class SuccessesServiceImpl @Inject constructor(
    private var successesLocalDataSource: SuccessesLocalDataSource,
    private var sharedPreferencesService: SharedPreferencesService
) : SuccessesService {

    override fun saveSuccesses(argument: SuccessesServiceArgument): Completable {

        when (argument) {
            is SuccessesServiceArgument.Successes -> {
                val successEntities = argument.successes.map { it.toEntity() }
                return successesLocalDataSource.addSuccesses(successEntities)
            }
        }
    }

    override fun fetchSuccess(id: Long): Single<SuccessesServiceResult> {
        return successesLocalDataSource.fetchSuccess(id).map { successEntity ->
            SuccessesServiceResult.Successes(mutableListOf(successEntity.toServiceModel()))
        }
    }

    override fun getSuccesses(searchTerm: String?, searchFilter: SearchFilter): Single<SuccessesServiceResult> {
        return successesLocalDataSource.getSuccesses(
            searchTerm.orEmpty(),
            searchFilter.sortType?.name?.toLowerCase() ?: SortType.TITLE.name.toLowerCase(),
            searchFilter.isSortingAscending
        ).map { successEntities ->
            SuccessesServiceResult.Successes(successEntities.map { it.toServiceModel() })
        }
    }

    override fun getDefaultSuccesses(): SuccessesServiceResult {
        return SuccessesServiceResult.Successes(
            successesLocalDataSource.getDefaultSuccesses().map { successEntity ->
                successEntity.toServiceModel()
            }
        )
    }

    override fun editSuccess(argument: SuccessesServiceArgument): Completable {
//TODO implement better error handling
        return when (argument) {
            is SuccessesServiceArgument.Successes -> {
                if (argument.successes.hasOne()) {
                    successesLocalDataSource.editSuccess(argument.successes.first().toEntity())
                } else {
                    Completable.error(InvalidArgumentException())
                }
            }
        }
    }

    override fun removeSuccesses(argument: SuccessesServiceArgument): Completable {

        when (argument) {
            is SuccessesServiceArgument.Successes -> {
                val successes = argument.successes.map { it.toEntity() }
                return successesLocalDataSource.removeSuccesses(successes)
            }
        }
    }

    override fun removeAllSuccesses(): Completable {
        return successesLocalDataSource.removeAllSuccesses()
    }

    override fun saveFilters(
        newCustomization: SearchFilter,
        oldCustomization: SearchFilter
    ) {
        if (areFiltersEqual(newCustomization, oldCustomization)) {
            return
        }
        sharedPreferencesService.saveSuccessesFilters(newCustomization)
    }

    override fun getFilters(): SearchFilter {
        return sharedPreferencesService.getSuccessesFilters()
    }

    private fun areFiltersEqual(
        newCustomization: SearchFilter,
        oldCustomization: SearchFilter
    ): Boolean {
        val isSortingOrderTheSame = newCustomization.isSortingAscending == oldCustomization.isSortingAscending
        val isSortTypeTheSame = newCustomization.sortType == oldCustomization.sortType
        if (isSortingOrderTheSame && isSortTypeTheSame) {
            return true
        }
        return false
    }
}
