package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.allEntriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.adapters.categoriesSelectionAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.others.HeaderItemDecoration;
import com.example.jorgegonzalezcabrera.outgoing.others.HeaderItemDecoration.StickyHeaderInterface;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.Sort;

public class actionsFragment extends Fragment implements StickyHeaderInterface {

    private allEntriesAdapter adapter;
    private RealmList<entry> allTheActions;
    private Context context;
    private Vector<categoriesSelectionAdapter.categoryCheckBox> categories;
    private Date minDate;
    private Date maxDate;
    private String minValue;
    private String maxValue;
    private String description;
    private filterActions filtersManipulation;
    private localUtils.OnEntriesChangeInterface onEntriesChangeInterface;

    public interface filterActions {
        void cleanFilters();

        void applyFilters(Vector<categoriesSelectionAdapter.categoryCheckBox> categories, Date minDate,
                          Date maxDate, String minValue, String maxValue, String description);
    }

    public actionsFragment() {
        Vector<String> categoryNames = localUtils.getAllCategories();
        this.categories = new Vector<>();
        for (int i = 0; i < categoryNames.size(); i++) {
            categories.add(new categoriesSelectionAdapter.categoryCheckBox(categoryNames.get(i), true));
        }
        minDate = null;
        maxDate = null;
        minValue = "";
        maxValue = "";
        description = "";
    }

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

        filtersManipulation = new filterActions() {
            @Override
            public void cleanFilters() {
                for (int i = 0; i < categories.size(); i++) {
                    categories.get(i).selected = true;
                }
                minDate = null;
                maxDate = null;
                minValue = "";
                maxValue = "";
                description = "";
                allTheActions.clear();
                allTheActions.addAll(Realm.getDefaultInstance().where(entry.class).findAll());
                adapter.changeData(allTheActions);
            }

            @Override
            public void applyFilters(Vector<categoriesSelectionAdapter.categoryCheckBox> categories, Date minDate,
                                     Date maxDate, String minValue, String maxValue, @NonNull String description) {

                RealmQuery<entry> filteredResults = Realm.getDefaultInstance().where(entry.class);

                setMinValue(minValue);
                if (!minValue.isEmpty()) {
                    filteredResults.greaterThanOrEqualTo("valor", Double.valueOf(minValue));
                }

                setMaxValue(maxValue);
                if (!maxValue.isEmpty()) {
                    filteredResults.lessThanOrEqualTo("valor", Double.valueOf(maxValue));
                }

                setDescription(description);
                if (!description.isEmpty()) {
                    filteredResults.contains("description", description);
                }

                setCategories(categories);
                for (int i = 0; i < categories.size(); i++) {
                    if (!categories.get(i).selected) {
                        filteredResults.notEqualTo("category", categories.get(i).name);
                    }
                }

                setMinDate(minDate);
                if (minDate != null) {
                    filteredResults.greaterThanOrEqualTo("creationDate", minDate);
                }

                setMaxDate(maxDate);
                if (maxDate != null) {
                    filteredResults.lessThan("creationDate", maxDate);
                }
                allTheActions.clear();
                allTheActions.addAll(filteredResults.findAll());
                adapter.changeData(allTheActions);
            }
        };

        return view;
    }

    public void updateDataAdded(entry newEntry) {
        adapter.newEntryAdded(newEntry); //TODO: check filters first
    }

    public void updateDataModified(entry nextVersion) {
        adapter.entryModified(nextVersion);
    }

    public Vector<categoriesSelectionAdapter.categoryCheckBox> getCategories() {
        return categories;
    }

    public Date getMinDate() {
        return minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public String getMinValue() {
        return minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public String getDescription() {
        return description;
    }

    public filterActions getFiltersManipulation() {
        return filtersManipulation;
    }

    public void setCategories(Vector<categoriesSelectionAdapter.categoryCheckBox> categories) {
        this.categories = categories;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void removeCategoryInFilters(String removedCategory) {
        int i = 0;
        while (i < categories.size()) {
            if (categories.get(i).name.equals(removedCategory)) {
                categories.remove(i);
                return;
            }
            i++;
        }
    }

    public void addCategoryInFilters(String newCategory) {
        categories.add(new categoriesSelectionAdapter.categoryCheckBox(newCategory, true));
    }

    public void editCategoryInFilters(String newName, String oldName) {
        int i = 0;
        while (i < categories.size()) {
            if (categories.get(i).name.equals(oldName)) {
                categories.get(i).name = newName;
                return;
            }
            i++;
        }
    }
}
