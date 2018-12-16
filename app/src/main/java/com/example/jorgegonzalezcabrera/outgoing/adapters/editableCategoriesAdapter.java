package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.activities.editFieldActivity.editIncomeCategoryInterface;
import com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs;
import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmList;

import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_ADD_INCOME_CATEGORY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_ADD_OUTGOING_CATEGORY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_EDIT_CATEGORY;

public class editableCategoriesAdapter extends RecyclerView.Adapter<editableCategoriesAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private RealmList<category> categories;
    private localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface;
    private editIncomeCategoryInterface editIncomeCategoryInterface;
    private int categoriesType;
    private boolean lastIsEmpty;
    private boolean showingLast;

    public editableCategoriesAdapter(Context context,
                                     localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface,
                                     final editIncomeCategoryInterface editIncomeCategoryInterface,
                                     int categoriesType) {
        this.context = context;
        this.categoriesType = categoriesType;
        this.categories = new RealmList<>();
        this.categories.addAll(Realm.getDefaultInstance().where(category.class).equalTo("type", categoriesType).equalTo("operative", true).findAll());
        this.layout = R.layout.erasable_item;
        this.onCategoriesChangeInterface = onCategoriesChangeInterface;
        this.editIncomeCategoryInterface = editIncomeCategoryInterface;
        this.lastIsEmpty = false;
        this.showingLast = false;
    }

    @NonNull
    @Override
    public editableCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new editableCategoriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull editableCategoriesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(categories.get(i));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void addOne() {
        if (!lastIsEmpty) {
            categories.add(new category("", categoriesType));
            notifyItemInserted(getItemCount() - 1);
            lastIsEmpty = true;
        }
    }

    public void modify(category modifiedCategory) {
        for (int i = 0; i < categories.size(); i++) {
            if (modifiedCategory.getId() == categories.get(i).getId()) {
                categories.remove(i);
                categories.add(i, modifiedCategory);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void confirmLast(category storedCategory) {
        if (lastIsEmpty) {
            categories.remove(getItemCount() - 1);
            categories.add(storedCategory);
            notifyItemChanged(getItemCount() - 1);
            lastIsEmpty = false;
            showingLast = false;
        }
    }

    public void cancelNewCategory() {
        if (lastIsEmpty) {
            categories.remove(getItemCount() - 1);
            notifyItemRemoved(getItemCount());
            lastIsEmpty = false;
            showingLast = false;
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if ((holder.getAdapterPosition() == (getItemCount() - 1)) && lastIsEmpty && !showingLast) {
            if (categoriesType == category.INCOME) {
                editIncomeCategoryInterface.editCategoryField("",
                        holder.container, "Income category", REQUEST_ADD_INCOME_CATEGORY, -1);
            } else {
                editIncomeCategoryInterface.editCategoryField("",
                        holder.container, "Outgoing category", REQUEST_ADD_OUTGOING_CATEGORY, -1);
            }
            showingLast = true;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout container;
        TextView categoryName;
        ImageButton removeButton;

        private final static String CONTAINER_TRANSITION_NAME = "container";

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.constraintLayout);
            categoryName = itemView.findViewById(R.id.textViewCategoryName);
            removeButton = itemView.findViewById(R.id.imageButtonRemoveItem);
        }

        void bind(final category categoryToBind) {
            container.setTransitionName(CONTAINER_TRANSITION_NAME + categoryToBind.getId());

            categoryName.setText(categoryToBind.getName());
            categoryName.setFocusable(false);
            categoryName.setFocusableInTouchMode(false);
            categoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (categoriesType == com.example.jorgegonzalezcabrera.outgoing.models.category.INCOME) {
                        editIncomeCategoryInterface.editCategoryField(categoryToBind.getName(),
                                container, "Income category", REQUEST_EDIT_CATEGORY, categoryToBind.getId());
                    } else {
                        editIncomeCategoryInterface.editCategoryField(categoryToBind.getName(),
                                container,"Outgoing category", REQUEST_EDIT_CATEGORY, categoryToBind.getId());
                    }
                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = "Choose what to do with entries of this category";
                    Vector<String> options = new Vector<>();
                    options.add("Change the category to the entries");
                    options.add("Keep entries as before");
                    options.add("Cancel");
                    dialogs.chooseOptionDialog(context, title, options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == 0) {

                                final int categoryPosition = getAdapterPosition();
                                final Vector<String> allCategories = (categoriesType == category.OUTGOING ? localUtils.getFunctioningOutgoingCategories() : localUtils.getFunctioningIncomeCategories());
                                allCategories.remove(categories.get(getAdapterPosition()).getName());

                                dialogs.chooseOptionDialog(context, "Pick a category", allCategories, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        onCategoriesChangeInterface.removeAndReplaceCategory(categories.get(categoryPosition), allCategories.get(i));
                                        categories.remove(categoryPosition);
                                        notifyItemRemoved(categoryPosition);
                                    }
                                });
                                dialogInterface.dismiss();
                            } else if (i == 1) {

                                int categoryPosition = getAdapterPosition();
                                onCategoriesChangeInterface.removeAndKeepCategory(categories.get(categoryPosition));
                                categories.remove(categoryPosition);
                                notifyItemRemoved(categoryPosition);
                                dialogInterface.dismiss();
                            } else if (i == 2) {
                                dialogInterface.cancel();
                            }
                        }
                    });
                }
            });
        }
    }
}
