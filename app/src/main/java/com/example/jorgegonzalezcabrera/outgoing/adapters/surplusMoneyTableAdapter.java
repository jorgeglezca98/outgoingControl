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
import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.utilities.utils;

import java.util.Date;
import java.util.Vector;

import javax.annotation.Nonnull;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

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

    public void updateData(String category, double value, boolean addData) {
        for (int i = 0; i < items.size(); i++) {
            RealmList<category> subcategories = items.get(i).category.getSubcategories();
            if (subcategories.where().equalTo("name", category).findFirst() != null) {
                if (addData) {
                    items.get(i).surplusMoney -= value;
                } else {
                    items.get(i).surplusMoney += value;
                }
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void modifyData(entry currentEntry, entry nextEntry, Date dateOfLastUpdate) {
        boolean included = !utils.areFromTheSameMonth(dateOfLastUpdate, nextEntry.getCreationDate());
        boolean deleted = !utils.areFromTheSameMonth(dateOfLastUpdate, currentEntry.getCreationDate());

        for (int i = 0; i < items.size() && !(included && deleted); i++) {
            RealmList<category> subcategories = items.get(i).category.getSubcategories();
            if (subcategories.where().equalTo("name", currentEntry.getCategory()).findFirst() != null) {
                if (utils.areFromTheSameMonth(dateOfLastUpdate, currentEntry.getCreationDate())) {
                    items.get(i).surplusMoney += currentEntry.getValor();
                    notifyItemChanged(i);
                }
                deleted = true;
            }
            if (subcategories.where().equalTo("name", nextEntry.getCategory()).findFirst() != null) {
                if (utils.areFromTheSameMonth(dateOfLastUpdate, nextEntry.getCreationDate())) {
                    items.get(i).surplusMoney -= nextEntry.getValor();
                    notifyItemChanged(i);
                }
                included = true;
            }
        }
    }

    public void updateCategoryName(outgoingCategory modifiedOutgoingCategory) {
        for (int i = 0; i < items.size(); i++) {
            if (modifiedOutgoingCategory.getId() == items.get(i).category.getId()) {
                items.get(i).category = modifiedOutgoingCategory;
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void updateCategoryMaximum(outgoingCategory modifiedOutgoingCategory) {
        for (int i = 0; i < items.size(); i++) {
            if (modifiedOutgoingCategory.getId() == items.get(i).category.getId()) {
                items.get(i).category = modifiedOutgoingCategory;
                Date date = utils.firstDateOfTheMonth(new Date());
                RealmResults<entry> entries = Realm.getDefaultInstance().where(entry.class).greaterThanOrEqualTo("creationDate", date).findAll();
                double outgoingsByCategory = 0;
                for (int j = 0; j < modifiedOutgoingCategory.getSubcategories().size(); j++) {
                    String subcategoryName = modifiedOutgoingCategory.getSubcategories().get(j).getName();
                    outgoingsByCategory += entries.where().equalTo("category", subcategoryName).sum("valor").doubleValue();
                }
                items.get(i).surplusMoney = modifiedOutgoingCategory.getMaximum() - outgoingsByCategory;
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void updateSubcategories(outgoingCategory modifiedOutgoingCategory) {
        for (int i = 0; i < items.size(); i++) {
            if (modifiedOutgoingCategory.getId() == items.get(i).category.getId()) {
                items.get(i).category = modifiedOutgoingCategory;
                Date date = utils.firstDateOfTheMonth(new Date());
                RealmResults<entry> entries = Realm.getDefaultInstance().where(entry.class).greaterThanOrEqualTo("creationDate", date).findAll();
                double outgoingsByCategory = 0;
                for (int j = 0; j < modifiedOutgoingCategory.getSubcategories().size(); j++) {
                    String subcategoryName = modifiedOutgoingCategory.getSubcategories().get(j).getName();
                    outgoingsByCategory += entries.where().equalTo("category", subcategoryName).sum("valor").doubleValue();
                }
                items.get(i).surplusMoney = modifiedOutgoingCategory.getMaximum() - outgoingsByCategory;
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
                    } else {
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

    public void refresh(@NonNull Vector<surplusMoneyByCategory> items) {
        this.items = new Vector<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null) {
                this.items.add(items.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public void addCategory(@NonNull outgoingCategory newOutgoingCategory) {
        items.add(new surplusMoneyByCategory(newOutgoingCategory, newOutgoingCategory.getMaximum()));
        notifyItemInserted(items.size() - 1);
    }

    public void removeCategory(@NonNull outgoingCategory category) {
        int position = find(category.getName());
        if (position >= 0) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    private int find(@NonNull String category) {
        int i = 0;
        while (i < items.size()) {
            outgoingCategory categoryToCompare = items.get(i).category;
            if (categoryToCompare.getName().equals(category)) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
