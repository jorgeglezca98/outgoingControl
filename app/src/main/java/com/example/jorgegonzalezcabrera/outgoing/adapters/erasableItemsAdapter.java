package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.jorgegonzalezcabrera.outgoing.R;

public class erasableItemsAdapter extends RecyclerView.Adapter<erasableItemsAdapter.ViewHolder> {

    private int layout;
    private int quantity;

    public erasableItemsAdapter() {
        layout = R.layout.erasable_item;
        quantity = 1;
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

    @NonNull
    @Override
    public erasableItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new erasableItemsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull erasableItemsAdapter.ViewHolder viewHolder, int i) {
    }

    @Override
    public int getItemCount() {
        return quantity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.editTextErasableItem);
        }
    }
}
