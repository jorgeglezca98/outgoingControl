package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;

import io.realm.Realm;
import io.realm.RealmList;

public class editableOutgoingCategoriesAdapter extends RecyclerView.Adapter<editableOutgoingCategoriesAdapter.ViewHolder> {

    private int layout;
    private RealmList<outgoingCategory> categories;

    public editableOutgoingCategoriesAdapter() {
        this.categories = new RealmList<>();
        this.categories.addAll(Realm.getDefaultInstance().where(outgoingCategory.class).findAll());
        layout = R.layout.editable_outgoing_category;
    }

    @NonNull
    @Override
    public editableOutgoingCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new editableOutgoingCategoriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull editableOutgoingCategoriesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(categories.get(i));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private EditText name;
        private EditText max;
        private EditText subcategories;
        private ImageButton expandButton;
        private ImageButton editButton;
        private ImageButton removeButton;

        private boolean expanded;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.editTextCategoryName);
            max = itemView.findViewById(R.id.editTextMaxValue);
            subcategories = itemView.findViewById(R.id.editTextSubcategories);
            expandButton = itemView.findViewById(R.id.imageButtonExpandCategory);
            editButton = itemView.findViewById(R.id.imageButtonEditCategory);
            removeButton = itemView.findViewById(R.id.imageButtonRemoveCategory);
            expanded = false;
        }

        void bind(@NonNull final outgoingCategory category) {
            name.setText(category.getName());
            max.setText(String.valueOf(category.getMaximum()));

            String listOfSubcategories = "";
            for (int i = 0; i < category.getSubcategories().size(); i++) {
                listOfSubcategories += category.getSubcategories().get(i).getName() + ",";
            }
            subcategories.setText(listOfSubcategories);

            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!expanded) {
                        expandButton.animate().rotation(180);
                        String listOfSubcategories = "";
                        for (int i = 0; i < category.getSubcategories().size(); i++) {
                            listOfSubcategories += category.getSubcategories().get(i).getName() + "\n";
                        }
                        subcategories.setText(listOfSubcategories);
                        editButton.setVisibility(View.VISIBLE);
                        removeButton.setVisibility(View.VISIBLE);
                    } else {
                        expandButton.animate().rotation(0);
                        String listOfSubcategories = "";
                        for (int i = 0; i < category.getSubcategories().size(); i++) {
                            listOfSubcategories += category.getSubcategories().get(i).getName() + ",";
                        }
                        subcategories.setText(listOfSubcategories);
                        editButton.setVisibility(View.GONE);
                        removeButton.setVisibility(View.GONE);
                    }

                    expanded = !expanded;
                }
            });
        }
    }
}
