package com.theandroiddev.mywins.UI.Adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.UI.Helpers.DrawableSelector;
import com.theandroiddev.mywins.UI.Models.Success;

import java.util.List;

/**
 * Created by jakub on 07.08.17.
 * https://github.com/jcup1/MyWins-CardView/blob/master/app/src/main/java/com/example/jakubchmiel/mywins/SuccessAdapter.java
 */

public class SuccessAdapter extends RecyclerView.Adapter<SuccessAdapter.ViewHolder> {

    private List<Success> successes;
    private OnItemClickListener listener;
    private int itemLayout;
    private Context context;
    private DrawableSelector drawableSelector;

    public SuccessAdapter(List<Success> successes, int itemLayout, Context context, OnItemClickListener listener) {
        this.successes = successes;
        this.itemLayout = itemLayout;
        this.context = context;
        this.listener = listener;
        this.drawableSelector = new DrawableSelector(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.titleTv.setText(successes.get(position).getTitle());
        holder.categoryTv.setText(successes.get(position).getCategory());
        holder.dateStartedTv.setText(successes.get(position).getDateStarted());
        holder.dateEndedTv.setText(successes.get(position).getDateEnded());
        drawableSelector.selectCategoryImage(holder.categoryIv, successes.get(position).getCategory(), holder.categoryTv);
        drawableSelector.selectImportanceImage(holder.importanceIv, successes.get(position).getImportance());
        holder.bind(successes.get(position), listener, position);


    }

    @Override
    public int getItemCount() {
        return successes.size();
    }


    public interface OnItemClickListener {
        void onItemClick(Success success, int position, TextView titleTv, TextView categoryTv, TextView dateStartedTv, TextView dateEndedTv,
                         ImageView categoryIv, ImageView importanceIv, ConstraintLayout constraintLayout, CardView cardView);

        void onLongItemClick(int position, CardView cardView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv, categoryTv, dateStartedTv, dateEndedTv;
        ImageView categoryIv, importanceIv;
        ConstraintLayout constraintLayout;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.item_title);
            categoryTv = itemView.findViewById(R.id.item_category);
            dateStartedTv = itemView.findViewById(R.id.item_date_started);
            dateEndedTv = itemView.findViewById(R.id.item_date_ended);
            categoryIv = itemView.findViewById(R.id.item_category_iv);
            importanceIv = itemView.findViewById(R.id.item_importance_iv);

            constraintLayout = itemView.findViewById(R.id.item_constraint_layout);
            cardView = itemView.findViewById(R.id.item_card_view);
        }

        void bind(final Success success, final OnItemClickListener listener, final int position) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(success, position, titleTv, categoryTv, dateStartedTv, dateEndedTv, categoryIv, importanceIv, constraintLayout, cardView);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongItemClick(position, cardView);
                    return true;
                }
            });
        }

    }


}
