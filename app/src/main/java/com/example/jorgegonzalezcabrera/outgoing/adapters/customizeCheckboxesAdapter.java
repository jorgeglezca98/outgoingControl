package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;

import java.util.Vector;

public class customizeCheckboxesAdapter extends RecyclerView.Adapter<customizeCheckboxesAdapter.ViewHolder> {

    private static class elementCheckbox {
        public String name;
        public boolean selected;

        public elementCheckbox(@NonNull String name, boolean selected) {
            this.name = name;
            this.selected = selected;
        }
    }

    private int layout;
    private Vector<elementCheckbox> items;

    public customizeCheckboxesAdapter(Vector<String> items) {
        this.layout = R.layout.labeled_checkbox_item;
        this.items = new Vector<>();
        for (int i = 0; i < items.size(); i++) {
            this.items.add(new elementCheckbox(items.get(i), false));
        }
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

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkbox);
            textView = itemView.findViewById(R.id.label);
        }

        public void bind(elementCheckbox element) {
            checkBox.setChecked(element.selected);
            textView.setText(element.name);
        }
    }
}
