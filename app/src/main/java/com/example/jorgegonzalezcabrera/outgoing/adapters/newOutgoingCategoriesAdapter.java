package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.subcategory;

import io.realm.RealmList;

public class newOutgoingCategoriesAdapter extends RecyclerView.Adapter<newOutgoingCategoriesAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private int quantity;

    public newOutgoingCategoriesAdapter(Context context) {
        this.context = context;
        layout = R.layout.new_outgoing_category;
        quantity = 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(layout, viewGroup, false);
        return new newOutgoingCategoriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind();
    }

    @Override
    public int getItemCount() {
        return quantity;
    }

    public void addOne() {
        quantity++;
        notifyItemInserted(quantity - 1);
    }

    public void deleteLast() {
        if (quantity > 1) {
            quantity--;
            notifyItemRemoved(quantity);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText name;
        public EditText max;
        public ImageButton imageButtonAddSubcategory;
        public ImageButton imageButtonDeleteSubcategory;
        public RecyclerView recyclerViewSubcategories;
        erasableItemsAdapter adapter;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.editTextCategoryName);
            max = itemView.findViewById(R.id.editTextMaxValue);
            imageButtonAddSubcategory = itemView.findViewById(R.id.imageButtonAddSubcategory);
            imageButtonDeleteSubcategory = itemView.findViewById(R.id.imageButtonDeleteSubcategory);
            recyclerViewSubcategories = itemView.findViewById(R.id.recyclerViewSubcategories);
        }

        void bind() {
            adapter = new erasableItemsAdapter();
            recyclerViewSubcategories.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerViewSubcategories.setLayoutManager(layoutManager);

            imageButtonAddSubcategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.addOne();
                }
            });

            imageButtonDeleteSubcategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.deleteLast();
                }
            });
        }

        public RealmList<subcategory> getSubcategories() {
            RealmList<subcategory> result = new RealmList<>();
            erasableItemsAdapter.ViewHolder viewHolder;
            String subcategoryName;
            for (int i = 0; i < adapter.getItemCount(); i++) {
                viewHolder = (erasableItemsAdapter.ViewHolder) recyclerViewSubcategories.findViewHolderForAdapterPosition(i);
                if (viewHolder != null) {
                    subcategoryName = viewHolder.name.getText().toString();
                    if (!subcategoryName.isEmpty())
                        result.add(new subcategory(subcategoryName));
                }
            }
            if (result.isEmpty())
                result.add(new subcategory(name.getText().toString()));
            return result;
        }
    }
}