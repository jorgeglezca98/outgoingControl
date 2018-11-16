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
    private erasableItemsAdapter incomeCategoriesAdapter;
    private LinearLayoutManager incomeCategoriesLayoutManager;
    private int typeOfCategory;
    private RealmList<category> outgoingCategories;
    private RealmList<category> incomeCategories;
    private RecyclerView recyclerViewOutgoingCategories;

    public secondPageInitialConfiguration() {
        outgoingCategories = new RealmList<>();
        incomeCategories = new RealmList<>();
        outgoingCategoriesAdapter = new erasableItemsAdapter("Outgoing category", new erasableItemsAdapter.onItemsChange() {
            @Override
            public void onItemModified(int position, @NonNull String item) {
                outgoingCategories.get(position).setName(item);
            }

            @Override
            public void onItemRemoved(int position) {
                outgoingCategories.remove(position);
            }

            @Override
            public void onItemAdded(int position) {
                category categoryAdded = new category("", category.OUTGOING);
                outgoingCategories.add(position, categoryAdded);
            }

            @Override
            public void onItemsChanged(@NonNull Vector<String> newItems) {
                outgoingCategories.clear();
                for (int i = 0; i < newItems.size(); i++) {
                    outgoingCategories.add(new category(newItems.get(i), category.OUTGOING));
                }
            }
        }, null);
        incomeCategoriesAdapter = new erasableItemsAdapter("Income category", new erasableItemsAdapter.onItemsChange() {
            @Override
            public void onItemModified(int position, @NonNull String item) {
                incomeCategories.get(position).setName(item);
            }

            @Override
            public void onItemRemoved(int position) {
                incomeCategories.remove(position);
            }

            @Override
            public void onItemAdded(int position) {
                category categoryAdded = new category("", category.INCOME);
                incomeCategories.add(position, categoryAdded);
            }

            @Override
            public void onItemsChanged(@NonNull Vector<String> newItems) {
                incomeCategories.clear();
                for (int i = 0; i < newItems.size(); i++) {
                    incomeCategories.add(new category(newItems.get(i), category.INCOME));
                }
            }
        }, null);
        typeOfCategory = category.OUTGOING;
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

        recyclerViewOutgoingCategories = view.findViewById(R.id.recyclerViewOutgoingsCategoriesRequest);
        recyclerViewOutgoingCategories.setAdapter(outgoingCategoriesAdapter);
        outgoingCategoriesLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewOutgoingCategories.setLayoutManager(outgoingCategoriesLayoutManager);
        recyclerViewOutgoingCategories.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        RecyclerView incomeCategoriesRecyclerView = view.findViewById(R.id.recyclerViewIncomeCategoriesRequest);
        incomeCategoriesRecyclerView.setAdapter(incomeCategoriesAdapter);
        incomeCategoriesLayoutManager = new LinearLayoutManager(getContext());
        incomeCategoriesRecyclerView.setLayoutManager(incomeCategoriesLayoutManager);
        incomeCategoriesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    public void addOne() {
        if (typeOfCategory == category.OUTGOING) {
            outgoingCategoriesAdapter.addOne();
            outgoingCategoriesLayoutManager.scrollToPosition(outgoingCategoriesAdapter.getItemCount() - 1);
        } else if (typeOfCategory == category.INCOME) {
            incomeCategoriesAdapter.addOne();
            incomeCategoriesLayoutManager.scrollToPosition(incomeCategoriesAdapter.getItemCount() - 1);
        }
    }

    public int getTypeOfCategory() {
        return typeOfCategory;
    }

    public boolean checkOutgoingCategories() {
        int i = 0;
        while (i < outgoingCategories.size()) {
            if (outgoingCategories.get(i) == null || !outgoingCategories.get(i).check()) {
                return false;
            }
            i++;
        }
        return true;
    }

    public boolean checkIncomeCategories() {
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
        RealmList<category> data = new RealmList<>();
        data.addAll(outgoingCategories);
        data.addAll(incomeCategories);
        return data;
    }

    public RealmList<category> getIncomeCategoriesCategories() {
        return incomeCategories;
    }

    public RealmList<category> getOutgoingCategories() {
        return outgoingCategories;
    }

    public boolean goOn() {
        if (typeOfCategory == category.INCOME) {
            return false;
        } else {
            typeOfCategory = category.INCOME;
            recyclerViewOutgoingCategories.setVisibility(View.GONE);
            return true;
        }
    }

    public boolean goBack() {
        if (typeOfCategory == category.OUTGOING) {
            return false;
        } else {
            typeOfCategory = category.OUTGOING;
            recyclerViewOutgoingCategories.setVisibility(View.VISIBLE);
            return true;
        }
    }
}
