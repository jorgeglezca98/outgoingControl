package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.util.ArrayList;
import java.util.Vector;

public class categoriesSelectionAdapter extends RecyclerView.Adapter<categoriesSelectionAdapter.ViewHolder> {

    public static class categoryCheckBox {
        public String name;
        public boolean selected;

        public categoryCheckBox(@NonNull String name, boolean selected) {
            this.name = name;
            this.selected = selected;
        }
    }

    private int layout;
    private Vector<categoryCheckBox> categories;
    private onItemsChangedInterface onItemsChangedInterface;

    public interface onItemsChangedInterface {
        void onItemChange(categoryCheckBox category);
    }

    public final static int FUNCTIONING_OUTGOING_CATEGORIES = 0;
    public final static int ALL_CATEGORIES = 1;

    public categoriesSelectionAdapter(int option) {
        this.layout = R.layout.category_selection_item;
        this.categories = new Vector<>();
        if (option == FUNCTIONING_OUTGOING_CATEGORIES) {
            Vector<String> categoryNames = localUtils.getFunctioningOutgoingCategories();
            for (int i = 0; i < categoryNames.size(); i++) {
                categories.add(new categoryCheckBox(categoryNames.get(i), true));
            }
        } else if (option == ALL_CATEGORIES) {
            Vector<String> categoryNames = localUtils.getAllCategories();
            for (int i = 0; i < categoryNames.size(); i++) {
                categories.add(new categoryCheckBox(categoryNames.get(i), true));
            }
        }
        this.onItemsChangedInterface = new onItemsChangedInterface() {
            @Override
            public void onItemChange(categoryCheckBox category) {

            }
        };
    }

    public categoriesSelectionAdapter(@NonNull Vector<categoryCheckBox> categories) {
        this.layout = R.layout.category_selection_item;
        this.categories = categories;
        this.onItemsChangedInterface = new onItemsChangedInterface() {
            @Override
            public void onItemChange(categoryCheckBox category) {

            }
        };
    }

    public void setOnItemsChangedInterface(categoriesSelectionAdapter.onItemsChangedInterface onItemsChangedInterface) {
        this.onItemsChangedInterface = onItemsChangedInterface;
    }

    public Vector<categoryCheckBox> getCategories() {
        return categories;
    }

    public void markAsChecked(ArrayList<String> checkedCategories) {
        if (checkedCategories != null) {
            for (int i = 0; i < this.categories.size(); i++) {
                this.categories.get(i).selected = checkedCategories.contains(this.categories.get(i).name);
            }
        }
    }

    public void updateCategories(Vector<String> newCategories) {
        Vector<categoryCheckBox> updatedCategories = new Vector<>();
        for (int i = 0; i < newCategories.size(); i++) {
            boolean selected = false;
            int j = 0;
            while (j < categories.size()) {
                if (categories.get(j).name.equals(newCategories.get(i))) {
                    selected = categories.get(j).selected;
                    break;
                }
                j++;
            }
            updatedCategories.add(new categoryCheckBox(newCategories.get(i), selected));
        }
        this.categories = updatedCategories;
        notifyDataSetChanged();
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkboxCategory;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxCategory = itemView.findViewById(R.id.checkboxCategory);
        }

        void bind(final categoryCheckBox category) {
            final int categoryPosition = getAdapterPosition();
            checkboxCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    categories.get(categoryPosition).selected = b;
                    onItemsChangedInterface.onItemChange(categories.get(categoryPosition));
                }
            });
            checkboxCategory.setChecked(category.selected);
            checkboxCategory.setText(category.name);
        }
    }
}
