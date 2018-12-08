package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.models.moneyController;

import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmList;

public class newMoneyControllersAdapter extends RecyclerView.Adapter<newMoneyControllersAdapter.ViewHolder> {

    private final Context context;
    private int layout;
    private Vector<moneyController> controllers;
    private RealmList<category> categories;

    public newMoneyControllersAdapter(Context context, @NonNull RealmList<category> categories) {
        this.context = context;
        this.controllers = new Vector<>();
        this.controllers.addAll(Realm.getDefaultInstance().where(moneyController.class).findAll());
        this.layout = R.layout.new_money_controller_item;
        this.categories = categories;
    }

    @NonNull
    @Override
    public newMoneyControllersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new newMoneyControllersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull newMoneyControllersAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(controllers.get(i));
    }

    @Override
    public int getItemCount() {
        return controllers.size();
    }

    public void addOne() {
        controllers.add(new moneyController());
        controllers.lastElement().setAvailableId();
        notifyItemInserted(getItemCount() - 1);
    }

    public Vector<moneyController> getData() {
        return this.controllers;
    }

    public void setCategories(RealmList<category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView subcategoriesRecyclerView;
        private EditText name;
        private EditText max;
        private ImageButton removeButton;
        private categoriesSelectionAdapter subcategoriesAdapter;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            subcategoriesRecyclerView = itemView.findViewById(R.id.recyclerViewSubcategories);
            subcategoriesAdapter = new categoriesSelectionAdapter(new Vector<categoriesSelectionAdapter.categoryCheckBox>());
            subcategoriesRecyclerView.setAdapter(subcategoriesAdapter);

            name = itemView.findViewById(R.id.editTextCategoryName);
            max = itemView.findViewById(R.id.editTextMaxValue);
            removeButton = itemView.findViewById(R.id.imageButtonRemoveMoneyController);
        }

        void bind(final moneyController category) {
            name.setText(category.getName());
            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    category.setName(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            max.setText(category.getMaximum() == 0 ? "" : String.valueOf(category.getMaximum()));
            max.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    category.setMaximum(charSequence.length() == 0 ? 0.0 : Double.parseDouble(charSequence.toString()));
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            controllers.get(getAdapterPosition()).getSubcategories().clear();
            Vector<String> categoryNames = new Vector<>();
            for (int i = 0; i < categories.size(); i++) {
                categoryNames.add(categories.get(i).getName());
            }
            subcategoriesAdapter.updateCategories(categoryNames);
            subcategoriesAdapter.setOnItemsChangedInterface(new categoriesSelectionAdapter.onItemsChangedInterface() {
                @Override
                public void onItemChange(categoriesSelectionAdapter.categoryCheckBox changedCategory) {
                    if (changedCategory.selected) {
                        int i = 0;
                        category categoryToAdd = null;
                        while (i < categories.size()) {
                            if (categories.get(i).getName().equals(changedCategory.name)) {
                                categoryToAdd = categories.get(i);
                                break;
                            }
                            i++;
                        }
                        if (categoryToAdd != null)
                            controllers.get(getAdapterPosition()).getSubcategories().add(categoryToAdd);
                    } else {
                        int i = 0;
                        moneyController moneyController = controllers.get(getAdapterPosition());
                        while (i < moneyController.getSubcategories().size()) {
                            if (moneyController.getSubcategories().get(i).getName().equals(changedCategory.name)) {
                                moneyController.getSubcategories().remove(i);
                                break;
                            }
                        }
                    }
                }
            });
            subcategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    controllers.remove(pos);
                    notifyItemRemoved(pos);
                }
            });
        }
    }
}
