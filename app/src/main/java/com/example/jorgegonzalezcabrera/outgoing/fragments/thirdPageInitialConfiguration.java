package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.erasableItemsAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.category;

import java.util.Vector;

import io.realm.RealmList;

public class thirdPageInitialConfiguration extends Fragment {

    private erasableItemsAdapter incomeCategoriesAdapter;
    private LinearLayoutManager incomeCategoriesLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment_initial_configuration, container, false);

        final TextView informationTextView = view.findViewById(R.id.incomeCategoriesDescription);
        ImageButton informationButton = view.findViewById(R.id.buttonInformation);
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                informationTextView.setVisibility(informationTextView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        RecyclerView incomeCategoriesRecyclerView = view.findViewById(R.id.recyclerViewIncomeCategoriesRequest);
        incomeCategoriesAdapter = new erasableItemsAdapter("Income category");
        incomeCategoriesRecyclerView.setAdapter(incomeCategoriesAdapter);
        incomeCategoriesLayoutManager = new LinearLayoutManager(getContext());
        incomeCategoriesRecyclerView.setLayoutManager(incomeCategoriesLayoutManager);

        return view;
    }

    public void addOne() {
        incomeCategoriesAdapter.addOne();
        incomeCategoriesLayoutManager.scrollToPosition(incomeCategoriesAdapter.getItemCount() - 1);
    }

    public boolean checkData() {
        Vector<String> adapterItems = incomeCategoriesAdapter.getItems();
        int i = 0;
        while (i < adapterItems.size()) {
            if (adapterItems.get(i).isEmpty()) {
                return false;
            }
            i++;
        }
        return true;
    }

    public RealmList<category> getData() {
        Vector<String> adapterItems = incomeCategoriesAdapter.getItems();
        RealmList<category> data = new RealmList<>();
        for (int i = 0; i < adapterItems.size(); i++) {
            data.add(new category(adapterItems.get(i), category.INCOME));
        }
        return data;
    }
}
