package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;

import java.util.Vector;

public class surplusMoneyTableAdapter extends RecyclerView.Adapter<surplusMoneyTableAdapter.ViewHolder> {

    private int layout;
    private Vector<surplusMoneyByCategory> items;

    public surplusMoneyTableAdapter(Vector<surplusMoneyByCategory> items) {
        this.items = items;
        this.layout = R.layout.surplus_money_item;
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryName;
        private TextView surplusMoney;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.textViewCategoryName);
            surplusMoney = itemView.findViewById(R.id.textViewSurplusMoney);
        }

        public void bind(surplusMoneyByCategory surplusMoneyByCategory) {
            categoryName.setText(surplusMoneyByCategory.categoryName);
            surplusMoney.setText(String.valueOf(surplusMoneyByCategory.surplusMoney));
        }
    }


    public static class surplusMoneyByCategory{
        public String categoryName;
        public double surplusMoney;

        public surplusMoneyByCategory(String categoryName, double surplusMoney) {
            this.categoryName = categoryName;
            this.surplusMoney = surplusMoney;
        }
    }
}
