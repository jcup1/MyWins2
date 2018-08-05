package com.theandroiddev.mywins.success_slider

import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.data.models.SuccessImage
import java.util.*

interface SuccessImageLoader {

    abstract fun getSuccessImages(id: String): ArrayList<SuccessImage>

    abstract fun getSuccess(id: String): Success

    abstract fun openDB()

    abstract fun closeDB()
}