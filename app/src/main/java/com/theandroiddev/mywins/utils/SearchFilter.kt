package com.theandroiddev.mywins.utils

import android.os.Parcelable
import com.theandroiddev.mywins.utils.Constants.Companion.SortType
import kotlinx.android.parcel.Parcelize

/**
 * Created by jakub on 12.11.17.
 */
@Parcelize
data class SearchFilter(
        var searchTerm: String? = "",
        var sortType: SortType? = SortType.DATE_ADDED,
        var isSortingAscending: Boolean = true
) : Parcelable
