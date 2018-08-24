package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jorgegonzalezcabrera.outgoing.R;

public class erasableItemsAdapter extends RecyclerView.Adapter<erasableItemsAdapter.ViewHolder> {

    private int layout;
    private int quantity;
    private String hint;

    public erasableItemsAdapter(@NonNull String hint) {
        this.layout = R.layout.erasable_item;
        this.quantity = 1;
        this.hint = hint;
    }

    public void addOne() {
        quantity++;
        notifyItemInserted(quantity - 1);
    }

    private void deleteItemAt(int position) {
        if (quantity > 0 && position < quantity) {
            quantity--;
            notifyItemRemoved(position);
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
        viewHolder.bind();
    }

    @Override
    public int getItemCount() {
        return quantity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText name;
        ImageButton deleteItemButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.editTextErasableItem);
            deleteItemButton = itemView.findViewById(R.id.imageButtonRemoveItem);
        }

        void bind(){
            name.setHint(hint);
            deleteItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    name.setText("");
                    deleteItemAt(getAdapterPosition());
                }
            });
        }
    }
}
