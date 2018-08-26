package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.fragments.backSurplusMoneyFragment;
import com.example.jorgegonzalezcabrera.outgoing.fragments.frontSurplusMoneyFragment;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.subcategory;

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
    private FragmentManager fragmentManager;

    public surplusMoneyTableAdapter(FragmentManager fragmentManager, @Nonnull Vector<surplusMoneyByCategory> items) {
        this.items = new Vector<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null) {
                this.items.add(items.get(i));
            }
        }
        this.layout = R.layout.surplus_money_item;
        this.fragmentManager = fragmentManager;
    }

    public void updateData(String category, double value, boolean addedData) {
        for (int i = 0; i < items.size(); i++) {
            RealmList<subcategory> subcategories = items.get(i).category.getSubcategories();
            if (subcategories.where().equalTo("name", category).findFirst() != null) {
                if(addedData){
                    items.get(i).surplusMoney -= value;
                } else{
                    items.get(i).surplusMoney += value;
                }
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

        private FrameLayout container;
        private int viewId;
        private boolean isShowingBack;
        private frontSurplusMoneyFragment frontFragment;
        private backSurplusMoneyFragment backFragment;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.frameLayoutSurplusMoney);
            viewId = View.generateViewId();
            container.setId(viewId);
            isShowingBack = false;

            frontFragment = new frontSurplusMoneyFragment();
            backFragment = new backSurplusMoneyFragment();
            fragmentManager
                    .beginTransaction()
                    .add(viewId, frontFragment)
                    .commit();
        }

        void bind(@NonNull surplusMoneyByCategory surplusMoneyByCategory) {

            frontFragment.setData(surplusMoneyByCategory);
            backFragment.setData(surplusMoneyByCategory);

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isShowingBack) {
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(
                                        R.animator.right_in,
                                        R.animator.right_out,
                                        R.animator.left_in,
                                        R.animator.left_out)
                                .replace(viewId, frontFragment)
                                .commit();
                        isShowingBack = false;
                    } else{
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(
                                        R.animator.right_in,
                                        R.animator.right_out,
                                        R.animator.left_in,
                                        R.animator.left_out)
                                .replace(viewId, backFragment)
                                .commit();
                        isShowingBack = true;
                    }
                }
            });
        }
    }
}
