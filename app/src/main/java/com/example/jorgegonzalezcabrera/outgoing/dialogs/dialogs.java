package com.example.jorgegonzalezcabrera.outgoing.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.categoriesSpinnerAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.entry.type;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;

import java.util.Vector;

import io.realm.Realm;

public class dialogs {

    public interface OnNewEntryAccepted {
        void OnClick(String subcategory, int type, double value, String description);
    }

    public static void newEntryDialog(Context context, final OnNewEntryAccepted myInterface) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_entry_dialog);

        final EditText valueEditText = dialog.findViewById(R.id.editTextValueNewEntry);
        final Spinner categorySpinner = dialog.findViewById(R.id.spinnerCategorySelection);
        final EditText descriptionEditText = dialog.findViewById(R.id.editTextConceptNewEntry);
        Button cancelButton = dialog.findViewById(R.id.buttonCancel);
        Button applyButton = dialog.findViewById(R.id.buttonApplyNewEntry);

        final categoriesSpinnerAdapter categoriesSpinnerAdapter = new categoriesSpinnerAdapter(context);
        categorySpinner.setAdapter(categoriesSpinnerAdapter);
        categorySpinner.setSelection(1);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!valueEditText.getText().toString().isEmpty()) {
                    int typeOfCategory;
                    if (categoriesSpinnerAdapter.isOutgoingCategory(categorySpinner.getSelectedItemPosition()))
                        typeOfCategory = type.OUTGOING.ordinal();
                    else
                        typeOfCategory = type.INCOME.ordinal();

                    String subcategory = categoriesSpinnerAdapter.getItem(categorySpinner.getSelectedItemPosition());
                    String description = descriptionEditText.getText().toString();
                    double value = Double.valueOf(valueEditText.getText().toString());

                    myInterface.OnClick(subcategory, typeOfCategory, value, description);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public static void newPeriodicEntryDialog(final Context context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_periodic_entry_dialog);

        final EditText editTextValue = dialog.findViewById(R.id.editTextValueNewEntry);
        final Spinner spinnerCategories = dialog.findViewById(R.id.spinnerCategorySelection);
        final EditText editTextDescription = dialog.findViewById(R.id.editTextConceptNewEntry);
        final Spinner spinnerPeriodicityType = dialog.findViewById(R.id.spinnerPeriodicityType);
        final Spinner spinnerSelectedDates = dialog.findViewById(R.id.spinnerSelectedDates);
        final Button buttonAddPeriodicEntry = dialog.findViewById(R.id.buttonApplyNewPeriodicEntry);

        final Realm database = Realm.getDefaultInstance();

        final categoriesSpinnerAdapter categoriesSpinnerAdapter = new categoriesSpinnerAdapter(context);
        spinnerCategories.setAdapter(categoriesSpinnerAdapter);
        spinnerCategories.setSelection(1);

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
                    entry.type type;
                    if (categoriesSpinnerAdapter.isOutgoingCategory(spinnerCategories.getSelectedItemPosition()))
                        type = entry.type.OUTGOING;
                    else
                        type = entry.type.INCOME;

                    periodicEntry.periodicType frequency;
                    if (spinnerPeriodicityType.getSelectedItemPosition() == 0) {
                        frequency = periodicEntry.periodicType.ANNUAL;
                    } else if (spinnerPeriodicityType.getSelectedItemPosition() == 1) {
                        frequency = periodicEntry.periodicType.MONTHLY;
                    } else {
                        frequency = periodicEntry.periodicType.WEEKLY;
                    }

                    String category = categoriesSpinnerAdapter.getItem(spinnerCategories.getSelectedItemPosition());
                    if (category == null) {
                        Toast.makeText(context, "Error: empty fields", Toast.LENGTH_LONG).show();
                    } else {
                        int selectedDate = (Integer) spinnerSelectedDates.getSelectedItem();
                        int value = Integer.valueOf(editTextValue.getText().toString());
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

                        Toast.makeText(context, "Periodic entry added", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        dialog.show();
    }

}