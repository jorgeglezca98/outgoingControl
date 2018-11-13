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

public class secondPageInitialConfiguration extends Fragment {

    private erasableItemsAdapter outgoingCategoriesAdapter;
    private LinearLayoutManager outgoingCategoriesLayoutManager;
    private RealmList<category> data;

    public secondPageInitialConfiguration() {
        data = new RealmList<>();
        outgoingCategoriesAdapter = new erasableItemsAdapter("Outgoing category", new erasableItemsAdapter.onItemsChange() {
            @Override
            public void onItemModified(int position, @NonNull String item) {
                data.get(position).setName(item);
            }

            @Override
            public void onItemRemoved(int position) {
                data.remove(position);
            }

            @Override
            public void onItemAdded(int position) {
                category categoryAdded = new category("", category.OUTGOING);
                data.add(position, categoryAdded);
            }

            @Override
            public void onItemsChanged(@NonNull Vector<String> newItems) {
                data.clear();
                for (int i = 0; i < newItems.size(); i++) {
                    data.add(new category(newItems.get(i), category.OUTGOING));
                }
            }
        }, null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_fragment_initial_configuration, container, false);

        final TextView informationTextView = view.findViewById(R.id.outgoingsCategoriesDescription);
        ImageButton informationButton = view.findViewById(R.id.buttonInformation);
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                informationTextView.setVisibility(informationTextView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        RecyclerView recyclerViewOutgoingCategories = view.findViewById(R.id.recyclerViewOutgoingsCategoriesRequest);
        recyclerViewOutgoingCategories.setAdapter(outgoingCategoriesAdapter);
        outgoingCategoriesLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewOutgoingCategories.setLayoutManager(outgoingCategoriesLayoutManager);

        return view;
    }

    public void addOne() {
        outgoingCategoriesAdapter.addOne();
        outgoingCategoriesLayoutManager.scrollToPosition(outgoingCategoriesAdapter.getItemCount() - 1);
    }

    public boolean checkData() {
        int i = 0;
        while (i < data.size()) {
            if (data.get(i) == null || !data.get(i).check()) {
                return false;
            }
            i++;
        }
        return true;
    }

    public RealmList<category> getData() {
        return data;
    }
}
