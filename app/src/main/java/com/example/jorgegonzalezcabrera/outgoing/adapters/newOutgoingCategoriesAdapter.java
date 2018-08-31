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
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.subcategory;

import java.util.Vector;

import io.realm.RealmList;

public class newOutgoingCategoriesAdapter extends RecyclerView.Adapter<newOutgoingCategoriesAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private RealmList<outgoingCategory> categories;

    public newOutgoingCategoriesAdapter(Context context) {
        this.context = context;
        this.layout = R.layout.new_outgoing_category;
        this.categories = new RealmList<>();
        this.categories.add(new outgoingCategory());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(layout, viewGroup, false);
        return new newOutgoingCategoriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(categories.get(i));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void addOne() {
        categories.add(new outgoingCategory());
        notifyItemInserted(getItemCount() - 1);
    }

    private void deleteItemAt(int position) {
        if (getItemCount() > 1 && position < getItemCount()) {
            categories.remove(position);
            notifyItemRemoved(position);
        }
    }

    public RealmList<outgoingCategory> getCategories() {
        return categories;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText name;
        public EditText max;
        public ImageButton imageButtonAddSubcategory;
        public RecyclerView recyclerViewSubcategories;
        public ImageButton imageButtonRemoveCategory;
        erasableItemsAdapter adapter;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.editTextCategoryName);
            max = itemView.findViewById(R.id.editTextMaxValue);
            imageButtonAddSubcategory = itemView.findViewById(R.id.imageButtonAddSubcategory);
            recyclerViewSubcategories = itemView.findViewById(R.id.recyclerViewSubcategories);
            imageButtonRemoveCategory = itemView.findViewById(R.id.imageButtonRemoveCategory);

            adapter = new erasableItemsAdapter("Subcategory", new erasableItemsAdapter.onItemsChange() {
                @Override
                public void onItemModified(int position, @NonNull String item) {
                    categories.get(getAdapterPosition()).getSubcategories().get(position).setName(item);
                }

                @Override
                public void onItemRemoved(int position) {
                    categories.get(getAdapterPosition()).getSubcategories().remove(position);
                }

                @Override
                public void onItemAdded(int position) {
                    categories.get(getAdapterPosition()).getSubcategories().add(position, new subcategory());
                }

                @Override
                public void onItemsChanged(@NonNull Vector<String> newItems) {
                    RealmList<subcategory> subcategories = new RealmList<>();
                    for (int i = 0; i < newItems.size(); i++) {
                        subcategories.add(new subcategory(newItems.get(i)));
                    }
                    categories.get(getAdapterPosition()).setSubcategories(subcategories);
                }
            });

            recyclerViewSubcategories.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerViewSubcategories.setLayoutManager(layoutManager);
        }

        void bind(final outgoingCategory outgoingCategory) {
            imageButtonAddSubcategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.addOne();
                }
            });

            imageButtonRemoveCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItemAt(getAdapterPosition());
                }
            });

            name.setText(outgoingCategory.getName());
            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    outgoingCategory.setName(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            if (outgoingCategory.getMaximum() != 0.0d) {
                max.setText(String.valueOf(outgoingCategory.getMaximum()));
            } else {
                max.setText("");
            }
            max.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().isEmpty()) {
                        outgoingCategory.setMaximum(Double.valueOf(charSequence.toString()));
                    } else {
                        outgoingCategory.setMaximum(0.0d);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            Vector<String> subcategories = new Vector<>();
            for (int i = 0; i < outgoingCategory.getSubcategories().size(); i++) {
                subcategories.add(outgoingCategory.getSubcategories().get(i).getName());
            }
            adapter.updateItems(subcategories);
        }
    }
}