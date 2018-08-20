package com.theandroiddev.mywins.presentation.success_slider

import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.data.entity.SuccessImageEntity
import com.theandroiddev.mywins.domain.service.successes.SuccessesService
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by jakub on 12.11.17.
 */

class SuccessImageLoaderImpl @Inject constructor(
        private var successesService: SuccessesService
) : SuccessImageLoader {

    override fun getSuccessImages(id: Long): Flowable<MutableList<SuccessImageEntity>> {
        return successesService.getSuccessImages(id)
    }

    override fun fetchSuccess(id: Long): Single<SuccessEntity> {
        return successesService.fetchSuccess(id)
    }

}
