package com.theandroiddev.mywins.presentation.successes.filters

import androidx.fragment.app.DialogFragment
import com.theandroiddev.mywins.utils.SearchFilter

interface SuccessesFiltersDialogListener {
    fun onCancel(dialogFragment: DialogFragment) {
        dialogFragment.dismiss()
    }

    fun onSaveFilters(
        dialogFragment: DialogFragment,
        filtersModel: SearchFilter
    ) {
        dialogFragment.dismiss()
    }
}