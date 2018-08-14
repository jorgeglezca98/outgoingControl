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
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.allEntriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.adapters.categoriesSelectionAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.others.HeaderItemDecoration;
import com.example.jorgegonzalezcabrera.outgoing.others.HeaderItemDecoration.StickyHeaderInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;

public class actionsFragment extends Fragment implements StickyHeaderInterface {

    private allEntriesAdapter adapter;
    private RealmList<entry> allTheActions;
    private Context context;
    private EditText editTextMinValue;
    private EditText editTextMaxValue;
    private EditText editTextDescriptionFilter;
    private RecyclerView recyclerViewCategoriesSelection;
    private categoriesSelectionAdapter categoriesSelectionAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allTheActions = new RealmList<>();
        allTheActions.addAll(Realm.getDefaultInstance().where(entry.class).findAll());
        adapter = new allEntriesAdapter(allTheActions);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actions_fragment, container, false);

        RecyclerView recyclerViewAllTheActions = view.findViewById(R.id.recyclerViewAllTheActions);
        recyclerViewAllTheActions.setAdapter(adapter);
        LinearLayoutManager actionsLayoutManager = new LinearLayoutManager(context);
        recyclerViewAllTheActions.setLayoutManager(actionsLayoutManager);
        recyclerViewAllTheActions.addItemDecoration(new HeaderItemDecoration(R.layout.entries_by_month, this));
        recyclerViewAllTheActions.addItemDecoration(new DividerItemDecoration(context, actionsLayoutManager.getOrientation()));

        final ConstraintLayout expandableFilterLayout = view.findViewById(R.id.expandableFilterLayout);
        Button filterButton = view.findViewById(R.id.buttonFilter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expandableFilterLayout.getVisibility() == View.VISIBLE) {
                    expandableFilterLayout.setVisibility(View.GONE);
                } else if (expandableFilterLayout.getVisibility() == View.GONE) {
                    expandableFilterLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        recyclerViewCategoriesSelection = view.findViewById(R.id.recyclerViewCategoriesSelection);
        categoriesSelectionAdapter = new categoriesSelectionAdapter();
        recyclerViewCategoriesSelection.setAdapter(categoriesSelectionAdapter);
        StaggeredGridLayoutManager categoriesSelectionLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        recyclerViewCategoriesSelection.setLayoutManager(categoriesSelectionLayoutManager);

        Button buttonApplyFilters = view.findViewById(R.id.buttonApplyFilters);
        editTextMinValue = view.findViewById(R.id.editTextMinValue);
        editTextMaxValue = view.findViewById(R.id.editTextMaxValue);
        editTextDescriptionFilter = view.findViewById(R.id.editTextDescriptionFilter);
        buttonApplyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyFilters();
            }
        });

        return view;
    }

    private void applyFilters() {
        RealmQuery<entry> filteredResults = Realm.getDefaultInstance().where(entry.class);
        if (!editTextMinValue.getText().toString().isEmpty()) {
            double minValue = Double.valueOf(editTextMinValue.getText().toString());
            filteredResults.greaterThanOrEqualTo("valor", minValue);
        }

        if (!editTextMaxValue.getText().toString().isEmpty()) {
            double maxValue = Double.valueOf(editTextMaxValue.getText().toString());
            filteredResults.lessThanOrEqualTo("valor", maxValue);
        }

        if (!editTextDescriptionFilter.getText().toString().isEmpty()) {
            filteredResults.contains("description", editTextDescriptionFilter.getText().toString());
        }

        categoriesSelectionAdapter.ViewHolder viewHolder;
        for (int i = 0; i < categoriesSelectionAdapter.getItemCount(); i++) {
            viewHolder = (categoriesSelectionAdapter.ViewHolder) recyclerViewCategoriesSelection.findViewHolderForAdapterPosition(i);
            if (viewHolder != null && !viewHolder.checkboxCategory.isChecked()) {
                filteredResults.notEqualTo("category", viewHolder.checkboxCategory.getText().toString());
            }
        }

        allTheActions.clear();
        allTheActions.addAll(filteredResults.findAll());
        adapter.changeData(allTheActions);
    }

    public void updateData(entry newEntry) {
        adapter.newEntryAdded(newEntry);
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        return itemPosition;
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {
        TextView date = header.findViewById(R.id.textViewMonth);
        DateFormat df = new SimpleDateFormat("MMMM 'de' yyyy", new Locale("es", "ES"));
        if (adapter.get(headerPosition) == null) {
            date.setText(df.format(adapter.get(headerPosition + 1).getCreationDate()));
        } else {
            date.setText(df.format(adapter.get(headerPosition).getCreationDate()));
        }
    }

    @Override
    public boolean isHeader(int itemPosition) {
        return adapter.get(itemPosition) == null;
    }
}
