package com.theandroiddev.mywins.presentation.successes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.theandroiddev.mywins.utils.DrawableSelector
import kotlinx.android.synthetic.main.success_layout.view.*
import javax.inject.Inject

/**
 * Created by jakub on 07.08.17.
 */

class SuccessAdapter @Inject constructor(
    private val itemLayout: Int,
    private val listener: OnItemClickListener,
    private val drawableSelector: DrawableSelector
) : androidx.recyclerview.widget.RecyclerView.Adapter<SuccessAdapter.ViewHolder>() {

    var successes = mutableListOf<SuccessModel>()
    var successesToRemove = mutableListOf<SuccessModel>()
    var backupSuccess: SuccessModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.titleTv.text = successes[position].title
        holder.categoryTv.text =
            holder.categoryTv.context.getString(successes[position].category.res)
        holder.dateStartedTv.text = successes[position].dateStarted
        holder.dateEndedTv.text = successes[position].dateEnded
        drawableSelector.selectCategoryImage(
            holder.categoryIv,
            successes[position].category,
            holder.categoryTv
        )
        drawableSelector.selectImportanceImage(
            holder.importanceIv,
            successes[position].importance
        )

        holder.bind(successes[position], listener, position)
    }

    override fun getItemCount(): Int {
        return successes.size
    }

    fun updateSuccessList(successList: List<SuccessModel>) {
        this.successes = successList.toMutableList()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(
            success: SuccessModel,
            position: Int,
            titleTv: TextView,
            categoryTv: TextView,
            dateStartedTv: TextView,
            dateEndedTv: TextView,
            categoryIv: ImageView,
            importanceIv: ImageView,
            constraintLayout: ConstraintLayout,
            cardView: CardView
        )

        fun onLongItemClick(position: Int, cardView: CardView)
    }

    class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var titleTv = itemView.item_title
        var categoryTv = itemView.item_category
        var dateStartedTv = itemView.item_date_started
        var dateEndedTv = itemView.item_date_ended
        var categoryIv = itemView.item_category_iv
        var importanceIv = itemView.item_importance_iv
        var constraintLayout = itemView.item_constraint_layout
        var cardView = itemView.item_card_view

        fun bind(success: SuccessModel, listener: OnItemClickListener, position: Int) {

            itemView.setOnClickListener {
                listener.onItemClick(
                    success, position, titleTv, categoryTv, dateStartedTv, dateEndedTv,
                    categoryIv, importanceIv, constraintLayout, cardView
                )
            }

            itemView.setOnLongClickListener {
                listener.onLongItemClick(position, cardView)
                true
            }
        }
    }
}
