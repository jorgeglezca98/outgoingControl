package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.category;

import java.util.Vector;

public class erasableItemsAdapter extends RecyclerView.Adapter<erasableItemsAdapter.ViewHolder> {

    private int layout;
    private Vector<category> items;
    private Vector<String> errorByItem;
    private String hint;
    private onItemsChange onItemsChange;
    private customizeView customizeViewInterface;

    private static final String ERROR_MESSAGE = "Mandatory field";

    public erasableItemsAdapter(String hint, erasableItemsAdapter.onItemsChange onItemsChange, customizeView customizeViewInterface) {
        this.layout = R.layout.editable_item;
        this.items = new Vector<>();
        category firstCategory = new category();
        firstCategory.setValidId();
        this.items.add(firstCategory);
        this.errorByItem = new Vector<>();
        this.errorByItem.add(null);
        this.hint = hint;
        this.onItemsChange = onItemsChange;
        this.onItemsChange.onItemAdded(0);
        this.customizeViewInterface = customizeViewInterface;
    }

    public void addOne() {
        category firstCategory = new category();
        firstCategory.setValidId();
        this.items.add(firstCategory);
        errorByItem.add(null);
        notifyItemInserted(getItemCount() - 1);
        if (onItemsChange != null) {
            onItemsChange.onItemAdded(getItemCount() - 1);
        }
    }

    private void deleteItemAt(int position) {
        if (position >= 0 && position < getItemCount()) {
            items.remove(position);
            errorByItem.remove(position);
            notifyItemRemoved(position);
            if (onItemsChange != null) {
                onItemsChange.onItemRemoved(position);
            }
        }
    }

    public Vector<category> getItems() {
        return items;
    }

    public void showErrorMessage() {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().isEmpty() && errorByItem.get(i) == null) {
                errorByItem.remove(i);
                errorByItem.add(i, ERROR_MESSAGE);
                notifyItemChanged(i);
            } else if (!items.get(i).getName().isEmpty() && errorByItem.get(i) != null) {
                errorByItem.remove(i);
                errorByItem.add(i, null);
                notifyItemChanged(i);
            }
        }
    }

    public void removeErrorMessage() {
        for (int i = 0; i < items.size(); i++) {
            if (errorByItem.get(i) != null) {
                errorByItem.remove(i);
                errorByItem.add(i, null);
                notifyItemChanged(i);
            }
        }
    }

    public void setOnItemsChange(erasableItemsAdapter.onItemsChange onItemsChange) {
        this.onItemsChange = onItemsChange;
    }

    public interface onItemsChange {
        void onCategoryChanged(int position, @NonNull int type);

        void onNameChanged(int position, @NonNull String newName);

        void onItemRemoved(int position);

        void onItemAdded(int position);

        void onItemsChanged(@NonNull Vector<category> newItems);
    }

    public interface customizeView {
        void custom(ViewHolder vh);
    }

    public void setCustomizeViewInterface(customizeView customizeViewInterface) {
        this.customizeViewInterface = customizeViewInterface;
    }

    @NonNull
    @Override
    public erasableItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new erasableItemsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull erasableItemsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(items.get(i));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(Vector<category> newItems) {
        items = newItems;
        errorByItem = new Vector<>();
        for (int i = 0; i < items.size(); i++) {
            errorByItem.add(null);
        }
        notifyDataSetChanged();
        if (onItemsChange != null) {
            onItemsChange.onItemsChanged(newItems);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextInputLayout container;
        public EditText name;
        RadioGroup type;
        ImageButton deleteItemButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteItemButton = itemView.findViewById(R.id.imageButtonRemoveItem);
            container = itemView.findViewById(R.id.editTextContainer);
            name = itemView.findViewById(R.id.editTextErasableItem);
            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    items.get(getAdapterPosition()).setName(charSequence.toString());
                    if (onItemsChange != null) {
                        onItemsChange.onNameChanged(getAdapterPosition(), charSequence.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            type = itemView.findViewById(R.id.radioGroupType);
            type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (R.id.radioButtonOutgoing == checkedId) {
                        items.get(getAdapterPosition()).setType(category.OUTGOING);
                        onItemsChange.onCategoryChanged(getAdapterPosition(), category.OUTGOING);
                    } else {
                        items.get(getAdapterPosition()).setType(category.INCOME);
                        onItemsChange.onCategoryChanged(getAdapterPosition(), category.INCOME);
                    }
                }
            });
        }

        void bind(category categoryToBind) {
            container.setError(errorByItem.get(getAdapterPosition()));
            name.setText(categoryToBind.getName());
            if (onItemsChange != null) {
                onItemsChange.onNameChanged(getAdapterPosition(), items.get(getAdapterPosition()).getName());
            }
            container.setHint(hint);
            deleteItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItemAt(getAdapterPosition());
                }
            });
            type.check(categoryToBind.getType() == category.OUTGOING ? R.id.radioButtonOutgoing : R.id.radioButtonIncome);
            if (customizeViewInterface != null) {
                customizeViewInterface.custom(this);
            }
        }
    }
}
