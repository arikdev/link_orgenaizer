package com.example.links_orgenizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TagsListForAdd_adapter extends RecyclerView.Adapter<TagsListForAdd_adapter.ViewHolder> {
    ArrayList<Tag> tags;
    ItemClicked activity;

    public TagsListForAdd_adapter(Context context, ArrayList<Tag> tags) {
        this.tags = tags;
        activity = (ItemClicked)context;
    }

    public interface ItemClicked {
        void tag_onClick(int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tag_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tag_tv = itemView.findViewById(R.id.tag_add_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.tag_onClick(tags.indexOf(v.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_add_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.itemView.setTag(tags.get(i));
        holder.tag_tv.setText(tags.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }
}
