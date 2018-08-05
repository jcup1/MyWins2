package com.theandroiddev.mywins.success_slider

import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.models.SuccessImage
import com.theandroiddev.mywins.data.repositories.SuccessesRepository
import java.util.*
import javax.inject.Inject

/**
 * Created by jakub on 12.11.17.
 */

class SuccessImageLoaderImpl @Inject constructor(
        private var successesRepository: SuccessesRepository
) : SuccessImageLoader {

    override fun getSuccessImages(id: String): ArrayList<SuccessImage> {
        return successesRepository.getSuccessImages(id)
    }

    override fun getSuccess(id: String): Success {
        return successesRepository.getSuccess(id)
    }

    override fun openDB() {
        successesRepository.openDB()
    }

    override fun closeDB() {
        successesRepository.closeDB()
    }
}
