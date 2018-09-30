package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.allEntriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.adapters.categoriesSelectionAdapter;
import com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.others.HeaderItemDecoration;
import com.example.jorgegonzalezcabrera.outgoing.others.HeaderItemDecoration.StickyHeaderInterface;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.Sort;

import static com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs.newDatePickerDialog;

public class actionsFragment extends Fragment implements StickyHeaderInterface {

    private allEntriesAdapter adapter;
    private RealmList<entry> allTheActions;
    private Context context;
    private EditText editTextMinValue;
    private EditText editTextMaxValue;
    private EditText editTextDescriptionFilter;
    private RecyclerView recyclerViewCategoriesSelection;
    private categoriesSelectionAdapter categoriesSelectionAdapter;
    private LinearLayout expandableFilterLayout;
    private EditText editTextMinDate;
    private EditText editTextMaxDate;
    private Date minDate;
    private Date maxDate;
    private localUtils.OnEntriesChangeInterface onEntriesChangeInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onEntriesChangeInterface = (localUtils.OnEntriesChangeInterface) context;
        } catch (Exception e) {
            onEntriesChangeInterface = new localUtils.OnEntriesChangeInterface() {
                @Override
                public void addEntry(@NonNull entry newEntry) {
                }

                @Override
                public void removeEntry(@NonNull entry removedEntry) {
                }

                @Override
                public void editEntry(@NonNull entry nextVersion) {

                }
            };
        }

        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allTheActions = new RealmList<>();
        allTheActions.addAll(Realm.getDefaultInstance().where(entry.class).findAll().sort("creationDate", Sort.ASCENDING));
        adapter = new allEntriesAdapter(context, allTheActions, onEntriesChangeInterface);
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

        expandableFilterLayout = view.findViewById(R.id.expandableFilterLayout);

        recyclerViewCategoriesSelection = view.findViewById(R.id.recyclerViewCategoriesSelection);
        categoriesSelectionAdapter = new categoriesSelectionAdapter();
        recyclerViewCategoriesSelection.setAdapter(categoriesSelectionAdapter);
        StaggeredGridLayoutManager categoriesSelectionLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        recyclerViewCategoriesSelection.setLayoutManager(categoriesSelectionLayoutManager);

        Button buttonApplyFilters = view.findViewById(R.id.buttonApplyFilters);
        Button buttonCancelFilters = view.findViewById(R.id.buttonCancelFilters);
        editTextMinValue = view.findViewById(R.id.editTextMinValue);
        editTextMaxValue = view.findViewById(R.id.editTextMaxValue);
        editTextDescriptionFilter = view.findViewById(R.id.editTextDescriptionFilter);

        buttonApplyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyFilters();
                expandableFilterLayout.setVisibility(View.GONE);
            }
        });

        buttonCancelFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cleanFilters();
                applyFilters();
                expandableFilterLayout.setVisibility(View.GONE);
            }
        });

        editTextMinDate = view.findViewById(R.id.editTextMinDate);
        editTextMinDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date initialDate = editTextMinDate.getText().toString().isEmpty() ? new Date() : minDate;
                newDatePickerDialog(initialDate, context, new dialogs.OnDateRemovedListener() {
                    @Override
                    public void onDateRemoved() {
                        editTextMinDate.setText("");
                    }
                }, new dialogs.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int month, int day) {
                        GregorianCalendar dateSet = new GregorianCalendar();
                        dateSet.set(year, month, day, 0, 0, 0);
                        dateSet.set(Calendar.MILLISECOND, 0);

                        minDate = dateSet.getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
                        editTextMinDate.setText(dateFormat.format(minDate));
                    }
                });
            }
        });
        editTextMaxDate = view.findViewById(R.id.editTextMaxDate);
        editTextMaxDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date initialDate = editTextMaxDate.getText().toString().isEmpty() ? new Date() : maxDate;
                newDatePickerDialog(initialDate, context, new dialogs.OnDateRemovedListener() {
                    @Override
                    public void onDateRemoved() {
                        editTextMaxDate.setText("");
                    }
                }, new dialogs.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int month, int day) {
                        GregorianCalendar dateSet = new GregorianCalendar();
                        dateSet.set(year, month, day, 0, 0, 0);
                        dateSet.set(Calendar.MILLISECOND, 0);
                        dateSet.add(Calendar.DATE, 1);

                        maxDate = dateSet.getTime();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
                        editTextMaxDate.setText(dateFormat.format(maxDate));
                    }
                });
            }
        });

        return view;
    }

    public void cleanFilters() {
        editTextMinValue.setText("");
        editTextMaxValue.setText("");
        editTextDescriptionFilter.setText("");
        editTextMinDate.setText("");
        editTextMaxDate.setText("");

        categoriesSelectionAdapter.ViewHolder viewHolder;
        for (int i = 0; i < categoriesSelectionAdapter.getItemCount(); i++) {
            viewHolder = (categoriesSelectionAdapter.ViewHolder) recyclerViewCategoriesSelection.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                viewHolder.checkboxCategory.setChecked(true);
            }
        }
    }

    public void expandFilters() {
        expandableFilterLayout.setVisibility(View.VISIBLE);
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

        if (!editTextMinDate.getText().toString().isEmpty()) {
            filteredResults.greaterThanOrEqualTo("creationDate", minDate);
        }

        if (!editTextMaxDate.getText().toString().isEmpty()) {
            filteredResults.lessThan("creationDate", maxDate);
        }

        allTheActions.clear();
        allTheActions.addAll(filteredResults.findAll());
        adapter.changeData(allTheActions);
    }

    public void updateDataAdded(entry newEntry) {
        adapter.newEntryAdded(newEntry);
    }

    public void updateDataModified(entry nextVersion) {
        adapter.entryModified(nextVersion);
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

    public void removeCategoryInFilters(outgoingCategory removedCategory) {
        categoriesSelectionAdapter.removeCategory(removedCategory);
    }

    public void addCategoryInFilters(outgoingCategory newOutgoingCategory) {
        categoriesSelectionAdapter.addCategory(newOutgoingCategory);
    }
}
