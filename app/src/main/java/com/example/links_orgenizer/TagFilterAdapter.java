package com.example.links_orgenizer;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TagFilterAdapter extends RecyclerView.Adapter<TagFilterAdapter.ViewHolder> {
    ArrayList<Tag> filter;

    public TagFilterAdapter(Context context, ArrayList<Tag> filter) {
        this.filter = filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tag_tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tag_tv = itemView.findViewById(R.id.tag_filter_tv);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_filter_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.tag_tv.setText(filter.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return filter.size();
    }
}
