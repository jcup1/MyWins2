package com.theandroiddev.mywins.presentation.insert_success

import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.Constants.Companion.Category
import com.theandroiddev.mywins.utils.MvpBundle
import java.io.Serializable

data class InsertSuccessBundle(val category: Category) : MvpBundle()