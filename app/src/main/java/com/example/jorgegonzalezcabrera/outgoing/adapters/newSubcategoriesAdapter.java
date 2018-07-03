package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.subcategory;

import io.realm.RealmList;

public class newSubcategoriesAdapter extends RecyclerView.Adapter<newSubcategoriesAdapter.ViewHolder> {

    private int layout;
    private RealmList<subcategory> newSubcategories;

    public newSubcategoriesAdapter() {
        layout = R.layout.new_subcategory;
        newSubcategories = new RealmList<>();
        newSubcategories.add(new subcategory());
    }

    public void addOne() {
        newSubcategories.add(new subcategory());
        notifyDataSetChanged();
    }

    public void deleteLast() {
        if(newSubcategories.size()>0) {
            newSubcategories.remove(newSubcategories.size() - 1);
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new newSubcategoriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //TODO
    }

    @Override
    public int getItemCount() {
        return newSubcategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText subcategoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subcategoryName = itemView.findViewById(R.id.editTextNewSubcategory);
        }
    }
}
