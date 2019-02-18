package com.theandroiddev.mywins.utils

import android.os.Parcelable
import com.theandroiddev.mywins.presentation.successes.SortType
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * Created by jakub on 12.11.17.
 */
@Parcelize
data class SearchFilter(
        var sortType: SortType? = SortType.DATE_ADDED,
        var isSortingAscending: Boolean = true
) : Parcelable, Serializable
