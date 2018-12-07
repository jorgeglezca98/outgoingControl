package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;

import java.util.Vector;

public class customizeCheckboxesAdapter extends RecyclerView.Adapter<customizeCheckboxesAdapter.ViewHolder> {

    private static class elementCheckbox {
        public String name;
        public boolean selected;

        elementCheckbox(@NonNull String name, boolean selected) {
            this.name = name;
            this.selected = selected;
        }
    }

    private int layout;
    private Vector<elementCheckbox> items;
    private int numberOfSelectedItems;

    public customizeCheckboxesAdapter(Vector<String> items) {
        this.layout = R.layout.labeled_checkbox_item;
        this.items = new Vector<>();
        for (int i = 0; i < items.size(); i++) {
            this.items.add(new elementCheckbox(items.get(i), false));
        }
        this.numberOfSelectedItems = 0;
    }

    @NonNull
    @Override
    public customizeCheckboxesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new customizeCheckboxesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull customizeCheckboxesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(items.get(i));
    }

    public boolean isSomeoneSelected() {
        return numberOfSelectedItems > 0;
    }

    public Vector<String> getCheckedItems() {
        Vector<String> result = new Vector<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).selected) {
                result.add(items.get(i).name);
            }
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox);
            textView = itemView.findViewById(R.id.label);
        }

        public void bind(elementCheckbox element) {
            checkBox.setChecked(element.selected);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    items.get(getAdapterPosition()).selected = b;
                    if (b) numberOfSelectedItems++;
                    else numberOfSelectedItems--;
                }
            });
            textView.setText(element.name);
        }
    }
}
