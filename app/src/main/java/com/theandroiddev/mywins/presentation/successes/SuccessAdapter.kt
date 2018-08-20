package com.theandroiddev.mywins.presentation.successes

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.utils.DrawableSelector
import kotlinx.android.synthetic.main.success_layout.view.*
import javax.inject.Inject

/**
 * Created by jakub on 07.08.17.
 */

class SuccessAdapter @Inject constructor(
        private val itemLayout: Int,
        private val listener: OnItemClickListener,
        private val drawableSelector: DrawableSelector) : RecyclerView.Adapter<SuccessAdapter.ViewHolder>() {

    var successes = mutableListOf<SuccessEntity>()
    var successesToRemove = mutableListOf<SuccessEntity>()
    var backupSuccess: SuccessEntity? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.titleTv.text = successes.get(position).title
        holder.categoryTv.text = successes.get(position).category
        holder.dateStartedTv.text = successes.get(position).dateStarted
        holder.dateEndedTv.text = successes.get(position).dateEnded
        drawableSelector.selectCategoryImage(holder.categoryIv, successes.get(position).category, holder.categoryTv)
        drawableSelector.selectImportanceImage(holder.importanceIv, successes.get(position).importance)


        holder.bind(successes.get(position), listener, position)

    }

    override fun getItemCount(): Int {
        return successes.size
    }

    fun updateSuccessList(successList: MutableList<SuccessEntity>) {
        this.successes = successList
        notifyDataSetChanged()
    }


    interface OnItemClickListener {
        fun onItemClick(success: SuccessEntity,
                        position: Int,
                        titleTv: TextView,
                        categoryTv: TextView,
                        dateStartedTv: TextView,
                        dateEndedTv: TextView,
                        categoryIv: ImageView,
                        importanceIv: ImageView,
                        constraintLayout: ConstraintLayout,
                        cardView: CardView)

        fun onLongItemClick(position: Int, cardView: CardView)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var titleTv = itemView.item_title
        var categoryTv = itemView.item_category
        var dateStartedTv = itemView.item_date_started
        var dateEndedTv = itemView.item_date_ended
        var categoryIv = itemView.item_category_iv
        var importanceIv = itemView.item_importance_iv
        var constraintLayout = itemView.item_constraint_layout
        var cardView = itemView.item_card_view

        fun bind(success: SuccessEntity, listener: OnItemClickListener, position: Int) {

            itemView.setOnClickListener {
                listener.onItemClick(success, position, titleTv, categoryTv, dateStartedTv, dateEndedTv,
                        categoryIv, importanceIv, constraintLayout, cardView)
            }

            itemView.setOnLongClickListener {
                listener.onLongItemClick(position, cardView)
                true
            }
        }

    }


}
