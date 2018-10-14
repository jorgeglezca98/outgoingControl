package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.util.List;

public class categoriesSelectionAdapter extends RecyclerView.Adapter<categoriesSelectionAdapter.ViewHolder> {

    private int layout;
    private List<String> categories;

    public categoriesSelectionAdapter() {
        layout = R.layout.category_selection_item;
        categories = localUtils.getAllCategories();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new categoriesSelectionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(categories.get(i));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void removeCategory(String removedCategory) {
        categories.remove(removedCategory);
    }

    public void addCategory(String newCategory) {
        categories.add(newCategory);
        notifyItemInserted(categories.size() - 1);
    }

    public void editCategory(String newName, String oldName) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).equals(oldName)) {
                categories.remove(i);
                categories.add(newName);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkboxCategory;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkboxCategory = itemView.findViewById(R.id.checkboxCategory);
        }

        void bind(String categoryName) {
            checkboxCategory.setChecked(true);
            checkboxCategory.setText(categoryName);
        }
    }
}
