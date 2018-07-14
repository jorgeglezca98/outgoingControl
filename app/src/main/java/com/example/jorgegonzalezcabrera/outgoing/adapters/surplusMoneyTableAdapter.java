package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;

import java.util.Vector;

public class surplusMoneyTableAdapter extends RecyclerView.Adapter<surplusMoneyTableAdapter.ViewHolder> {

    private int layout;
    private Vector<surplusMoneyByCategory> items;

    public surplusMoneyTableAdapter(Vector<surplusMoneyByCategory> items) {
        this.items = items;
        this.layout = R.layout.surplus_money_item;
    }

    public void updateData(String category,double value){
        //TODO: optimize this method
        int i=0,j=0;
        while(i<items.size() && !items.get(i).category.getSubcategories().get(j).getName().equals(category)){
            j=0;
            while(j<items.get(i).category.getSubcategories().size() && !items.get(i).category.getSubcategories().get(j).getName().equals(category)){
                j++;
            }
            i++;
        }
        if(i<items.size())
            items.get(i).surplusMoney-=value;
        notifyItemChanged(i);
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

        void bind(surplusMoneyByCategory surplusMoneyByCategory) {
            categoryName.setText(surplusMoneyByCategory.category.getName());
            surplusMoney.setText(String.valueOf(surplusMoneyByCategory.surplusMoney + "€"));
        }
    }


    public static class surplusMoneyByCategory{
        public outgoingCategory category;
        public double surplusMoney;

        public surplusMoneyByCategory(outgoingCategory category, double surplusMoney) {
            this.category = category;
            this.surplusMoney = surplusMoney;
        }
    }
}
