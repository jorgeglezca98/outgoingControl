package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.editableIncomeCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.adapters.editableOutgoingCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.incomeCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.others.ItemOffsetDecoration;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

public class settingFragment extends Fragment {

    private Context context;
    private localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface;
    private editableOutgoingCategoriesAdapter.editOutgoingCategoryInterface editOutgoingCategoryInterface;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.setting_fragment, container, false);

        final RecyclerView recyclerViewEditableOutgoingCategories = view.findViewById(R.id.recyclerViewEditableOutgoingCategories);
        editableOutgoingCategoriesAdapter adapter = new editableOutgoingCategoriesAdapter(getContext(), onCategoriesChangeInterface, editOutgoingCategoryInterface);
        recyclerViewEditableOutgoingCategories.setAdapter(adapter);
        recyclerViewEditableOutgoingCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEditableOutgoingCategories.addItemDecoration(new ItemOffsetDecoration(context, 5));

        final RecyclerView recyclerViewEditableIncomeCategories = view.findViewById(R.id.recyclerViewEditableIncomeCategories);
        recyclerViewEditableIncomeCategories.setAdapter(new editableIncomeCategoriesAdapter(getContext(), onCategoriesChangeInterface));
        recyclerViewEditableIncomeCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEditableIncomeCategories.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        final ScrollView scrollViewSettingFragment = view.findViewById(R.id.scrollViewSettingFragment);
        ConstraintLayout incomeCategoriesHeader = view.findViewById(R.id.incomeCategoriesHeader);
        final ImageView incomeCategoriesExpandImage = view.findViewById(R.id.imageViewExpandIncomeCategories);
        ConstraintLayout outgoingCategoriesHeader = view.findViewById(R.id.outgoingCategoriesHeader);
        final ImageView outgoingCategoriesExpandImage = view.findViewById(R.id.imageViewExpandOutgoingCategories);

        incomeCategoriesHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewEditableIncomeCategories.getVisibility() == View.VISIBLE) {
                    recyclerViewEditableIncomeCategories.setVisibility(View.GONE);
                    incomeCategoriesExpandImage.animate().rotation(180.0f).setDuration(500);
                } else if (recyclerViewEditableIncomeCategories.getVisibility() == View.GONE) {
                    recyclerViewEditableIncomeCategories.setVisibility(View.VISIBLE);
                    incomeCategoriesExpandImage.animate().rotation(0.0f).setDuration(500);
                    if (recyclerViewEditableOutgoingCategories.getVisibility() == View.VISIBLE) {
                        recyclerViewEditableOutgoingCategories.setVisibility(View.GONE);
                        outgoingCategoriesExpandImage.animate().rotation(180.0f).setDuration(500);
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
                    outgoingCategoriesExpandImage.animate().rotation(180.0f).setDuration(500);
                } else if (recyclerViewEditableOutgoingCategories.getVisibility() == View.GONE) {
                    recyclerViewEditableOutgoingCategories.setVisibility(View.VISIBLE);
                    outgoingCategoriesExpandImage.animate().rotation(0.0f).setDuration(500);
                    if (recyclerViewEditableIncomeCategories.getVisibility() == View.VISIBLE) {
                        recyclerViewEditableIncomeCategories.setVisibility(View.GONE);
                        incomeCategoriesExpandImage.animate().rotation(180.0f).setDuration(500);
                    }
                    scrollViewSettingFragment.scrollTo(0, recyclerViewEditableOutgoingCategories.getScrollY());
                }
            }
        });

        return view;
    }
}
