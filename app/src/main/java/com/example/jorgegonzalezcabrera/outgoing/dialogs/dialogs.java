package com.example.jorgegonzalezcabrera.outgoing.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.categoriesSpinnerAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.entry.type;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import io.realm.Realm;

import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getFunctioningIncomeCategories;
import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getFunctioningOutgoingCategories;
import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getTypeFromOrdinal;

public class dialogs {

    public static void newEntryDialog(final Context context, final localUtils.OnEntriesChangeInterface onEntriesChange) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_entry_dialog);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogWindow.setWindowAnimations(R.style.DialogAnimation);
        }

        final ConstraintLayout dialogContainer = dialog.findViewById(R.id.dialogContainer);
        final EditText valueEditText = dialog.findViewById(R.id.editTextValueNewEntry);
        final EditText categorySelectionEditText = dialog.findViewById(R.id.editTextCategorySelection);
        final EditText descriptionEditText = dialog.findViewById(R.id.editTextConceptNewEntry);
        final EditText datePickerEditText = dialog.findViewById(R.id.editTextEntryDate);
        Button cancelButton = dialog.findViewById(R.id.buttonCancel);
        Button applyButton = dialog.findViewById(R.id.buttonApplyNewEntry);

        final PopupMenu popup = new PopupMenu(context, categorySelectionEditText);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                categorySelectionEditText.setText(menuItem.getTitle());
                return true;
            }
        });
        final List<String> outgoingCategories = getFunctioningOutgoingCategories();
        for (int i = 0; i < outgoingCategories.size(); i++) {
            popup.getMenu().add(0, Menu.NONE, i, outgoingCategories.get(i));
        }
        int firstIncomeCategory = outgoingCategories.size();
        List<String> incomeCategories = getFunctioningIncomeCategories();
        for (int i = 0; i < incomeCategories.size(); i++) {
            popup.getMenu().add(1, Menu.NONE, firstIncomeCategory + i, incomeCategories.get(i));
        }

        categorySelectionEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();
            }
        });

        final GregorianCalendar creationDate = new GregorianCalendar();
        creationDate.setTime(new Date());
        String initialDate = creationDate.get(Calendar.DAY_OF_MONTH) + "/" + creationDate.get(Calendar.MONTH) + "/" + creationDate.get(Calendar.YEAR);
        datePickerEditText.setText(initialDate);
        datePickerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogContainer.setVisibility(View.INVISIBLE);
                newDatePickerDialog(creationDate.getTime(), context, new OnDateRemovedListener() {
                    @Override
                    public void onDateRemoved() {
                        dialogContainer.setVisibility(View.VISIBLE);
                    }
                }, new OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int month, int day) {
                        dialogContainer.setVisibility(View.VISIBLE);
                        creationDate.set(year, month, day);
                        String updatedDate = day + "/" + month + "/" + year;
                        datePickerEditText.setText(updatedDate);
                    }
                });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!valueEditText.getText().toString().isEmpty() && !categorySelectionEditText.getText().toString().isEmpty()) {
                    int typeOfCategory;
                    if (outgoingCategories.contains(categorySelectionEditText.getText().toString()))
                        typeOfCategory = type.OUTGOING.ordinal();
                    else
                        typeOfCategory = type.INCOME.ordinal();

                    String subcategory = categorySelectionEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    double value = Double.valueOf(valueEditText.getText().toString());

                    onEntriesChange.addEntry(new entry(value, getTypeFromOrdinal(typeOfCategory), subcategory, description, creationDate.getTime()));
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public static void editEntryDialog(Context context, @NonNull final entry lastVersion, final localUtils.OnEntriesChangeInterface onEntriesChange) {
        final Dialog dialog = new Dialog(context, R.style.DialogAnimation);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_entry_dialog);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setWindowAnimations(R.style.DialogAnimation);
        }

        final EditText valueEditText = dialog.findViewById(R.id.editTextValueNewEntry);
        valueEditText.setText(String.valueOf(lastVersion.getValor()));

        final EditText descriptionEditText = dialog.findViewById(R.id.editTextConceptNewEntry);
        descriptionEditText.setText(lastVersion.getDescription());

        final Spinner categorySpinner = dialog.findViewById(R.id.spinnerCategorySelection);
        final categoriesSpinnerAdapter categoriesSpinnerAdapter = new categoriesSpinnerAdapter(context);
        categorySpinner.setAdapter(categoriesSpinnerAdapter);
        categorySpinner.setSelection(categoriesSpinnerAdapter.getPosition(lastVersion.getCategory()));

        final DatePicker datePicker = dialog.findViewById(R.id.datePicker);
        datePicker.setMaxDate((new Date()).getTime());
        GregorianCalendar lastVersionDate = new GregorianCalendar();
        lastVersionDate.setTime(lastVersion.getCreationDate());
        datePicker.updateDate(lastVersionDate.get(Calendar.YEAR), lastVersionDate.get(Calendar.MONTH), lastVersionDate.get(Calendar.DAY_OF_MONTH));

        Button cancelButton = dialog.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        Button applyButton = dialog.findViewById(R.id.buttonApplyNewEntry);
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

                    GregorianCalendar creationDate = new GregorianCalendar();
                    creationDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                    entry nextVersion = new entry(value, getTypeFromOrdinal(typeOfCategory), subcategory, description, creationDate.getTime());
                    //TODO: look for another option that avoids using this method
                    nextVersion.setId(lastVersion.getId());
                    onEntriesChange.editEntry(nextVersion);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public static void newPeriodicEntryDialog(final Context context) {
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
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public static void newDatePickerDialog(Date currentDate,
                                           Context context,
                                           final OnDateRemovedListener onDateRemovedListener,
                                           final OnDateSetListener onDateSetListener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.date_picker_dialog);

        final DatePicker datePicker = dialog.findViewById(R.id.datePicker);
        datePicker.setMaxDate((new Date()).getTime());

        GregorianCalendar updatedDate = new GregorianCalendar();
        updatedDate.setTime(currentDate);
        datePicker.updateDate(updatedDate.get(Calendar.YEAR), updatedDate.get(Calendar.MONTH), updatedDate.get(Calendar.DAY_OF_MONTH));

        Button buttonRemove = dialog.findViewById(R.id.buttonRemoveDatePicker);
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDateRemovedListener.onDateRemoved();
                dialog.dismiss();
            }
        });

        Button buttonSetDate = dialog.findViewById(R.id.buttonSetDate);
        buttonSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDateSetListener.onDateSet(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public static void chooseOptionDialog(Context context, String title, Vector<String> options, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        String[] formattedOptions = new String[options.size()];
        formattedOptions = options.toArray(formattedOptions);
        dialogBuilder.setTitle(title).setItems(formattedOptions, onClickListener);
        dialogBuilder.create().show();
    }

    public static void newDatePickerDialog(Context context,
                                           final OnDateRemovedListener onDateRemovedListener,
                                           final OnDateSetListener onDateSetListener) {

        newDatePickerDialog(new Date(), context, onDateRemovedListener, onDateSetListener);

    }

    public interface OnDateSetListener {
        void onDateSet(int year, int month, int day);
    }

    public interface OnDateRemovedListener {
        void onDateRemoved();
    }

}