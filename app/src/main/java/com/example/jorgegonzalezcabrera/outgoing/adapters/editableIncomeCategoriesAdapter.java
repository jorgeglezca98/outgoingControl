package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.activities.editFieldActivity.editIncomeCategoryInterface;
import com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.incomeCategory;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmList;

import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_ADD_INCOME_CATEGORY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_EDIT_INCOME_CATEGORY;

public class editableIncomeCategoriesAdapter extends RecyclerView.Adapter<editableIncomeCategoriesAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private RealmList<incomeCategory> categories;
    private localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface;
    private editIncomeCategoryInterface editIncomeCategoryInterface;
    private boolean lastIsEmpty;
    private boolean showingLast;

    public editableIncomeCategoriesAdapter(Context context,
                                           localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface,
                                           final editIncomeCategoryInterface editIncomeCategoryInterface) {
        this.context = context;
        this.categories = new RealmList<>();
        appConfiguration currentConfiguration = Realm.getDefaultInstance().where(appConfiguration.class).findFirst();
        this.categories.addAll(currentConfiguration.getIncomeCategories());
        this.layout = R.layout.erasable_item;
        this.onCategoriesChangeInterface = onCategoriesChangeInterface;
        this.editIncomeCategoryInterface = editIncomeCategoryInterface;
        this.lastIsEmpty = false;
        this.showingLast = false;
    }

    @NonNull
    @Override
    public editableIncomeCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new editableIncomeCategoriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull editableIncomeCategoriesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(categories.get(i));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void addOne() {
        if (!lastIsEmpty) {
            categories.add(new incomeCategory());
            notifyItemInserted(getItemCount() - 1);
            lastIsEmpty = true;
        }
    }

    public void modify(incomeCategory modifiedIncomeCategory) {
        for (int i = 0; i < categories.size(); i++) {
            if (modifiedIncomeCategory.getId() == categories.get(i).getId()) {
                categories.remove(i);
                categories.add(i, modifiedIncomeCategory);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void confirmLast(incomeCategory storedIncomeCategory) {
        if (lastIsEmpty) {
            categories.remove(getItemCount() - 1);
            categories.add(storedIncomeCategory);
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
            editIncomeCategoryInterface.editCategoryField("",
                    holder.container, holder.categoryName, "Income category name", REQUEST_ADD_INCOME_CATEGORY, -1);
            showingLast = true;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout container;
        EditText categoryName;
        ImageButton removeButton;

        private final static String CONTAINER_TRANSITION_NAME = "container";
        private final static String CATEGORY_NAME_TRANSITION_NAME = "categoryNameEditText";

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.constraintLayout);
            categoryName = itemView.findViewById(R.id.editTextErasableItem);
            removeButton = itemView.findViewById(R.id.imageButtonRemoveItem);
        }

        void bind(final incomeCategory incomeCategory) {
            container.setTransitionName(CONTAINER_TRANSITION_NAME + getAdapterPosition());
            categoryName.setTransitionName(CATEGORY_NAME_TRANSITION_NAME + getAdapterPosition());

            categoryName.setText(incomeCategory.getName());
            categoryName.setFocusable(false);
            categoryName.setFocusableInTouchMode(false);
            categoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editIncomeCategoryInterface.editCategoryField(incomeCategory.getName(),
                            container, categoryName, "Income category name", REQUEST_EDIT_INCOME_CATEGORY, incomeCategory.getId());
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
                                final Vector<String> allCategories = localUtils.getFunctioningIncomeCategories();
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
