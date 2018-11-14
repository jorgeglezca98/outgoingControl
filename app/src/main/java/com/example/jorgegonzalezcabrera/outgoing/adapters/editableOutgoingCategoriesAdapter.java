package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.activities.editFieldActivity;
import com.example.jorgegonzalezcabrera.outgoing.models.moneyController;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.util.ArrayList;
import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmList;

import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_EDIT_MONEY_CONTROLLER_MAXIMUM;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_EDIT_OUTGOING_CATEGORY_NAME;

public class editableOutgoingCategoriesAdapter extends RecyclerView.Adapter<editableOutgoingCategoriesAdapter.ViewHolder> {

    private final Context context;
    private int layout;
    private RealmList<moneyController> categories;
    private localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface;
    private editFieldActivity.editIncomeCategoryInterface onEditCategoryFieldInterface;
    private editOutgoingCategoryInterface editOutgoingCategoryInterface;
    private boolean lastIsEmpty;
    private boolean showingLast;

    public interface editOutgoingCategoryInterface {
        void edit(moneyController moneyController, ConstraintLayout container, EditText categoryName, EditText categoryMaximum);
    }

    public editableOutgoingCategoriesAdapter(Context context,
                                             @NonNull localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface,
                                             editOutgoingCategoryInterface editOutgoingCategoryInterface) {
        this.context = context;
        this.categories = new RealmList<>();
        this.categories.addAll(Realm.getDefaultInstance().where(moneyController.class).findAll());
        this.layout = R.layout.editable_outgoing_category;
        this.onCategoriesChangeInterface = onCategoriesChangeInterface;
        this.editOutgoingCategoryInterface = editOutgoingCategoryInterface;
        this.onEditCategoryFieldInterface = new editFieldActivity.editIncomeCategoryInterface() {
            @Override
            public void editCategoryField(String initialValue, ConstraintLayout container, TextView field, String hint, int requestCode, long id) {

            }
        };
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
            categories.add(new moneyController());
            notifyItemInserted(getItemCount() - 1);
            lastIsEmpty = true;
        }
    }

    public void confirmLast(moneyController storedMoneyController) {
        if (lastIsEmpty) {
            categories.remove(getItemCount() - 1);
            categories.add(storedMoneyController);
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

    public void addOnEditCategoryFieldInterface(editFieldActivity.editIncomeCategoryInterface onEditCategoryFieldInterface) {
        this.onEditCategoryFieldInterface = onEditCategoryFieldInterface;
    }

    public void modify(moneyController modifiedMoneyController) {
        for (int i = 0; i < categories.size(); i++) {
            if (modifiedMoneyController.getId() == categories.get(i).getId()) {
                notifyItemChanged(i);
                return;
            }
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
        private RecyclerView subcategoriesRecyclerView;
        private TextInputLayout textInputLayoutMaxValue;
        private LinearLayout linearLayoutMainData;
        private EditText name;
        private EditText max;
        private ImageButton expandButton;
        private ImageButton removeButton;

        private boolean expanded;

        private final static String CONTAINER_TRANSITION_NAME = "container";
        private final static String CATEGORY_NAME_TRANSITION_NAME = "categoryNameEditText";
        private final static String CATEGORY_MAXIMUM_TRANSITION_NAME = "categoryMaximumEditText";

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutEditableOutgoingCategory = itemView.findViewById(R.id.layoutEditableOutgoingCategory);
            linearLayoutMainData = itemView.findViewById(R.id.linearLayoutMainData);
            subcategoriesRecyclerView = itemView.findViewById(R.id.recyclerViewSubcategories);
            textInputLayoutMaxValue = itemView.findViewById(R.id.textInputLayoutMaxValue);
            name = itemView.findViewById(R.id.editTextCategoryName);
            max = itemView.findViewById(R.id.editTextMaxValue);
            expandButton = itemView.findViewById(R.id.imageButtonExpandCategory);
            removeButton = itemView.findViewById(R.id.imageButtonRemoveCategory);
            expanded = false;
        }

        void bind(final moneyController category) {
            layoutEditableOutgoingCategory.setTransitionName(CONTAINER_TRANSITION_NAME + getAdapterPosition());

            name.setTransitionName(CATEGORY_NAME_TRANSITION_NAME + getAdapterPosition());
            name.setText(category.getName());
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEditCategoryFieldInterface.editCategoryField(
                            name.getText().toString(), layoutEditableOutgoingCategory, name, "Category name", REQUEST_EDIT_OUTGOING_CATEGORY_NAME, category.getId());
                }
            });

            max.setTransitionName(CATEGORY_MAXIMUM_TRANSITION_NAME + getAdapterPosition());
            max.setText(String.valueOf(category.getMaximum()));
            max.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEditCategoryFieldInterface.editCategoryField(
                            max.getText().toString(), layoutEditableOutgoingCategory, max, "Maximum", REQUEST_EDIT_MONEY_CONTROLLER_MAXIMUM, category.getId());
                }
            });

            final categoriesSelectionAdapter subcategoriesAdapter = new categoriesSelectionAdapter(categoriesSelectionAdapter.FUNCTIONING_OUTGOING_CATEGORIES);
            ArrayList<String> subcategories = new ArrayList<>();
            for (int i = 0; i < category.getSubcategories().size(); i++) {
                subcategories.add(category.getSubcategories().get(i).getName());
            }
            subcategoriesAdapter.markAsChecked(subcategories);
            subcategoriesRecyclerView.setAdapter(subcategoriesAdapter);
            subcategoriesRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            layoutEditableOutgoingCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.subcategories_dialog);

                    RecyclerView recyclerViewSubcategories = dialog.findViewById(R.id.recyclerViewSubcategories);
                    final categoriesSelectionAdapter adapter = new categoriesSelectionAdapter(categoriesSelectionAdapter.FUNCTIONING_OUTGOING_CATEGORIES);
                    ArrayList<String> categoriesToCheck = new ArrayList<>();
                    for (int i = 0; i < category.getSubcategories().size(); i++) {
                        categoriesToCheck.add(category.getSubcategories().get(i).getName());
                    }
                    adapter.markAsChecked(categoriesToCheck);
                    recyclerViewSubcategories.setAdapter(adapter);
                    recyclerViewSubcategories.setLayoutManager(new LinearLayoutManager(context));

                    Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
                    buttonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });

                    Button buttonApply = dialog.findViewById(R.id.buttonApply);
                    buttonApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Vector<String> newCategories = new Vector<>();
                            for (int i = 0; i < adapter.getCategories().size(); i++) {
                                if (adapter.getCategories().get(i).selected) {
                                    newCategories.add(adapter.getCategories().get(i).name);
                                }
                            }
                            onCategoriesChangeInterface.changeMoneyControllerSubcategories(category, newCategories);
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!expanded) {
                        expandButton.animate().rotation(180);
                        removeButton.setVisibility(View.VISIBLE);
                        layoutEditableOutgoingCategory.animate().translationZ(30.0f);
                        subcategoriesRecyclerView.setVisibility(View.VISIBLE);
                        textInputLayoutMaxValue.setVisibility(View.VISIBLE);
                        linearLayoutMainData.setWeightSum(4.0f);
                        max.setClickable(true);
                        name.setClickable(true);
                    } else {
                        expandButton.animate().rotation(0);
                        removeButton.setVisibility(View.GONE);
                        layoutEditableOutgoingCategory.animate().translationZ(0.0f);
                        subcategoriesRecyclerView.setVisibility(View.GONE);
                        textInputLayoutMaxValue.setVisibility(View.GONE);
                        linearLayoutMainData.setWeightSum(2.5f);
                        max.setClickable(false);
                        name.setClickable(false);
                    }

                    expanded = !expanded;
                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    categories.remove(pos);
                    notifyItemRemoved(pos);
                    onCategoriesChangeInterface.removeMoneyController(category);
                }
            });
        }
    }
}
