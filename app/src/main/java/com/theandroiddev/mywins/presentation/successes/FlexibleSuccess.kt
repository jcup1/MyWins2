package com.theandroiddev.mywins.presentation.successes

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.utils.DrawableSelector
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.success_layout.view.*

class FlexibleSuccess(private val listener: OnItemClickListener,
                      private val drawableSelector: DrawableSelector
) : AbstractFlexibleItem<FlexibleSuccess.SuccessViewHolder>() {

    override fun getLayoutRes(): Int {
        return R.layout.success_layout
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>): SuccessViewHolder {
        return SuccessViewHolder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
                                holder: SuccessViewHolder,
                                position: Int, payloads: MutableList<Any>) {
        val successes = payloads as MutableList<SuccessModel>

        holder.titleTv.text = successes[position].title
        holder.categoryTv.text = holder.categoryTv.context.getString(successes[position].category.res)
        holder.dateStartedTv.text = successes[position].dateStarted
        holder.dateEndedTv.text = successes[position].dateEnded
        drawableSelector.selectCategoryImage(
                holder.categoryIv,
                successes[position].category,
                holder.categoryTv)
        drawableSelector.selectImportanceImage(
                holder.importanceIv,
                successes[position].importance)
        holder.repeatCountTv.text = successes[position].repeatCount.toString()

        holder.bind(successes[position], listener, position)
    }

    override fun equals(other: Any?): Boolean {
        TODO("Not yet implemented")
    }

    interface OnItemClickListener {
        fun onItemClick(success: SuccessModel,
                        position: Int,
                        titleTv: TextView,
                        categoryTv: TextView,
                        dateStartedTv: TextView,
                        dateEndedTv: TextView,
                        categoryIv: ImageView,
                        importanceIv: ImageView,
                        repeatCountTv: TextView,
                        constraintLayout: ConstraintLayout,
                        cardView: CardView)

        fun onLongItemClick(position: Int, cardView: CardView)
    }

    inner class SuccessViewHolder(itemView: View, adapter: FlexibleAdapter<*>) :
            FlexibleViewHolder(itemView, adapter) {
        var titleTv = itemView.item_title
        var categoryTv = itemView.item_category
        var dateStartedTv = itemView.item_date_started
        var dateEndedTv = itemView.item_date_ended
        var categoryIv = itemView.item_category_iv
        var importanceIv = itemView.item_importance_iv
        var repeatCountTv = itemView.item_repeat_count
        var constraintLayout = itemView.item_constraint_layout
        var cardView = itemView.item_card_view

        fun bind(success: SuccessModel, listener: OnItemClickListener, position: Int) {

            itemView.setOnClickListener {
                listener.onItemClick(success, position, titleTv, categoryTv, dateStartedTv, dateEndedTv,
                        categoryIv, importanceIv, repeatCountTv, constraintLayout, cardView)
            }

            itemView.setOnLongClickListener {
                listener.onLongItemClick(position, cardView)
                true
            }
        }
    }
}