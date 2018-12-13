package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.customizeCheckboxesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmList;

import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.ID_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_ASK_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_CATEGORY_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_DESCRIPTION_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_END_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_FREQUENCY_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_QUANTITY_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_REPETITIONS_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_START_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_VALUE_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getFunctioningIncomeCategories;
import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getFunctioningOutgoingCategories;

public class editPeriodicEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editable_periodic_entry_activity);
        setFinishOnTouchOutside(false);
        supportPostponeEnterTransition();

        final Bundle extras = getIntent().getExtras();
        final long id = extras.getLong(ID_KEY);
        double value = extras.getDouble(PERIODIC_ENTRY_VALUE_KEY);
        long categoryId = extras.getLong(PERIODIC_ENTRY_CATEGORY_KEY);
        String description = extras.getString(PERIODIC_ENTRY_DESCRIPTION_KEY);
        int quantity = extras.getInt(PERIODIC_ENTRY_QUANTITY_KEY);
        int frequency = extras.getInt(PERIODIC_ENTRY_FREQUENCY_KEY);
        ArrayList<Integer> repetitions = extras.getIntegerArrayList(PERIODIC_ENTRY_REPETITIONS_KEY);
        Date start = (Date) extras.get(PERIODIC_ENTRY_START_KEY);
        Date end = (Date) extras.get(PERIODIC_ENTRY_END_KEY);
        boolean ask = extras.getBoolean(PERIODIC_ENTRY_ASK_KEY);

        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));

        final EditText valueEditText = findViewById(R.id.editTextValueNewEntry);
        valueEditText.setText(String.valueOf(value));

        final EditText categorySelectionEditText = findViewById(R.id.editTextCategorySelection);
        final PopupMenu categoriesPopup = new PopupMenu(this, categorySelectionEditText);
        categoriesPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                categorySelectionEditText.setText(menuItem.getTitle());
                return true;
            }
        });
        final List<String> outgoingCategories = getFunctioningOutgoingCategories();
        for (int i = 0; i < outgoingCategories.size(); i++) {
            categoriesPopup.getMenu().add(0, Menu.NONE, i, outgoingCategories.get(i));
        }
        int firstIncomeCategory = outgoingCategories.size();
        List<String> incomeCategories = getFunctioningIncomeCategories();
        for (int i = 0; i < incomeCategories.size(); i++) {
            categoriesPopup.getMenu().add(1, Menu.NONE, firstIncomeCategory + i, incomeCategories.get(i));
        }
        categorySelectionEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoriesPopup.show();
            }
        });
        categorySelectionEditText.setText(Realm.getDefaultInstance().where(category.class).equalTo("id", categoryId).findFirst().getName());

        final EditText descriptionEditText = findViewById(R.id.editTextConceptNewEntry);
        descriptionEditText.setText(description);

        final LinearLayout repetitionInformation = findViewById(R.id.repetitionInformation);
        repetitionInformation.setVisibility(View.GONE);
        final RecyclerView daysOfExecution = findViewById(R.id.daysOfExecution);
        Vector<String> weekLabels = new Vector<>();
        weekLabels.add("L");
        weekLabels.add("M");
        weekLabels.add("X");
        weekLabels.add("J");
        weekLabels.add("V");
        weekLabels.add("S");
        weekLabels.add("D");
        final customizeCheckboxesAdapter weekAdapter = new customizeCheckboxesAdapter(weekLabels);
        Vector<String> monthLabels = new Vector<>();
        for (int i = 1; i <= 28; i++) {
            monthLabels.add(String.valueOf(i));
        }
        final customizeCheckboxesAdapter monthAdapter = new customizeCheckboxesAdapter(monthLabels);
        daysOfExecution.setAdapter(weekAdapter);
        daysOfExecution.setLayoutManager(new GridLayoutManager(this, 7, LinearLayoutManager.VERTICAL, false));

        final EditText editTextQuantityOf = findViewById(R.id.editTextQuantityOf);
        editTextQuantityOf.setText(String.valueOf(quantity));
        editTextQuantityOf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && editTextQuantityOf.getText().toString().isEmpty()) {
                    editTextQuantityOf.setText("1");
                }
            }
        });

        final EditText editTextPeriodicityType = findViewById(R.id.editTextPeriodicityType);
        if (frequency == periodicEntry.periodicType.DAILY.ordinal()) {
            editTextPeriodicityType.setText("days");
        } else if (frequency == periodicEntry.periodicType.WEEKLY.ordinal()) {
            editTextPeriodicityType.setText("weeks");
            for (int i = 0; i < repetitions.size(); i++) {
                weekAdapter.changeItem(repetitions.get(i) == 1 ? 6 : repetitions.get(i) - 2);
            }
            repetitionInformation.setVisibility(View.VISIBLE);
            daysOfExecution.setAdapter(weekAdapter);
        } else if (frequency == periodicEntry.periodicType.MONTHLY.ordinal()) {
            editTextPeriodicityType.setText("months");
            for (int i = 0; i < repetitions.size(); i++) {
                monthAdapter.changeItem(repetitions.get(i) - 1);
            }
            repetitionInformation.setVisibility(View.VISIBLE);
            daysOfExecution.setAdapter(monthAdapter);
        } else if (frequency == periodicEntry.periodicType.ANNUAL.ordinal()) {
            editTextPeriodicityType.setText("years");
        }
        final PopupMenu popup = new PopupMenu(this, editTextPeriodicityType);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().equals("weeks")) {
                    repetitionInformation.setVisibility(View.VISIBLE);
                    daysOfExecution.setAdapter(weekAdapter);
                } else if (menuItem.getTitle().equals("months")) {
                    repetitionInformation.setVisibility(View.VISIBLE);
                    daysOfExecution.setAdapter(monthAdapter);
                } else {
                    repetitionInformation.setVisibility(View.GONE);
                }
                editTextPeriodicityType.setText(menuItem.getTitle());
                return true;
            }
        });
        popup.getMenu().add("days");
        popup.getMenu().add("weeks");
        popup.getMenu().add("months");
        popup.getMenu().add("years");
        editTextPeriodicityType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();
            }
        });

        final EditText editInitialDate = findViewById(R.id.editInitialDate);
        final GregorianCalendar startDate = new GregorianCalendar();
        startDate.setTime(start);
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);
        editInitialDate.setText(df.format(startDate.getTime()));
        editInitialDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DatePickerDialog(editPeriodicEntryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        startDate.set(year, month, day);
                        editInitialDate.setText(df.format(startDate.getTime()));
                    }
                }, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        final EditText lastDayEditText = findViewById(R.id.lastDayEditText);
        final GregorianCalendar endDate = new GregorianCalendar();
        if (end != null) endDate.setTime(end);
        endDate.setTime(new Date());
        endDate.set(Calendar.HOUR_OF_DAY, 0);
        endDate.set(Calendar.MINUTE, 0);
        endDate.set(Calendar.SECOND, 0);
        endDate.set(Calendar.MILLISECOND, 0);
        lastDayEditText.setText(df.format(endDate.getTime()));
        lastDayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DatePickerDialog(editPeriodicEntryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        endDate.set(year, month, day);
                        lastDayEditText.setText(df.format(endDate.getTime()));
                    }
                }, endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        final EditText quantityOfRepetitionsEditText = findViewById(R.id.quantityOfRepetitionsEditText);
        quantityOfRepetitionsEditText.setText("1");
        quantityOfRepetitionsEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && editTextQuantityOf.getText().toString().isEmpty()) {
                    editTextQuantityOf.setText("1");
                }
            }
        });

        final RadioGroup endOptions = findViewById(R.id.radioGroupFinalDate);
        endOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButtonNever) {
                    lastDayEditText.setEnabled(false);
                    quantityOfRepetitionsEditText.setEnabled(false);
                } else if (i == R.id.radioButtonByDay) {
                    lastDayEditText.setEnabled(true);
                    quantityOfRepetitionsEditText.setEnabled(false);
                } else if (i == R.id.radioButtonAfterXRepetitions) {
                    lastDayEditText.setEnabled(false);
                    quantityOfRepetitionsEditText.setEnabled(true);
                }
            }
        });
        if (end == null) {
            endOptions.check(R.id.radioButtonNever);
        } else {
            endOptions.check(R.id.radioButtonByDay);
        }

        final Switch askBeforeSwitch = findViewById(R.id.askFirst);
        askBeforeSwitch.setChecked(ask);

        ImageButton closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                supportFinishAfterTransition();
            }
        });

        MaterialButton buttonApply = findViewById(R.id.buttonApply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                periodicEntry periodicEntryWithSameDescription = Realm.getDefaultInstance().where(periodicEntry.class).equalTo("description", descriptionEditText.getText().toString()).findFirst();
                if (!valueEditText.getText().toString().isEmpty() &&
                        !categorySelectionEditText.getText().toString().isEmpty() &&
                        !descriptionEditText.getText().toString().isEmpty() && (periodicEntryWithSameDescription == null || periodicEntryWithSameDescription.getId() == id)) {
                    if (!editTextQuantityOf.getText().toString().isEmpty()) {
                        String periodicityType = editTextPeriodicityType.getText().toString();
                        if ((periodicityType.equals("weeks") && weekAdapter.isSomeoneSelected()) ||
                                (periodicityType.equals("months") && monthAdapter.isSomeoneSelected()) ||
                                periodicityType.equals("days") || periodicityType.equals("years")) {
                            if ((endOptions.getCheckedRadioButtonId() == R.id.radioButtonAfterXRepetitions && !quantityOfRepetitionsEditText.getText().toString().isEmpty()) ||
                                    endOptions.getCheckedRadioButtonId() == R.id.radioButtonNever ||
                                    (endOptions.getCheckedRadioButtonId() == R.id.radioButtonByDay && startDate.before(endDate))) {

                                periodicEntry.periodicType frequency;
                                RealmList<Integer> formattedDaysOfRepetition = new RealmList<>();
                                if (periodicityType.equals("weeks")) {
                                    frequency = periodicEntry.periodicType.WEEKLY;
                                    Vector<String> daysOfRepetition = new Vector<>(weekAdapter.getCheckedItems());
                                    for (int i = 0; i < daysOfRepetition.size(); i++) {
                                        switch (daysOfRepetition.get(i)) {
                                            case "L":
                                                formattedDaysOfRepetition.add(Calendar.MONDAY);
                                                break;
                                            case "M":
                                                formattedDaysOfRepetition.add(Calendar.TUESDAY);
                                                break;
                                            case "X":
                                                formattedDaysOfRepetition.add(Calendar.WEDNESDAY);
                                                break;
                                            case "J":
                                                formattedDaysOfRepetition.add(Calendar.THURSDAY);
                                                break;
                                            case "V":
                                                formattedDaysOfRepetition.add(Calendar.FRIDAY);
                                                break;
                                            case "S":
                                                formattedDaysOfRepetition.add(Calendar.SATURDAY);
                                                break;
                                            case "D":
                                                formattedDaysOfRepetition.add(Calendar.SUNDAY);
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                } else if (periodicityType.equals("days")) {
                                    frequency = periodicEntry.periodicType.DAILY;
                                } else if (periodicityType.equals("months")) {
                                    frequency = periodicEntry.periodicType.MONTHLY;
                                    Vector<String> daysOfRepetition = new Vector<>(monthAdapter.getCheckedItems());
                                    for (int i = 0; i < daysOfRepetition.size(); i++) {
                                        formattedDaysOfRepetition.add(Integer.parseInt(daysOfRepetition.get(i)));
                                    }
                                } else {
                                    frequency = periodicEntry.periodicType.ANNUAL;
                                }

                                GregorianCalendar formattedEndDate;
                                if (endOptions.getCheckedRadioButtonId() == R.id.radioButtonNever) {
                                    formattedEndDate = null;
                                } else if (endOptions.getCheckedRadioButtonId() == R.id.radioButtonByDay) {
                                    formattedEndDate = endDate;
                                } else {
                                    if (periodicityType.equals("weeks") || (periodicityType.equals("months"))) {
                                        formattedEndDate = periodicEntry.setLastDayByTimes(Integer.parseInt(editTextQuantityOf.getText().toString()), Integer.parseInt(quantityOfRepetitionsEditText.getText().toString()), frequency, startDate, formattedDaysOfRepetition);
                                    } else {
                                        formattedEndDate = periodicEntry.setLastDayByTimes(Integer.parseInt(editTextQuantityOf.getText().toString()), Integer.parseInt(quantityOfRepetitionsEditText.getText().toString()), frequency, startDate);
                                    }
                                }
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra(ID_KEY, id);
                                returnIntent.putExtra(PERIODIC_ENTRY_VALUE_KEY, Double.parseDouble(valueEditText.getText().toString()));
                                returnIntent.putExtra(PERIODIC_ENTRY_CATEGORY_KEY, Realm.getDefaultInstance().where(category.class).equalTo("name", categorySelectionEditText.getText().toString()).findFirst().getId());
                                returnIntent.putExtra(PERIODIC_ENTRY_DESCRIPTION_KEY, descriptionEditText.getText().toString());
                                returnIntent.putExtra(PERIODIC_ENTRY_QUANTITY_KEY, Integer.parseInt(editTextQuantityOf.getText().toString()));
                                returnIntent.putExtra(PERIODIC_ENTRY_FREQUENCY_KEY, frequency.ordinal());
                                returnIntent.putIntegerArrayListExtra(PERIODIC_ENTRY_REPETITIONS_KEY, new ArrayList<>(formattedDaysOfRepetition));
                                returnIntent.putExtra(PERIODIC_ENTRY_START_KEY, startDate.getTime());
                                returnIntent.putExtra(PERIODIC_ENTRY_END_KEY, formattedEndDate != null ? formattedEndDate.getTime() : null);
                                returnIntent.putExtra(PERIODIC_ENTRY_ASK_KEY, askBeforeSwitch.isChecked());
                                setResult(Activity.RESULT_OK, returnIntent);
                                supportFinishAfterTransition();
                            } else {
                                Toast.makeText(editPeriodicEntryActivity.this, "The end date must be set and it can't be before the start date.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(editPeriodicEntryActivity.this, "You have to select at least one day of repetition.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(editPeriodicEntryActivity.this, "Set number of " + editTextPeriodicityType.getText() + "between repetitions", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(editPeriodicEntryActivity.this, "Set main data of entries.", Toast.LENGTH_LONG).show();
                }
            }
        });

        supportStartPostponedEnterTransition();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        supportFinishAfterTransition();
    }
}
