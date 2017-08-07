package com.example.jakubchmiel.mywins;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jakub on 07.08.17.
 */

public class SuccessAdapter extends RecyclerView.Adapter<SuccessAdapter.ViewHolder> {

    //1
    private List<Success> successes;
    private int itemLayout;

    //2
    public SuccessAdapter(List<Success> successes, int itemLayout) {
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

        holder.text.setText(successes.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return successes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.item_title);
        }
    }
}
