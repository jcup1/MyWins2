package com.example.jakubchmiel.mywins;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jakub on 07.08.17.
 * https://github.com/jcup1/MyWins-CardView/blob/master/app/src/main/java/com/example/jakubchmiel/mywins/SuccessAdapter.java
 */

class SuccessAdapter extends RecyclerView.Adapter<SuccessAdapter.ViewHolder> {

    //1
    private List<Success> successes;
    private int itemLayout;
    private Context context;

    //2
    SuccessAdapter(List<Success> successes, int itemLayout) {
        this.successes = successes;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //3
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //5
        holder.titleTv.setText(successes.get(position).getTitle());
        holder.categoryTv.setText(successes.get(position).getCategory());
        holder.dateTv.setText(successes.get(position).getDate());
        selectCategoryImage(holder.categoryIv, successes.get(position).getCategory());
        selectImportanceImage(holder.importanceIv, successes.get(position).getImportance());

    }


    @Override
    public int getItemCount() {
        return successes.size();
    }


    //6
    private void selectCategoryImage(ImageView image, String category) {
        int id = R.drawable.ic_mood_black_40dp; //default: other
        int color = R.color.other;

        if (category.equalsIgnoreCase("video")) {
            id = R.drawable.ic_play_circle_filled_black_40dp;
            color = R.color.videos;
        }
        if (category.equalsIgnoreCase("sport")) {
            id = R.drawable.ic_directions_bike_black_40dp;
            color = R.color.sport;
        }
        if (category.equalsIgnoreCase("money")) {
            id = R.drawable.ic_attach_money_black_40dp;
            color = R.color.money;
        }
        if (category.equalsIgnoreCase("journey")) {
            id = R.drawable.ic_location_on_black_40dp;
            color = R.color.journey;
        }
        if (category.equalsIgnoreCase("knowledge")) {
            id = R.drawable.ic_school_black_40dp;
            color = R.color.knowledge;
        }

        Drawable myDrawable = ResourcesCompat.getDrawable(context.getResources(), id, null);
        image.setImageDrawable(myDrawable);
        image.setColorFilter(ContextCompat.getColor(context, color));

    }

    //7
    private void selectImportanceImage(ImageView importanceIv, String importance) {

        int id = R.drawable.importance_medium; //default: other

        if (importance.equalsIgnoreCase("huge")) {
            id = R.drawable.importance_huge;
        }
        if (importance.equalsIgnoreCase("big")) {
            id = R.drawable.importance_big;
        }
        if (importance.equalsIgnoreCase("medium")) {
            id = R.drawable.importance_medium;
        }
        if (importance.equalsIgnoreCase("small")) {
            return;
        }

        Drawable myDrawable = ResourcesCompat.getDrawable(context.getResources(), id, null);
        importanceIv.setImageDrawable(myDrawable);

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        //4
        TextView titleTv, categoryTv, dateTv;
        ImageView categoryIv, importanceIv;

        ViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.item_title);
            categoryTv = itemView.findViewById(R.id.item_category_tv);
            dateTv = itemView.findViewById(R.id.item_date);
            categoryIv = itemView.findViewById(R.id.item_category_iv);
            importanceIv = itemView.findViewById(R.id.item_importance_iv);

        }
    }
}
