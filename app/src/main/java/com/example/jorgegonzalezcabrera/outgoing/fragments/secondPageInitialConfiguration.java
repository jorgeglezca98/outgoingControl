package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.erasableItemsAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.category;

import java.util.Vector;

import io.realm.RealmList;

public class secondPageInitialConfiguration extends Fragment {

    private erasableItemsAdapter outgoingCategoriesAdapter;
    private LinearLayoutManager outgoingCategoriesLayoutManager;
    private RealmList<category> categories;
    private RecyclerView recyclerViewOutgoingCategories;
    private ScrollView scrollViewContainer;

    public secondPageInitialConfiguration() {
        categories = new RealmList<>();
        outgoingCategoriesAdapter = new erasableItemsAdapter("Outgoing category", new erasableItemsAdapter.onItemsChange() {
            @Override
            public void onCategoryChanged(int position, @NonNull int type) {
                categories.get(position).setType(type);
            }

            @Override
            public void onNameChanged(int position, @NonNull String newName) {
                categories.get(position).setName(newName);
            }

            @Override
            public void onItemRemoved(int position) {
                categories.remove(position);
            }

            @Override
            public void onItemAdded(int position) {
                category categoryAdded = new category("", category.OUTGOING);
                categories.add(position, categoryAdded);
                if (scrollViewContainer != null)
                    scrollViewContainer.post(new Runnable() {
                        public void run() {
                            scrollViewContainer.fullScroll(View.FOCUS_DOWN);
                        }
                    });
            }

            @Override
            public void onItemsChanged(@NonNull Vector<category> newItems) {
                categories.clear();
                categories.addAll(newItems);
            }
        }, null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_fragment_initial_configuration, container, false);

        recyclerViewOutgoingCategories = view.findViewById(R.id.recyclerViewOutgoingsCategoriesRequest);
        recyclerViewOutgoingCategories.setAdapter(outgoingCategoriesAdapter);
        outgoingCategoriesLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewOutgoingCategories.setLayoutManager(outgoingCategoriesLayoutManager);
        recyclerViewOutgoingCategories.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        scrollViewContainer = view.findViewById(R.id.scrollViewContainer);

        return view;
    }

    public void addOne() {
        outgoingCategoriesAdapter.addOne();
    }

    public boolean checkCategories() {
        int i = 0;
        while (i < categories.size()) {
            if (categories.get(i) == null || !categories.get(i).check()) {
                return false;
            }
            i++;
        }
        return true;
    }

    public RealmList<category> getData() {
        return categories;
    }

    public RealmList<category> getIncomeCategoriesCategories() {
        RealmList<category> incomeCategories = new RealmList<>();
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getType() == category.INCOME)
                incomeCategories.add(categories.get(i));
        }
        return incomeCategories;
    }

    public RealmList<category> getCategories() {
        RealmList<category> outgoingCategories = new RealmList<>();
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getType() == category.OUTGOING)
                outgoingCategories.add(categories.get(i));
        }
        return outgoingCategories;
    }

}
