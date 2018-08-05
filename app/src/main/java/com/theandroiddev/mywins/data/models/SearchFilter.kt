package com.theandroiddev.mywins.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by jakub on 12.11.17.
 */
@Parcelize
data class SearchFilter(
        var searchTerm: String? = null,
        var sortType: String? = null,
        var isSortingAscending: Boolean = false
) : Parcelable
