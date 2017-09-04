package com.theandroiddev.mywins;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by grazyna on 2017-08-23.
 */

class SuccessImageAdapter extends RecyclerView.Adapter<SuccessImageAdapter.ViewHolder> {

    private List<SuccessImage> successImages;
    private OnSuccessImageClickListener listener;
    private int successImageLayout;


    SuccessImageAdapter(List<SuccessImage> successImages, OnSuccessImageClickListener listener, int successImageLayout) {
        this.successImages = successImages;
        this.listener = listener;
        this.successImageLayout = successImageLayout;
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
                holder.bind(successImages.get(position), position, listener);

            }
        } else {

            Bitmap thumbnail = getbitpam(successImages.get(position).getImagePath());
            Bitmap orientedBitmap = ExifUtil.rotateBitmap(successImages.get(position).getImagePath(), thumbnail);
            holder.successImageIv.setImageBitmap(orientedBitmap);
            holder.bind(successImages.get(position), position, listener);

        }

    }

    private Bitmap getbitpam(String path) {
        Bitmap imgthumBitmap = null;
        try {

            final int THUMBNAIL_SIZE = 256;

            FileInputStream fis = new FileInputStream(path);
            imgthumBitmap = BitmapFactory.decodeStream(fis);

            imgthumBitmap = Bitmap.createScaledBitmap(imgthumBitmap,
                    THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

            ByteArrayOutputStream bytearroutstream = new ByteArrayOutputStream();
            imgthumBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytearroutstream);


        } catch (Exception ex) {
            Log.d(TAG, Log.getStackTraceString(ex));

        }
        return imgthumBitmap;
    }


    @Override
    public int getItemCount() {
        return successImages.size();
    }

    interface OnSuccessImageClickListener {
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
