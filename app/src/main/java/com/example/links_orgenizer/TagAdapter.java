package com.example.links_orgenizer;

import android.content.Context;
import android.os.IInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    ArrayList<Tag> tags;
    ItemClicked activity;

    public TagAdapter(Context context, ArrayList<Tag> tags) {
        activity = (ItemClicked)context;
        this.tags = tags;
    }

    public interface ItemClicked {
        void onClick(int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tag_tv, desc_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tag_tv = itemView.findViewById(R.id.tag_name_tv);
            desc_tv = itemView.findViewById(R.id.tag_desc_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onClick(tags.indexOf(v.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.itemView.setTag(tags.get(i));
        holder.tag_tv.setText(tags.get(i).getName());
        holder.desc_tv.setText(tags.get(i).getDescription());
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }
}
