package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jorgegonzalezcabrera.outgoing.R;

import java.util.Vector;

public class erasableItemsAdapter extends RecyclerView.Adapter<erasableItemsAdapter.ViewHolder> {

    private int layout;
    private Vector<String> items;
    private String hint;
    private onItemsChange onItemsChange;

    public erasableItemsAdapter(@NonNull String hint) {
        this.layout = R.layout.erasable_item;
        this.items = new Vector<>();
        this.items.add("");
        this.hint = hint;
        this.onItemsChange = new onItemsChange() {
            @Override
            public void onItemModified(int position, @NonNull String item) {
            }

            @Override
            public void onItemRemoved(int position) {
            }

            @Override
            public void onItemAdded(int position) {
            }

            @Override
            public void onItemsChanged(@NonNull Vector<String> newItems) {
            }
        };
    }

    erasableItemsAdapter(String hint, erasableItemsAdapter.onItemsChange onItemsChange) {
        this.layout = R.layout.erasable_item;
        this.items = new Vector<>();
        this.items.add("");
        this.hint = hint;
        this.onItemsChange = onItemsChange;
    }

    public void addOne() {
        items.add("");
        notifyItemInserted(getItemCount() - 1);
        onItemsChange.onItemAdded(getItemCount() - 1);
    }

    private void deleteItemAt(int position) {
        if (getItemCount() > 0 && position < getItemCount()) {
            items.remove(position);
            notifyItemRemoved(position);
            onItemsChange.onItemRemoved(position);
        }
    }

    public Vector<String> getItems() {
        return items;
    }

    public interface onItemsChange {
        void onItemModified(int position, @NonNull String item);

        void onItemRemoved(int position);

        void onItemAdded(int position);

        void onItemsChanged(@NonNull Vector<String> newItems);
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
        return items.size();
    }

    public void updateItems(Vector<String> newItems) {
        items = newItems;
        notifyDataSetChanged();
        onItemsChange.onItemsChanged(newItems);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText name;
        ImageButton deleteItemButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteItemButton = itemView.findViewById(R.id.imageButtonRemoveItem);
            name = itemView.findViewById(R.id.editTextErasableItem);
            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    items.remove(getAdapterPosition());
                    items.add(getAdapterPosition(), charSequence.toString());
                    onItemsChange.onItemModified(getAdapterPosition(), charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        void bind() {
            name.setText(items.get(getAdapterPosition()));
            onItemsChange.onItemModified(getAdapterPosition(), items.get(getAdapterPosition()));
            name.setHint(hint);
            deleteItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItemAt(getAdapterPosition());
                }
            });
        }
    }
}
