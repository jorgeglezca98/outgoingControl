package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.widget.ImageView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.activities.editFieldActivity.editIncomeCategoryInterface;
import com.example.jorgegonzalezcabrera.outgoing.adapters.editableIncomeCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.adapters.editableOutgoingCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.incomeCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.subcategory;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

public class settingFragment extends Fragment {

    private Context context;
    private localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface;
    private editableOutgoingCategoriesAdapter.editOutgoingCategoryInterface editOutgoingCategoryInterface;
    private editIncomeCategoryInterface editIncomeCategoryInterface;
    private editableOutgoingCategoriesAdapter outgoingCategoriesAdapter;
    private editableIncomeCategoriesAdapter incomeCategoriesAdapter;
    private RecyclerView recyclerViewEditableOutgoingCategories;
    private RecyclerView recyclerViewEditableIncomeCategories;
    private NestedScrollView scrollViewSettingFragment;
    private ImageView incomeCategoriesExpandImage;
    private ImageView outgoingCategoriesExpandImage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

        try {
            onCategoriesChangeInterface = (localUtils.OnCategoriesChangeInterface) context;
        } catch (Exception e) {
            onCategoriesChangeInterface = new localUtils.OnCategoriesChangeInterface() {
                @Override
                public void removeAndReplaceCategory(@NonNull outgoingCategory removedOutgoingCategory, @NonNull String newCategory) {

                }

                @Override
                public void removeAndKeepCategory(@NonNull outgoingCategory removedOutgoingCategory) {

                }

                @Override
                public void removeAndReplaceCategory(@NonNull incomeCategory removedIncomeCategory, @NonNull String newCategory) {

                }

                @Override
                public void removeAndKeepCategory(@NonNull incomeCategory removedIncomeCategory) {

                }

                @Override
                public void removeAndReplaceCategory(@NonNull subcategory removedSubcategory, @NonNull String newSubcategory) {

                }

                @Override
                public void removeAndKeepCategory(@NonNull subcategory removedSubcategory, @NonNull outgoingCategory category) {

                }

                @Override
                public void addedCategory(@NonNull outgoingCategory newOutgoingCategory) {

                }

                @Override
                public void addedCategory(@NonNull incomeCategory newIncomeCategory) {

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
        outgoingCategoriesAdapter = new editableOutgoingCategoriesAdapter(getContext(), onCategoriesChangeInterface, editOutgoingCategoryInterface);
        outgoingCategoriesAdapter.addOnEditCategoryFieldInterface(editIncomeCategoryInterface);
        recyclerViewEditableOutgoingCategories.setAdapter(outgoingCategoriesAdapter);
        recyclerViewEditableOutgoingCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEditableOutgoingCategories.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        recyclerViewEditableIncomeCategories = view.findViewById(R.id.recyclerViewEditableIncomeCategories);
        incomeCategoriesAdapter = new editableIncomeCategoriesAdapter(getContext(), onCategoriesChangeInterface, editIncomeCategoryInterface);
        recyclerViewEditableIncomeCategories.setAdapter(incomeCategoriesAdapter);
        recyclerViewEditableIncomeCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEditableIncomeCategories.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        scrollViewSettingFragment = view.findViewById(R.id.scrollViewSettingFragment);
        ConstraintLayout incomeCategoriesHeader = view.findViewById(R.id.incomeCategoriesHeader);
        incomeCategoriesExpandImage = view.findViewById(R.id.imageViewExpandIncomeCategories);
        ConstraintLayout outgoingCategoriesHeader = view.findViewById(R.id.outgoingCategoriesHeader);
        outgoingCategoriesExpandImage = view.findViewById(R.id.imageViewExpandOutgoingCategories);

        incomeCategoriesHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewEditableIncomeCategories.getVisibility() == View.VISIBLE) {
                    recyclerViewEditableIncomeCategories.setVisibility(View.GONE);
                    incomeCategoriesExpandImage.animate().rotation(180.0f).setDuration(500).setListener(null);
                } else if (recyclerViewEditableIncomeCategories.getVisibility() == View.GONE) {
                    recyclerViewEditableIncomeCategories.setVisibility(View.VISIBLE);
                    incomeCategoriesExpandImage.animate().rotation(0.0f).setDuration(500).setListener(null);
                    if (recyclerViewEditableOutgoingCategories.getVisibility() == View.VISIBLE) {
                        recyclerViewEditableOutgoingCategories.setVisibility(View.GONE);
                        outgoingCategoriesExpandImage.animate().rotation(180.0f).setDuration(500).setListener(null);
                    }
                    scrollViewSettingFragment.scrollTo(0, recyclerViewEditableIncomeCategories.getScrollY());
                }
            }
        });

        outgoingCategoriesHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewEditableOutgoingCategories.getVisibility() == View.VISIBLE) {
                    recyclerViewEditableOutgoingCategories.setVisibility(View.GONE);
                    outgoingCategoriesExpandImage.animate().rotation(180.0f).setDuration(500).setListener(null);
                } else if (recyclerViewEditableOutgoingCategories.getVisibility() == View.GONE) {
                    recyclerViewEditableOutgoingCategories.setVisibility(View.VISIBLE);
                    outgoingCategoriesExpandImage.animate().rotation(0.0f).setDuration(500).setListener(null);
                    if (recyclerViewEditableIncomeCategories.getVisibility() == View.VISIBLE) {
                        recyclerViewEditableIncomeCategories.setVisibility(View.GONE);
                        incomeCategoriesExpandImage.animate().rotation(180.0f).setDuration(500).setListener(null);
                    }
                    scrollViewSettingFragment.scrollTo(0, recyclerViewEditableOutgoingCategories.getScrollY());
                }
            }
        });

        return view;
    }

    public void addIncomeCategory() {
        AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                scrollViewSettingFragment.fullScroll(NestedScrollView.FOCUS_DOWN);
                incomeCategoriesAdapter.addOne();
            }
        };

        if (recyclerViewEditableIncomeCategories.getVisibility() == View.GONE) {
            recyclerViewEditableIncomeCategories.setVisibility(View.VISIBLE);
            incomeCategoriesExpandImage.animate().rotation(0.0f).setDuration(500).setListener(animatorListenerAdapter);
            if (recyclerViewEditableOutgoingCategories.getVisibility() == View.VISIBLE) {
                recyclerViewEditableOutgoingCategories.setVisibility(View.GONE);
                outgoingCategoriesExpandImage.animate().rotation(180.0f).setDuration(500).setListener(null);
            }
        } else {
            scrollViewSettingFragment.fullScroll(NestedScrollView.FOCUS_DOWN);
            incomeCategoriesAdapter.addOne();
        }
    }

    public void addOutgoingCategory() {
        AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                scrollViewSettingFragment.fullScroll(NestedScrollView.FOCUS_DOWN);
                outgoingCategoriesAdapter.addOne();
            }
        };

        if (recyclerViewEditableOutgoingCategories.getVisibility() == View.GONE) {
            recyclerViewEditableOutgoingCategories.setVisibility(View.VISIBLE);
            outgoingCategoriesExpandImage.animate().rotation(0.0f).setDuration(500).setListener(animatorListenerAdapter);
            if (recyclerViewEditableIncomeCategories.getVisibility() == View.VISIBLE) {
                recyclerViewEditableIncomeCategories.setVisibility(View.GONE);
                incomeCategoriesExpandImage.animate().rotation(180.0f).setDuration(500).setListener(null);
            }
        } else {
            scrollViewSettingFragment.fullScroll(NestedScrollView.FOCUS_DOWN);
            outgoingCategoriesAdapter.addOne();
        }
    }

    public void confirmAddedCategory(incomeCategory storedIncomeCategory) {
        incomeCategoriesAdapter.confirmLast(storedIncomeCategory);
    }

    public void newIncomeCategoryCanceled() {
        incomeCategoriesAdapter.cancelNewCategory();
    }

    public void newOutgoingCategoryCanceled() {
        outgoingCategoriesAdapter.cancelNewCategory();
    }

    public void confirmAddedCategory(outgoingCategory storedOutgoingCategory) {
        outgoingCategoriesAdapter.confirmLast(storedOutgoingCategory);
    }

    public void modifyIncomeCategory(incomeCategory modifiedIncomeCategory) {
        incomeCategoriesAdapter.modify(modifiedIncomeCategory);
    }

    public void modifyOutgoingCategory(outgoingCategory modifiedOutgoingCategory) {
        outgoingCategoriesAdapter.modify(modifiedOutgoingCategory);
    }
}
