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
import com.example.jorgegonzalezcabrera.outgoing.models.incomeCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.subcategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

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
        Realm database = Realm.getDefaultInstance();
        RealmResults<outgoingCategory> outgoingCategories = database.where(outgoingCategory.class).findAll();
        RealmResults<incomeCategory> incomeCategories = database.where(incomeCategory.class).findAll();

        List<String> categories = new ArrayList<>();
        categories.add("Outgoing categories");
        for (int i = 0; i < outgoingCategories.size(); i++) {
            outgoingCategory outgoingCategory = outgoingCategories.get(i);
            if (outgoingCategory != null) {
                for (int j = 0; j < outgoingCategory.getSubcategories().size(); j++) {
                    subcategory subcategory = outgoingCategory.getSubcategories().get(j);
                    if (subcategory != null) {
                        categories.add(subcategory.getName());
                    }
                }
            }
        }
        categories.add("Income categories");
        for (int i = 0; i < incomeCategories.size(); i++) {
            incomeCategory incomeCategory = incomeCategories.get(i);
            if (incomeCategory != null) {
                categories.add(incomeCategory.getName());
            }
        }

        return categories;
    }
}