package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmList;

public class editableOutgoingCategoriesAdapter extends RecyclerView.Adapter<editableOutgoingCategoriesAdapter.ViewHolder> {

    private final Context context;
    private int layout;
    private RealmList<outgoingCategory> categories;
    private localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface;
    private editOutgoingCategoryInterface editOutgoingCategoryInterface;
    private boolean lastIsEmpty;
    private boolean showingLast;

    public interface editOutgoingCategoryInterface {
        void edit(outgoingCategory outgoingCategory, ConstraintLayout container, EditText categoryName, EditText categoryMaximum);
    }

    public editableOutgoingCategoriesAdapter(Context context,
                                             @NonNull localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface,
                                             editOutgoingCategoryInterface editOutgoingCategoryInterface) {
        this.context = context;
        this.categories = new RealmList<>();
        appConfiguration currentConfiguration = Realm.getDefaultInstance().where(appConfiguration.class).findFirst();
        this.categories.addAll(currentConfiguration.getOutgoingCategories());
        this.layout = R.layout.editable_outgoing_category;
        this.onCategoriesChangeInterface = onCategoriesChangeInterface;
        this.editOutgoingCategoryInterface = editOutgoingCategoryInterface;
        this.lastIsEmpty = false;
        this.showingLast = false;
    }

    @NonNull
    @Override
    public editableOutgoingCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new editableOutgoingCategoriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull editableOutgoingCategoriesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(categories.get(i));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void addOne() {
        if (!lastIsEmpty) {
            categories.add(new outgoingCategory());
            notifyItemInserted(getItemCount() - 1);
            lastIsEmpty = true;
        }
    }

    public void confirmLast(outgoingCategory storedOutgoingCategory) {
        if (lastIsEmpty) {
            categories.remove(getItemCount() - 1);
            categories.add(storedOutgoingCategory);
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
            editOutgoingCategoryInterface.edit(categories.get(holder.getAdapterPosition()),
                    holder.layoutEditableOutgoingCategory, holder.name, holder.max);
            showingLast = true;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout layoutEditableOutgoingCategory;
        private TextInputLayout subcategoriesContainer;
        private TextInputLayout textInputLayoutMaxValue;
        private LinearLayout linearLayoutMainData;
        private EditText name;
        private EditText max;
        private EditText subcategories;
        private ImageButton expandButton;
        private ImageButton editButton;
        private ImageButton removeButton;

        private boolean expanded;

        private final static String CONTAINER_TRANSITION_NAME = "container";
        private final static String CATEGORY_NAME_TRANSITION_NAME = "categoryNameEditText";
        private final static String CATEGORY_MAXIMUM_TRANSITION_NAME = "categoryMaximumEditText";

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutEditableOutgoingCategory = itemView.findViewById(R.id.layoutEditableOutgoingCategory);
            linearLayoutMainData = itemView.findViewById(R.id.linearLayoutMainData);
            subcategoriesContainer = itemView.findViewById(R.id.subcategoriesContainer);
            textInputLayoutMaxValue = itemView.findViewById(R.id.textInputLayoutMaxValue);
            name = itemView.findViewById(R.id.editTextCategoryName);
            max = itemView.findViewById(R.id.editTextMaxValue);
            subcategories = itemView.findViewById(R.id.editTextSubcategories);
            expandButton = itemView.findViewById(R.id.imageButtonExpandCategory);
            editButton = itemView.findViewById(R.id.imageButtonEditCategory);
            removeButton = itemView.findViewById(R.id.imageButtonRemoveCategory);
            expanded = false;
        }

        void bind(final outgoingCategory category) {
            layoutEditableOutgoingCategory.setTransitionName(CONTAINER_TRANSITION_NAME + getAdapterPosition());
            name.setTransitionName(CATEGORY_NAME_TRANSITION_NAME + getAdapterPosition());
            name.setText(category.getName());
            max.setTransitionName(CATEGORY_MAXIMUM_TRANSITION_NAME + getAdapterPosition());
            max.setText(String.valueOf(category.getMaximum()));

            StringBuilder listOfSubcategories = new StringBuilder();
            for (int i = 0; i < category.getSubcategories().size(); i++) {
                listOfSubcategories.append(category.getSubcategories().get(i).getName()).append("\n");
            }
            subcategories.setText(listOfSubcategories.toString());

            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!expanded) {
                        expandButton.animate().rotation(180);
                        subcategories.setMaxLines(category.getSubcategories().size());
                        editButton.setVisibility(View.VISIBLE);
                        removeButton.setVisibility(View.VISIBLE);
                        layoutEditableOutgoingCategory.animate().translationZ(30.0f);
                        subcategoriesContainer.setVisibility(View.VISIBLE);
                        textInputLayoutMaxValue.setVisibility(View.VISIBLE);
                        linearLayoutMainData.setWeightSum(4.0f);
                    } else {
                        expandButton.animate().rotation(0);
                        subcategories.setMaxLines(1);
                        editButton.setVisibility(View.GONE);
                        removeButton.setVisibility(View.GONE);
                        layoutEditableOutgoingCategory.animate().translationZ(0.0f);
                        subcategoriesContainer.setVisibility(View.GONE);
                        textInputLayoutMaxValue.setVisibility(View.GONE);
                        linearLayoutMainData.setWeightSum(2.5f);
                    }

                    expanded = !expanded;
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
                                final Vector<String> allCategories = localUtils.getFunctioningOutgoingCategories();
                                outgoingCategory category = categories.get(getAdapterPosition());
                                for (int j = 0; j < category.getSubcategories().size(); j++) {
                                    allCategories.remove(category.getSubcategories().get(j).getName());
                                }
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

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editOutgoingCategoryInterface.edit(category, layoutEditableOutgoingCategory, name, max);
                }
            });
        }
    }
}
