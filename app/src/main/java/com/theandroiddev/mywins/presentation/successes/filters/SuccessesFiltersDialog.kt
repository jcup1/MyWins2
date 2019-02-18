package com.theandroiddev.mywins.presentation.successes.filters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.presentation.successes.SortType.*
import com.theandroiddev.mywins.utils.SearchFilter
import kotlinx.android.synthetic.main.fragment_success_filters.*

class SuccessesFiltersDialog : DialogFragment() {

    private var filtersModel: SearchFilter? = null
    private var listener: SuccessesFiltersDialogListener? = null

    companion object {

        private const val SUCCESSES_FILTERS_MODEL_BUNDLE_KEY = "SUCCESSES_FILTERS_MODEL_BUNDLE_KEY"

        fun create(model: SearchFilter): SuccessesFiltersDialog {
            val issuesCustomizationDialog = SuccessesFiltersDialog()
            val bundle = Bundle()
            bundle.putSerializable(SUCCESSES_FILTERS_MODEL_BUNDLE_KEY, model)
            issuesCustomizationDialog.arguments = bundle
            return issuesCustomizationDialog
        }

        fun show(
            activity: FragmentActivity,
            model: SearchFilter
        ): SuccessesFiltersDialog {
            val dialog = SuccessesFiltersDialog.create(model)
            dialog.show(
                activity.supportFragmentManager,
                SuccessesFiltersDialog::class.java.simpleName
            )
            return dialog
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? SuccessesFiltersDialogListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_success_filters, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filtersModel = arguments?.getSerializable(SUCCESSES_FILTERS_MODEL_BUNDLE_KEY) as? SearchFilter

        successes_filters_ascending_order_switch.isChecked = filtersModel?.isSortingAscending ?: false

        when (filtersModel?.sortType) {
            TITLE -> successes_filters_sort_by_title_radio_button.isChecked = true
            IMPORTANCE -> successes_filters_sort_by_importance_radio_button.isChecked = true
            DATE_ADDED -> successes_filters_sort_by_creation_date_radio_button.isChecked = true
            else -> successes_filters_sort_by_creation_date_radio_button.isChecked = true
        }

        successes_filters_save_button.setOnClickListener {
            listener?.onSaveFilters(
                this,
                generateNewCustomizationModel()
            )
        }
        successes_filters_cancel_button.setOnClickListener { listener?.onCancel(this) }
    }

    private fun generateNewCustomizationModel(): SearchFilter {
        val sortType = when (successes_filters_sort_by_title_radio_group.checkedRadioButtonId) {
            R.id.successes_filters_sort_by_title_radio_button -> TITLE
            R.id.successes_filters_sort_by_importance_radio_button -> IMPORTANCE
            R.id.successes_filters_sort_by_creation_date_radio_button -> DATE_ADDED
            else -> DATE_ADDED
        }
        return SearchFilter(
            sortType,
            successes_filters_ascending_order_switch.isChecked
        )
    }
}