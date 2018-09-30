package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class categoriesSpinnerAdapter extends ArrayAdapter<String> {

    private int outgoingCategoriesSize;

    public categoriesSpinnerAdapter(@NonNull Context context) {
        super(context, R.layout.categories_spinner_item, getCategories());
        outgoingCategoriesSize = 0;
        while (!Objects.equals(getItem(outgoingCategoriesSize), "Income categories")) {
            outgoingCategoriesSize++;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return (position != 0) && (position != outgoingCategoriesSize);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    public boolean isOutgoingCategory(int position) {
        return (position < outgoingCategoriesSize);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.categories_spinner_item, null);

        TextView categoryName = itemView.findViewById(R.id.textViewCategoriesSpinnerItem);
        categoryName.setText(getItem(position));
        if (isEnabled(position)) {
            categoryName.setPadding(30, 3, 3, 3);
            if (position < outgoingCategoriesSize)
                categoryName.setTextColor(Color.parseColor("#DF0101"));
            else
                categoryName.setTextColor(Color.parseColor("#04B404"));
        } else {
            categoryName.setTextColor(Color.parseColor("#000000"));
        }

        return itemView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.categories_spinner_item, null);

        TextView categoryName = itemView.findViewById(R.id.textViewCategoriesSpinnerItem);
        categoryName.setText(getItem(position));
        if (position < outgoingCategoriesSize)
            categoryName.setTextColor(Color.parseColor("#DF0101"));
        else
            categoryName.setTextColor(Color.parseColor("#04B404"));
        return itemView;
    }

    private static List<String> getCategories() {
        Vector<String> categories = new Vector<>();
        categories.add("Outgoing categories");
        categories.addAll(localUtils.getFunctioningOutgoingCategories());
        categories.add("Income categories");
        categories.addAll(localUtils.getFunctioningIncomeCategories());

        return categories;
    }
}