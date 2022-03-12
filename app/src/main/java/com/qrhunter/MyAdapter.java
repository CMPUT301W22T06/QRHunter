package com.qrhunter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private ArrayList<String> mData;

    public MyAdapter(ArrayList<String> data) {
        this.mData = data;
    }

    public void updateData(ArrayList<String> data) {
        this.mData = data;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTv.setText(mData.get(position));
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mData.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTv;
        ImageView cancel;

        public ViewHolder(View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.item_tv);
            cancel = itemView.findViewById(R.id.item_cancel);

        }
    }
}