package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.subcategory;

import java.util.Locale;
import java.util.Vector;

import javax.annotation.Nonnull;

import io.realm.RealmList;

public class surplusMoneyTableAdapter extends RecyclerView.Adapter<surplusMoneyTableAdapter.ViewHolder> {

    public static class surplusMoneyByCategory {
        public outgoingCategory category;
        public double surplusMoney;

        public surplusMoneyByCategory(@Nonnull outgoingCategory category, double surplusMoney) {
            this.category = category;
            this.surplusMoney = surplusMoney;
        }
    }

    private int layout;
    private Vector<surplusMoneyByCategory> items;

    public surplusMoneyTableAdapter(@Nonnull Vector<surplusMoneyByCategory> items) {
        this.items = new Vector<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null) {
                this.items.add(items.get(i));
            }
        }
        this.layout = R.layout.surplus_money_item;
    }

    public void updateData(String category, double value) {
        for (int i = 0; i < items.size(); i++) {
            RealmList<subcategory> subcategories = items.get(i).category.getSubcategories();
            if (subcategories.where().equalTo("name", category).findFirst() != null) {
                items.get(i).surplusMoney -= value;
                notifyItemChanged(i);
                return;
            }
        }
    }

    @NonNull
    @Override
    public surplusMoneyTableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new surplusMoneyTableAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull surplusMoneyTableAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(items.get(i));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryName;
        private TextView surplusMoney;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.textViewCategoryName);
            surplusMoney = itemView.findViewById(R.id.textViewSurplusMoney);
        }

        void bind(@NonNull surplusMoneyByCategory surplusMoneyByCategory) {
            categoryName.setText(surplusMoneyByCategory.category.getName());
            String value = String.format(new Locale("es", "ES"), "%.2f", surplusMoneyByCategory.surplusMoney) + "€";
            surplusMoney.setText(value);
        }
    }
}
