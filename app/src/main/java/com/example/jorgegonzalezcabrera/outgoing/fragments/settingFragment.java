package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.editableOutgoingCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.incomeCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

public class settingFragment extends Fragment {

    private Context context;
    private localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface;

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
                public void removeCategory(@NonNull incomeCategory removedOutgoingCategory) {

                }
            };
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        RecyclerView recyclerViewEditableOutgoingCategories = view.findViewById(R.id.recyclerViewEditableOutgoingCategories);
        recyclerViewEditableOutgoingCategories.setAdapter(new editableOutgoingCategoriesAdapter(getContext(), onCategoriesChangeInterface));
        recyclerViewEditableOutgoingCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewEditableOutgoingCategories.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        return view;
    }
}
