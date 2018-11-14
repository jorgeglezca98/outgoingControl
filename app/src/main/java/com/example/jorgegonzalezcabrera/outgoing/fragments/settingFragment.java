package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.activities.editFieldActivity.editIncomeCategoryInterface;
import com.example.jorgegonzalezcabrera.outgoing.adapters.editableCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.adapters.editableOutgoingCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.util.Vector;

public class settingFragment extends Fragment {

    private Context context;
    private localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface;
    private editableOutgoingCategoriesAdapter.editOutgoingCategoryInterface editOutgoingCategoryInterface;
    private editIncomeCategoryInterface editIncomeCategoryInterface;
    private editableCategoriesAdapter outgoingCategoriesAdapter;
    private editableCategoriesAdapter incomeCategoriesAdapter;
    private editableOutgoingCategoriesAdapter moneyControllersAdapter;
    private RecyclerView recyclerViewMoneyControllers;
    private RecyclerView recyclerViewEditableOutgoingCategories;
    private RecyclerView recyclerViewEditableIncomeCategories;
    private NestedScrollView scrollViewSettingFragment;
    private TextView outgoingCategoriesHeader;
    private TextView incomeCategoriesHeader;
    private TextView moneyControllersHeader;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

        try {
            onCategoriesChangeInterface = (localUtils.OnCategoriesChangeInterface) context;
        } catch (Exception e) {
            onCategoriesChangeInterface = new localUtils.OnCategoriesChangeInterface() {
                @Override
                public void removeAndReplaceCategory(@NonNull category removedCategory, @NonNull String newSubcategory) {

                }

                @Override
                public void removeAndKeepCategory(@NonNull category removedCategory) {

                }

                @Override
                public void removeMoneyController(@NonNull outgoingCategory removedCategory) {

                }

                @Override
                public void changeMoneyControllerSubcategories(@NonNull outgoingCategory moneyControllers, @NonNull Vector<String> newCategories) {

                }
            };
        }

        try {
            editOutgoingCategoryInterface = (editableOutgoingCategoriesAdapter.editOutgoingCategoryInterface) context;
        } catch (Exception e) {
            editOutgoingCategoryInterface = new editableOutgoingCategoriesAdapter.editOutgoingCategoryInterface() {
                @Override
                public void edit(outgoingCategory outgoingCategory, ConstraintLayout container, EditText categoryName, EditText categoryMaximum) {

                }
            };
        }

        try {
            editIncomeCategoryInterface = (editIncomeCategoryInterface) context;
        } catch (Exception e) {
            editIncomeCategoryInterface = new editIncomeCategoryInterface() {
                @Override
                public void editCategoryField(String initialValue, ConstraintLayout container, EditText field, String hint, int requestCode, long id) {

                }
            };
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.setting_fragment, container, false);

        recyclerViewEditableOutgoingCategories = view.findViewById(R.id.recyclerViewEditableOutgoingCategories);
        outgoingCategoriesAdapter = new editableCategoriesAdapter(getContext(), onCategoriesChangeInterface, editIncomeCategoryInterface, category.OUTGOING);
        recyclerViewEditableOutgoingCategories.setAdapter(outgoingCategoriesAdapter);
        recyclerViewEditableOutgoingCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEditableOutgoingCategories.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        recyclerViewEditableIncomeCategories = view.findViewById(R.id.recyclerViewEditableIncomeCategories);
        incomeCategoriesAdapter = new editableCategoriesAdapter(getContext(), onCategoriesChangeInterface, editIncomeCategoryInterface, category.INCOME);
        recyclerViewEditableIncomeCategories.setAdapter(incomeCategoriesAdapter);
        recyclerViewEditableIncomeCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEditableIncomeCategories.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        recyclerViewMoneyControllers = view.findViewById(R.id.recyclerViewMoneyControllers);
        moneyControllersAdapter = new editableOutgoingCategoriesAdapter(getContext(), onCategoriesChangeInterface, editOutgoingCategoryInterface);
        moneyControllersAdapter.addOnEditCategoryFieldInterface(editIncomeCategoryInterface);
        recyclerViewMoneyControllers.setAdapter(moneyControllersAdapter);
        recyclerViewMoneyControllers.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMoneyControllers.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        scrollViewSettingFragment = view.findViewById(R.id.scrollViewSettingFragment);
        outgoingCategoriesHeader = view.findViewById(R.id.outgoingsCategoriesHeader);
        incomeCategoriesHeader = view.findViewById(R.id.incomeCategoriesHeader);
        moneyControllersHeader = view.findViewById(R.id.moneyControllersHeader);

        incomeCategoriesHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewEditableIncomeCategories.getVisibility() == View.VISIBLE) {
                    recyclerViewEditableIncomeCategories.setVisibility(View.GONE);
                    outgoingCategoriesHeader.setVisibility(View.VISIBLE);
                    moneyControllersHeader.setVisibility(View.VISIBLE);
                } else if (recyclerViewEditableIncomeCategories.getVisibility() == View.GONE) {
                    closeExpandableLists();
                    recyclerViewEditableIncomeCategories.setVisibility(View.VISIBLE);
                    outgoingCategoriesHeader.setVisibility(View.GONE);
                    moneyControllersHeader.setVisibility(View.GONE);
                    scrollViewSettingFragment.fullScroll(View.FOCUS_UP);
                }
            }
        });

        outgoingCategoriesHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewEditableOutgoingCategories.getVisibility() == View.VISIBLE) {
                    recyclerViewEditableOutgoingCategories.setVisibility(View.GONE);
                    incomeCategoriesHeader.setVisibility(View.VISIBLE);
                    moneyControllersHeader.setVisibility(View.VISIBLE);
                } else if (recyclerViewEditableOutgoingCategories.getVisibility() == View.GONE) {
                    closeExpandableLists();
                    recyclerViewEditableOutgoingCategories.setVisibility(View.VISIBLE);
                    incomeCategoriesHeader.setVisibility(View.GONE);
                    moneyControllersHeader.setVisibility(View.GONE);
                    scrollViewSettingFragment.fullScroll(View.FOCUS_UP);
                }
            }
        });

        moneyControllersHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewMoneyControllers.getVisibility() == View.VISIBLE) {
                    recyclerViewMoneyControllers.setVisibility(View.GONE);
                    outgoingCategoriesHeader.setVisibility(View.VISIBLE);
                    incomeCategoriesHeader.setVisibility(View.VISIBLE);
                } else if (recyclerViewMoneyControllers.getVisibility() == View.GONE) {
                    closeExpandableLists();
                    recyclerViewMoneyControllers.setVisibility(View.VISIBLE);
                    outgoingCategoriesHeader.setVisibility(View.GONE);
                    incomeCategoriesHeader.setVisibility(View.GONE);
                    scrollViewSettingFragment.fullScroll(View.FOCUS_UP);
                }
            }
        });

        return view;
    }

    private void closeExpandableLists() {
        if (recyclerViewEditableOutgoingCategories.getVisibility() == View.VISIBLE) {
            outgoingCategoriesHeader.setVisibility(View.GONE);
            recyclerViewEditableOutgoingCategories.setVisibility(View.GONE);
        } else if (recyclerViewEditableIncomeCategories.getVisibility() == View.VISIBLE) {
            outgoingCategoriesHeader.setVisibility(View.GONE);
            recyclerViewEditableIncomeCategories.setVisibility(View.GONE);
        } else if (recyclerViewMoneyControllers.getVisibility() == View.VISIBLE) {
            outgoingCategoriesHeader.setVisibility(View.GONE);
            recyclerViewMoneyControllers.setVisibility(View.GONE);
        }
    }

    public void addIncomeCategory() {
        if (recyclerViewEditableIncomeCategories.getVisibility() == View.GONE) {
            closeExpandableLists();
            recyclerViewEditableIncomeCategories.setVisibility(View.VISIBLE);
        } else {
            scrollViewSettingFragment.fullScroll(NestedScrollView.FOCUS_DOWN);
            incomeCategoriesAdapter.addOne();
        }
    }

    public void addOutgoingCategory() {
        if (recyclerViewEditableOutgoingCategories.getVisibility() == View.GONE) {
            closeExpandableLists();
            recyclerViewEditableOutgoingCategories.setVisibility(View.VISIBLE);
        } else {
            scrollViewSettingFragment.fullScroll(NestedScrollView.FOCUS_DOWN);
            outgoingCategoriesAdapter.addOne();
        }
    }

    public void confirmAddedCategory(category storedCategory) {
        if (storedCategory.getType() == category.INCOME) {
            incomeCategoriesAdapter.confirmLast(storedCategory);
        } else {
            outgoingCategoriesAdapter.confirmLast(storedCategory);
        }
    }

    public void confirmAddedMoneyController(outgoingCategory moneyController) {
        moneyControllersAdapter.confirmLast(moneyController);
    }

    public void newIncomeCategoryCanceled() {
        incomeCategoriesAdapter.cancelNewCategory();
    }

    public void newOutgoingCategoryCanceled() {
        outgoingCategoriesAdapter.cancelNewCategory();
    }

    public void newMoneyControllerCanceled() {
        moneyControllersAdapter.cancelNewCategory();
    }

    public void modifyCategory(category modifiedCategory) {
        if (modifiedCategory.getType() == category.INCOME) {
            incomeCategoriesAdapter.modify(modifiedCategory);
        } else {
            outgoingCategoriesAdapter.modify(modifiedCategory);
        }
    }

    public void addMoneyController() {
        if (recyclerViewMoneyControllers.getVisibility() == View.GONE) {
            closeExpandableLists();
            recyclerViewMoneyControllers.setVisibility(View.VISIBLE);
        } else {
            scrollViewSettingFragment.fullScroll(NestedScrollView.FOCUS_DOWN);
            moneyControllersAdapter.addOne();
        }
    }

    public void modifyOutgoingCategory(outgoingCategory modifiedOutgoingCategory) {
        moneyControllersAdapter.modify(modifiedOutgoingCategory);
    }
}