package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.incomeCategory;

import io.realm.RealmList;

public class newIncomeCategoriesAdapter extends RecyclerView.Adapter<newIncomeCategoriesAdapter.ViewHolder> {

    private int layout;
    private RealmList<incomeCategory> newCategories;

    public newIncomeCategoriesAdapter() {
        layout = R.layout.new_income_category;
        newCategories = new RealmList<>();
        newCategories.add(new incomeCategory());
    }

    public void addOne(){
        newCategories.add(new incomeCategory());
        notifyDataSetChanged();
    }

    public void deleteLast(){
        if(newCategories.size()>1){
            newCategories.remove(newCategories.size()-1);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public newIncomeCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout,viewGroup,false);
        return new newIncomeCategoriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull newIncomeCategoriesAdapter.ViewHolder viewHolder, int i) {
        //TODO
    }

    @Override
    public int getItemCount() {
        return newCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.editTextIncomeCategoryName);
        }
    }
}
