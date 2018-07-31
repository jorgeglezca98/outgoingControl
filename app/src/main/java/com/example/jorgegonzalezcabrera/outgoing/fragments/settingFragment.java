package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmList;

public class settingFragment extends Fragment {

    private Realm database;
    private EditText editTextValue;
    private Spinner spinnerCategories;
    private EditText editTextDescription;
    private Spinner spinnerPeriodicityType;
    private Spinner spinnerSelectedDates;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        editTextValue = view.findViewById(R.id.editTextValueNewEntry);
        spinnerCategories = view.findViewById(R.id.spinnerCategorySelection);
        editTextDescription = view.findViewById(R.id.editTextConceptNewEntry);
        spinnerPeriodicityType = view.findViewById(R.id.spinnerPeriodicityType);
        spinnerSelectedDates = view.findViewById(R.id.spinnerSelectedDates);
        Button buttonAddPeriodicEntry = view.findViewById(R.id.buttonApplyNewPeriodicEntry);

        database = Realm.getDefaultInstance();
        appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();

        final List<String> categories = new ArrayList<>();
        for (int i = 0; i < currentConfiguration.getOutgoingCategories().size(); i++) {
            for (int j = 0; j < currentConfiguration.getOutgoingCategories().get(i).getSubcategories().size(); j++) {
                categories.add(currentConfiguration.getOutgoingCategories().get(i).getSubcategories().get(j).getName());
            }
        }
        final int lastOutgoingCategoryPosition = categories.size() - 1;
        for (int i = 0; i < currentConfiguration.getIncomeCategories().size(); i++) {
            categories.add(currentConfiguration.getIncomeCategories().get(i).getName());
        }

        final ArrayAdapter<String> categoriesSpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categories);
        spinnerCategories.setAdapter(categoriesSpinnerAdapter);

        spinnerPeriodicityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Vector<Integer> selectedDate = new Vector<>();
                if (spinnerPeriodicityType.getSelectedItemPosition() == 0) {
                    for (int j = 1; j <= 12; j++) {
                        selectedDate.add(j);
                    }
                } else if (spinnerPeriodicityType.getSelectedItemPosition() == 1) {
                    for (int j = 1; j <= 28; j++) {
                        selectedDate.add(j);
                    }
                } else if (spinnerPeriodicityType.getSelectedItemPosition() == 2) {
                    for (int j = 1; j <= 7; j++) {
                        selectedDate.add(j);
                    }
                }
                spinnerSelectedDates.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, selectedDate));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinnerPeriodicityType.setSelection(0);
            }
        });

        buttonAddPeriodicEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!editTextValue.getText().toString().isEmpty()) {
                    entry.type type = spinnerCategories.getSelectedItemPosition() <= lastOutgoingCategoryPosition ? entry.type.OUTGOING : entry.type.INCOME;
                    periodicEntry.periodicType frequency;
                    if (spinnerPeriodicityType.getSelectedItemPosition() == 0) {
                        frequency = periodicEntry.periodicType.ANNUAL;
                    } else if (spinnerPeriodicityType.getSelectedItemPosition() == 1) {
                        frequency = periodicEntry.periodicType.MONTHLY;
                    } else {
                        frequency = periodicEntry.periodicType.WEEKLY;
                    }

                    RealmList<Integer> selectedDate = new RealmList<>();
                    selectedDate.add((Integer) spinnerSelectedDates.getSelectedItem());
                    int value = Integer.valueOf(editTextValue.getText().toString());
                    String category = categories.get(spinnerCategories.getSelectedItemPosition());
                    String description = editTextDescription.getText().toString();
                    periodicEntry newPeriodicEntry = new periodicEntry(value, type, category, description, frequency, selectedDate);

                    database.beginTransaction();
                    database.copyToRealm(newPeriodicEntry);
                    database.commitTransaction();

                    editTextValue.setText("");
                    editTextDescription.setText("");
                    spinnerPeriodicityType.setSelection(0);
                    spinnerCategories.setSelection(0);
                    spinnerSelectedDates.setSelection(0);

                    Toast.makeText(context,"Periodic entry added",Toast.LENGTH_LONG).show();

                }
            }
        });

        return view;
    }
}
