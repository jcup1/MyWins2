package com.theandroiddev.mywins.images;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.data.models.SuccessImage;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by grazyna on 2017-08-23.
 */

public class SuccessImageAdapter extends RecyclerView.Adapter<SuccessImageAdapter.ViewHolder> {

    private ArrayList<SuccessImage> successImages;
    private OnSuccessImageClickListener listener;
    private int successImageLayout;
    private Context context;


    public SuccessImageAdapter(ArrayList<SuccessImage> successImages, OnSuccessImageClickListener listener, int successImageLayout, Context context) {
        this.successImages = successImages;
        this.listener = listener;
        this.successImageLayout = successImageLayout;
        this.context = context;
    }

    @Override
    public SuccessImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(successImageLayout, parent, false);
        return new SuccessImageAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SuccessImageAdapter.ViewHolder holder, int position) {

        if (successImages.get(position).getImagePath() == null) {
            if (position == 0) {
                holder.successImageIv.setImageResource(R.drawable.ic_action_add);
                //holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.primary));
                holder.bind(successImages.get(position), position, listener);

            }
        } else {

            Picasso.with(context).load(new File(successImages.get(position).getImagePath())).resize(256, 256).centerCrop().into(holder.successImageIv);
            holder.bind(successImages.get(position), position, listener);

        }

    }


    @Override
    public int getItemCount() {
        return successImages.size();
    }

    public interface OnSuccessImageClickListener {
        void onSuccessImageClick(SuccessImage successImage, ImageView successImageIv, int position, ConstraintLayout constraintLayout,
                                 CardView cardView);

        void onSuccessImageLongClick(SuccessImage successImage, ImageView successImageIv, int position, ConstraintLayout constraintLayout,
                                     CardView cardView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView successImageIv;
        ConstraintLayout constraintLayout;
        CardView cardView;


        ViewHolder(View itemView) {
            super(itemView);

            successImageIv = itemView.findViewById(R.id.success_image_iv);
            constraintLayout = itemView.findViewById(R.id.success_image_constraint_layout);
            cardView = itemView.findViewById(R.id.success_image_card_view);

        }

        private void bind(final SuccessImage successImage, final int position, final OnSuccessImageClickListener onClickListener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSuccessImageClick(successImage, successImageIv, position, constraintLayout, cardView);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onSuccessImageLongClick(successImage, successImageIv, position, constraintLayout, cardView);
                    return true;
                }
            });
        }
    }
}
