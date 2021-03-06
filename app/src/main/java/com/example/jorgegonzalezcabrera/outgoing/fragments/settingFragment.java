package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.activities.editFieldActivity.editIncomeCategoryInterface;
import com.example.jorgegonzalezcabrera.outgoing.adapters.editableCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.adapters.editableOutgoingCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.adapters.editablePeriodicEntriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.models.moneyController;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

public class settingFragment extends Fragment {

    private Context context;
    private localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface;
    private editableOutgoingCategoriesAdapter.editOutgoingCategoryInterface editOutgoingCategoryInterface;
    private localUtils.changePeriodicEntriesInterface changePeriodicEntriesInterface;
    private manageViewsWithChanges manageViewsWithChangesInterface;
    private editIncomeCategoryInterface editIncomeCategoryInterface;
    private editableCategoriesAdapter outgoingCategoriesAdapter;
    private editableCategoriesAdapter incomeCategoriesAdapter;
    private editablePeriodicEntriesAdapter periodicEntriesAdapter;
    private editableOutgoingCategoriesAdapter moneyControllersAdapter;
    private RecyclerView recyclerViewMoneyControllers;
    private RecyclerView recyclerViewEditableOutgoingCategories;
    private RecyclerView recyclerViewEditableIncomeCategories;
    private RecyclerView recyclerViewPeriodicEntries;
    private NestedScrollView scrollViewSettingFragment;
    private TextView outgoingCategoriesHeader;
    private TextView incomeCategoriesHeader;
    private TextView moneyControllersHeader;
    private TextView periodicEntriesHeader;
    private FloatingActionButton addOutgoingCategory;
    private FloatingActionButton addIncomeCategory;
    private FloatingActionButton addMoneyController;
    private FloatingActionButton addPeriodicEntry;

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
                public void removeMoneyController(@NonNull moneyController removedCategory) {

                }

            };
        }

        try {
            editOutgoingCategoryInterface = (editableOutgoingCategoriesAdapter.editOutgoingCategoryInterface) context;
        } catch (Exception e) {
            editOutgoingCategoryInterface = new editableOutgoingCategoriesAdapter.editOutgoingCategoryInterface() {
                @Override
                public void editMoneyController(moneyController moneyController, ConstraintLayout container, int requestCode) {

                }
            };
        }

        try {
            editIncomeCategoryInterface = (editIncomeCategoryInterface) context;
        } catch (Exception e) {
            editIncomeCategoryInterface = new editIncomeCategoryInterface() {
                @Override
                public void editCategoryField(String initialValue, ConstraintLayout container, String hint, int requestCode, long id) {

                }
            };
        }

        try {
            changePeriodicEntriesInterface = (localUtils.changePeriodicEntriesInterface) context;
        } catch (Exception e) {
            changePeriodicEntriesInterface = new localUtils.changePeriodicEntriesInterface() {
                @Override
                public void edit(periodicEntry periodicEntry, ConstraintLayout container, int requestCode) {

                }

                @Override
                public void remove(periodicEntry periodicEntry) {

                }
            };
        }

        try {
            manageViewsWithChangesInterface = (manageViewsWithChanges) context;
        } catch (Exception e) {
            manageViewsWithChangesInterface = new manageViewsWithChanges() {
                @Override
                public void openedSettingSection(boolean someOpen) {

                }
            };
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.setting_fragment, container, false);

        recyclerViewEditableOutgoingCategories = view.findViewById(R.id.recyclerViewEditableOutgoingCategories);
        outgoingCategoriesAdapter = new editableCategoriesAdapter(getContext(), onCategoriesChangeInterface, editIncomeCategoryInterface, category.typeOfCategory.OUTGOING.ordinal());
        recyclerViewEditableOutgoingCategories.setAdapter(outgoingCategoriesAdapter);
        recyclerViewEditableOutgoingCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEditableOutgoingCategories.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        recyclerViewEditableIncomeCategories = view.findViewById(R.id.recyclerViewEditableIncomeCategories);
        incomeCategoriesAdapter = new editableCategoriesAdapter(getContext(), onCategoriesChangeInterface, editIncomeCategoryInterface, category.typeOfCategory.INCOME.ordinal());
        recyclerViewEditableIncomeCategories.setAdapter(incomeCategoriesAdapter);
        recyclerViewEditableIncomeCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEditableIncomeCategories.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        recyclerViewMoneyControllers = view.findViewById(R.id.recyclerViewMoneyControllers);
        moneyControllersAdapter = new editableOutgoingCategoriesAdapter(onCategoriesChangeInterface, editOutgoingCategoryInterface);
        recyclerViewMoneyControllers.setAdapter(moneyControllersAdapter);
        recyclerViewMoneyControllers.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMoneyControllers.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        recyclerViewPeriodicEntries = view.findViewById(R.id.recyclerViewPeriodicEntries);
        periodicEntriesAdapter = new editablePeriodicEntriesAdapter(changePeriodicEntriesInterface);
        recyclerViewPeriodicEntries.setAdapter(periodicEntriesAdapter);
        recyclerViewPeriodicEntries.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPeriodicEntries.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        scrollViewSettingFragment = view.findViewById(R.id.scrollViewSettingFragment);
        outgoingCategoriesHeader = view.findViewById(R.id.outgoingsCategoriesHeader);
        incomeCategoriesHeader = view.findViewById(R.id.incomeCategoriesHeader);
        moneyControllersHeader = view.findViewById(R.id.moneyControllersHeader);
        periodicEntriesHeader = view.findViewById(R.id.periodicEntriesHeader);

        addOutgoingCategory = view.findViewById(R.id.addOutgoingCategory);
        addOutgoingCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOutgoingCategory();
            }
        });

        addIncomeCategory = view.findViewById(R.id.addIncomeCategory);
        addIncomeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIncomeCategory();
            }
        });

        addMoneyController = view.findViewById(R.id.addMoneyControllerCategory);
        addMoneyController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMoneyController();
            }
        });

        addPeriodicEntry = view.findViewById(R.id.addPeriodicEntry);
        addPeriodicEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPeriodicEntry();
            }
        });

        incomeCategoriesHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewEditableIncomeCategories.getVisibility() == View.VISIBLE) {
                    recyclerViewEditableIncomeCategories.setVisibility(View.GONE);
                    outgoingCategoriesHeader.setVisibility(View.VISIBLE);
                    moneyControllersHeader.setVisibility(View.VISIBLE);
                    periodicEntriesHeader.setVisibility(View.VISIBLE);
                    addIncomeCategory.hide();
                    manageViewsWithChangesInterface.openedSettingSection(false);
                } else if (recyclerViewEditableIncomeCategories.getVisibility() == View.GONE) {
                    recyclerViewEditableIncomeCategories.setVisibility(View.VISIBLE);
                    outgoingCategoriesHeader.setVisibility(View.GONE);
                    moneyControllersHeader.setVisibility(View.GONE);
                    periodicEntriesHeader.setVisibility(View.GONE);
                    scrollViewSettingFragment.fullScroll(View.FOCUS_UP);
                    addIncomeCategory.show();
                    manageViewsWithChangesInterface.openedSettingSection(true);
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
                    periodicEntriesHeader.setVisibility(View.VISIBLE);
                    addOutgoingCategory.hide();
                    manageViewsWithChangesInterface.openedSettingSection(false);
                } else if (recyclerViewEditableOutgoingCategories.getVisibility() == View.GONE) {
                    recyclerViewEditableOutgoingCategories.setVisibility(View.VISIBLE);
                    incomeCategoriesHeader.setVisibility(View.GONE);
                    moneyControllersHeader.setVisibility(View.GONE);
                    periodicEntriesHeader.setVisibility(View.GONE);
                    scrollViewSettingFragment.fullScroll(View.FOCUS_UP);
                    addOutgoingCategory.show();
                    manageViewsWithChangesInterface.openedSettingSection(true);
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
                    periodicEntriesHeader.setVisibility(View.VISIBLE);
                    addMoneyController.hide();
                    manageViewsWithChangesInterface.openedSettingSection(false);
                } else if (recyclerViewMoneyControllers.getVisibility() == View.GONE) {
                    recyclerViewMoneyControllers.setVisibility(View.VISIBLE);
                    outgoingCategoriesHeader.setVisibility(View.GONE);
                    incomeCategoriesHeader.setVisibility(View.GONE);
                    periodicEntriesHeader.setVisibility(View.GONE);
                    scrollViewSettingFragment.fullScroll(View.FOCUS_UP);
                    addMoneyController.show();
                    manageViewsWithChangesInterface.openedSettingSection(true);
                }
            }
        });

        periodicEntriesHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewPeriodicEntries.getVisibility() == View.VISIBLE) {
                    recyclerViewPeriodicEntries.setVisibility(View.GONE);
                    outgoingCategoriesHeader.setVisibility(View.VISIBLE);
                    incomeCategoriesHeader.setVisibility(View.VISIBLE);
                    moneyControllersHeader.setVisibility(View.VISIBLE);
                    addPeriodicEntry.hide();
                    manageViewsWithChangesInterface.openedSettingSection(false);
                } else if (recyclerViewPeriodicEntries.getVisibility() == View.GONE) {
                    recyclerViewPeriodicEntries.setVisibility(View.VISIBLE);
                    outgoingCategoriesHeader.setVisibility(View.GONE);
                    incomeCategoriesHeader.setVisibility(View.GONE);
                    moneyControllersHeader.setVisibility(View.GONE);
                    scrollViewSettingFragment.fullScroll(View.FOCUS_UP);
                    addPeriodicEntry.show();
                    manageViewsWithChangesInterface.openedSettingSection(true);
                }
            }
        });

        return view;
    }

    public void addIncomeCategory() {
        if (recyclerViewEditableIncomeCategories.getVisibility() == View.GONE) {
            recyclerViewEditableIncomeCategories.setVisibility(View.VISIBLE);
        } else {
            scrollViewSettingFragment.fullScroll(NestedScrollView.FOCUS_DOWN);
            incomeCategoriesAdapter.addOne();
        }
    }

    public void addOutgoingCategory() {
        if (recyclerViewEditableOutgoingCategories.getVisibility() == View.GONE) {
            recyclerViewEditableOutgoingCategories.setVisibility(View.VISIBLE);
        } else {
            scrollViewSettingFragment.fullScroll(NestedScrollView.FOCUS_DOWN);
            outgoingCategoriesAdapter.addOne();
        }
    }

    public void confirmAddedCategory(category storedCategory) {
        if (storedCategory.getType() == category.typeOfCategory.INCOME.ordinal()) {
            incomeCategoriesAdapter.confirmLast(storedCategory);
        } else {
            outgoingCategoriesAdapter.confirmLast(storedCategory);
        }
    }

    public void confirmAddedMoneyController(moneyController moneyController) {
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

    public void newPeriodicEntryCanceled() {
        periodicEntriesAdapter.cancelNewCategory();
    }

    public void modifyCategory(category modifiedCategory) {
        if (modifiedCategory.getType() == category.typeOfCategory.INCOME.ordinal()) {
            incomeCategoriesAdapter.modify(modifiedCategory);
        } else {
            outgoingCategoriesAdapter.modify(modifiedCategory);
        }
    }

    public void addMoneyController() {
        if (recyclerViewMoneyControllers.getVisibility() == View.GONE) {
            recyclerViewMoneyControllers.setVisibility(View.VISIBLE);
        } else {
            scrollViewSettingFragment.fullScroll(NestedScrollView.FOCUS_DOWN);
            moneyControllersAdapter.addOne();
        }
    }

    public void modifyOutgoingCategory(moneyController modifiedMoneyController) {
        moneyControllersAdapter.modify(modifiedMoneyController);
    }

    public void addPeriodicEntry(periodicEntry addedPeriodicEntry) {
        periodicEntriesAdapter.add(addedPeriodicEntry);
    }

    private void addPeriodicEntry() {
        if (recyclerViewPeriodicEntries.getVisibility() == View.GONE) {
            recyclerViewPeriodicEntries.setVisibility(View.VISIBLE);
        } else {
            scrollViewSettingFragment.fullScroll(NestedScrollView.FOCUS_DOWN);
            periodicEntriesAdapter.addOne();
        }
    }

    public void confirmPeriodicEntry(periodicEntry periodicEntry) {
        periodicEntriesAdapter.confirmLast(periodicEntry);
    }

    public void modifyPeriodicEntry(periodicEntry newPeriodicEntry) {
        periodicEntriesAdapter.modify(newPeriodicEntry);
    }

    public interface manageViewsWithChanges{
        void openedSettingSection(boolean someOpen);
    }

    public void closeSections (){
        recyclerViewPeriodicEntries.setVisibility(View.GONE);
        recyclerViewMoneyControllers.setVisibility(View.GONE);
        recyclerViewEditableIncomeCategories.setVisibility(View.GONE);
        recyclerViewEditableOutgoingCategories.setVisibility(View.GONE);
        outgoingCategoriesHeader.setVisibility(View.VISIBLE);
        incomeCategoriesHeader.setVisibility(View.VISIBLE);
        moneyControllersHeader.setVisibility(View.VISIBLE);
        periodicEntriesHeader.setVisibility(View.VISIBLE);
        addPeriodicEntry.hide();
        addIncomeCategory.hide();
        addOutgoingCategory.hide();
        addMoneyController.hide();
        manageViewsWithChangesInterface.openedSettingSection(false);
    }
}