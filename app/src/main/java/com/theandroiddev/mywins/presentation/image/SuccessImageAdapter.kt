package com.theandroiddev.mywins.presentation.image

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.presentation.image.SuccessImageAdapter.ViewHolder
import com.theandroiddev.mywins.presentation.successes.SuccessImageModel
import java.io.File

class SuccessImageAdapter(
    private val listener: OnSuccessImageClickListener,
    private val successImageLayout: Int
) : androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {

    var successImages: MutableList<SuccessImageModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(successImageLayout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (successImages[position].imagePath.isEmpty()) {
            if (position == 0) {
                holder.successImageIv.setImageResource(R.drawable.ic_action_add)
                //holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.primary));
                holder.bind(successImages[position], position)

            }
        } else {

            Picasso.get().load(File(successImages[position].imagePath)).resize(256, 256).centerCrop().into(holder.successImageIv)
            holder.bind(successImages[position], position)

        }

    }

    override fun getItemCount(): Int {
        return successImages.size
    }

    interface OnSuccessImageClickListener {
        fun onSuccessImageClick(successImage: SuccessImageModel, successImageIv: ImageView, position: Int, constraintLayout: ConstraintLayout,
                                cardView: CardView
        )

        fun onSuccessImageLongClick(successImage: SuccessImageModel, successImageIv: ImageView, position: Int, constraintLayout: ConstraintLayout,
                                    cardView: CardView)
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var successImageIv: ImageView
        var constraintLayout: ConstraintLayout
        var cardView: CardView


        init {

            successImageIv = itemView.findViewById(R.id.success_image_iv)
            constraintLayout = itemView.findViewById(R.id.success_image_constraint_layout)
            cardView = itemView.findViewById(R.id.success_image_card_view)

        }

        fun bind(successImage: SuccessImageModel, position: Int) {

            itemView.setOnClickListener { listener.onSuccessImageClick(successImage, successImageIv, position, constraintLayout, cardView) }
            itemView.setOnLongClickListener {
                listener.onSuccessImageLongClick(successImage, successImageIv, position, constraintLayout, cardView)
                true
            }
        }
    }

    fun addSuccessImage(position: Int, successImage: SuccessImageModel) {
        this.successImages.add(position, successImage)
    }

    fun updateSuccessImage(position: Int, successImage: SuccessImageModel) {
        this.successImages[position] = successImage
    }

    fun addSuccessImage(successImage: SuccessImageModel) {
        this.successImages.add(successImage)
    }
}
