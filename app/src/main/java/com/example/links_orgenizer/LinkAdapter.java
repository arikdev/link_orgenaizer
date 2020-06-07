package com.example.links_orgenizer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder> {
    ArrayList<Link> links;
    ArrayList<Tag> filter;
    ItemClicked activity;
    Boolean with_filter;

    public LinkAdapter(Context context, ArrayList<Link> links) {
        this.links = links;
        activity = (ItemClicked)context;
        with_filter = false;
    }

    public interface ItemClicked {
        void onClick(int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_tv, description_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_tv = itemView.findViewById(R.id.name_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onClick(links.indexOf(v.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        int x;
        Log.i("XXX", "------On bind i:" + i + " filter:" + with_filter + " filter:" + filter + " The tags of the filter:\n");

        if (filter != null) {
            for (int j = 0; j < filter.size(); j++)
                Log.i("XXX", ":" + filter.get(j).getName().toString());
        }

        holder.itemView.setTag(links.get(i));
        holder.name_tv.setText(links.get(i).getName());
    }

    static Boolean filter_matchs(String tags, ArrayList<Tag> filter) {
        StringTokenizer tokens;
        ArrayList<String> tags_list;
        String tag;
        Boolean found;

        tags_list = new ArrayList<String>();
        tokens = new StringTokenizer(tags, " ");
        while (tokens.hasMoreTokens())
            tags_list.add(tokens.nextToken());

        for (int i = 0; i < filter.size(); i++) {
            for (int j = 0; j < tags_list.size(); j++) {
                if (filter.get(i).getName().equals(tags_list.get(j)))
                    return true;
            }
        }

        return false;
    }

    @Override
    public int getItemCount() {
        if (!with_filter)
            return links.size();
        int count = 0;
        for (int i = 0; i < links.size(); i++) {
            if (filter_matchs(links.get(i).getTags(), filter)) {
                count++;
            }
        }
        return count;
    }

    public void set_filter(Boolean with_filter, ArrayList<Tag> filter) {
        this.filter = filter;
        this.with_filter = with_filter;
    }
}
