package com.theandroiddev.mywins.utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by jakub on 12.11.17.
 */
@Parcelize
data class SearchFilter(
        var searchTerm: String? = "",
        var sortType: String? = "title",
        var isSortingAscending: Boolean = false
) : Parcelable
