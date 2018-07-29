package com.example.jorgegonzalezcabrera.outgoing.fragments;

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

    private EditText editTextValue;
    private Spinner spinnerCategories;
    private EditText editTextDescription;
    private Spinner spinnerPeriodicityType;
    private Spinner spinnerSelecetedDates;
    private Button buttonAddPeriodicEntry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        editTextValue = view.findViewById(R.id.editTextValueNewEntry);
        spinnerCategories = view.findViewById(R.id.spinnerCategorySelection);
        editTextDescription = view.findViewById(R.id.editTextConceptNewEntry);
        spinnerPeriodicityType = view.findViewById(R.id.spinnerPeriodicityType);
        spinnerSelecetedDates = view.findViewById(R.id.spinnerSelectedDates);
        buttonAddPeriodicEntry = view.findViewById(R.id.buttonApplyNewPeriodicEntry);

        Realm.getDefaultInstance().beginTransaction();
        appConfiguration currentConfiguration = Realm.getDefaultInstance().where(appConfiguration.class).findFirst();
        Realm.getDefaultInstance().commitTransaction();

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


        final ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        spinnerCategories.setAdapter(stringArrayAdapter);

        spinnerPeriodicityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Vector<Integer> selectedDate = new Vector<>();
                if (spinnerPeriodicityType.getSelectedItemPosition() == 0) {
                    for (int j = 1; j <= 12; j++) {
                        selectedDate.add(j);
                    }
                    spinnerSelecetedDates.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, selectedDate));
                } else if (spinnerPeriodicityType.getSelectedItemPosition() == 1) {
                    for (int j = 1; j <= 28; j++) {
                        selectedDate.add(j);
                    }
                    spinnerSelecetedDates.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, selectedDate));
                } else if (spinnerPeriodicityType.getSelectedItemPosition() == 2) {
                    for (int j = 1; j <= 7; j++) {
                        selectedDate.add(j);
                    }
                    spinnerSelecetedDates.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, selectedDate));
                }
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
                    selectedDate.add((Integer) spinnerSelecetedDates.getSelectedItem());
                    periodicEntry newPeriodicEntry = new periodicEntry(Integer.valueOf(editTextValue.getText().toString()), type, categories.get(spinnerCategories.getSelectedItemPosition()), editTextDescription.getText().toString(), frequency, selectedDate);
                    Realm.getDefaultInstance().beginTransaction();
                    Realm.getDefaultInstance().copyToRealm(newPeriodicEntry);
                    Realm.getDefaultInstance().commitTransaction();

                    editTextValue.setText("");
                    editTextDescription.setText("");

                    Toast.makeText(getContext(),"Periodic entry added",Toast.LENGTH_LONG).show();

                }
            }
        });

        return view;
    }
}
