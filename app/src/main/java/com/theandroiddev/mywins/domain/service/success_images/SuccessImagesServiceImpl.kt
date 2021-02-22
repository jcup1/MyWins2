package com.theandroiddev.mywins.domain.service.success_images

import com.theandroiddev.mywins.data.successes.repository.SuccessImagesLocalDataSource
import com.theandroiddev.mywins.data.successes.model.toServiceModel
import com.theandroiddev.mywins.domain.service.common.InvalidArgumentException
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class SuccessImagesServiceImpl @Inject constructor(
        private val successImagesLocalDataSource: SuccessImagesLocalDataSource
) : SuccessImagesService {

    override fun getSuccessImages(id: Long): Single<SuccessImagesServiceResult> {
        return successImagesLocalDataSource.getSuccessImages(id).map { successImageEntities ->
            SuccessImagesServiceResult.SuccessImages(
                    successImageEntities.map { it.toServiceModel() }
            )
        }
    }

    override fun editSuccessImages(argument: SuccessImagesServiceArgument): Completable {

        return when (argument) {
            is SuccessImagesServiceArgument.SuccessImages -> {

                    if (argument.successId != null) {
                        successImagesLocalDataSource.editSuccessImages(
                                argument.successImages.map { it.toEntity() },
                                argument.successId
                        )
                    } else {
                        Completable.error(InvalidArgumentException())
                    }
            }
        }
    }

    override fun removeAllSuccessImages(): Completable {
        return successImagesLocalDataSource.removeAllSuccessImages()
    }
}