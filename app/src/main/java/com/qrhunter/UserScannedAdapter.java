package com.qrhunter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UserScannedAdapter extends ArrayAdapter<Collectable> {
    private int resourceId;
    public UserScannedAdapter(@NonNull Context context, int resource, List<Collectable> list) {
        super(context, resource,list);
        resourceId = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Collectable collectable = getItem(position);

        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = view.findViewById(R.id.tv_name);
            viewHolder.tvScore = view.findViewById(R.id.tv_score);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvName.setText(collectable.getName());
        viewHolder.tvScore.setText(String.valueOf(collectable.getScore()));

        return view;
    }

    class ViewHolder{
        TextView tvName;
        TextView tvScore;
    }
}
