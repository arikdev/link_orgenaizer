package com.example.links_orgenizer;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class list_f extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LinkAdapter adapter;
    View view;

    public list_f() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_f, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LinkAdapter(this.getContext(), MainActivity.links);
        recyclerView.setAdapter(adapter);
    }

    public void notify_change() {
        adapter.notifyDataSetChanged();
    }

    public void set_filter(Boolean with_filter, ArrayList<Tag> filter) {
        adapter.set_filter(with_filter, filter);
    }
}
