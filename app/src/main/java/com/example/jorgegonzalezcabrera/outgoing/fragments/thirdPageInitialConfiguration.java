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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.erasableItemsAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.incomeCategory;

import io.realm.RealmList;

public class thirdPageInitialConfiguration extends Fragment {

    private RecyclerView incomeCategoriesRecyclerView;
    private erasableItemsAdapter incomeCategoriesAdapter;

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

        incomeCategoriesRecyclerView = view.findViewById(R.id.recyclerViewIncomeCategoriesRequest);
        incomeCategoriesAdapter = new erasableItemsAdapter("Income category");
        incomeCategoriesRecyclerView.setAdapter(incomeCategoriesAdapter);
        final LinearLayoutManager incomeCategoriesLayoutManager = new LinearLayoutManager(getContext());
        incomeCategoriesRecyclerView.setLayoutManager(incomeCategoriesLayoutManager);

        Button buttonAddNewIncomeCategory = view.findViewById(R.id.buttonAddNewIncomeCategory);
        buttonAddNewIncomeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomeCategoriesAdapter.addOne();
                incomeCategoriesLayoutManager.scrollToPosition(incomeCategoriesAdapter.getItemCount() - 1);
            }
        });

        return view;
    }

    public boolean checkData() {
        boolean check = true;
        if (incomeCategoriesAdapter.getItemCount() > 0) {
            erasableItemsAdapter.ViewHolder incomeViewHolder;
            for (int i = 0; i < incomeCategoriesAdapter.getItemCount(); i++) {
                incomeViewHolder = (erasableItemsAdapter.ViewHolder) incomeCategoriesRecyclerView.findViewHolderForAdapterPosition(i);
                if (incomeViewHolder != null && incomeViewHolder.name.getText().toString().isEmpty()) {
                    incomeViewHolder.name.setHintTextColor(getResources().getColor(R.color.colorWrong));
                    check = false;
                }
            }
        }
        return check;
    }

    public RealmList<incomeCategory> getData() {
        RealmList<incomeCategory> data = new RealmList<>();
        erasableItemsAdapter.ViewHolder viewHolder;
        for (int i = 0; i < incomeCategoriesAdapter.getItemCount(); i++) {
            viewHolder = (erasableItemsAdapter.ViewHolder) incomeCategoriesRecyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                data.add(new incomeCategory(viewHolder.name.getText().toString()));
            }
        }
        return data;
    }
}
