package com.example.jorgegonzalezcabrera.outgoing.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.categoriesSelectionAdapter;
import com.example.jorgegonzalezcabrera.outgoing.fragments.actionsFragment;
import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.category.typeOfCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;
import com.example.jorgegonzalezcabrera.outgoing.views.editTextWithButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getFunctioningIncomeCategories;
import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getFunctioningOutgoingCategories;
import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getTypeFromOrdinal;

public class dialogs {

    private interface onPeriodicitySet {
        void periodicitySet(periodicEntry periodicEntrySet);
    }

    public static void newEntryDialog(final Context context, final localUtils.OnEntriesChangeInterface onEntriesChange) {
        final Dialog dialog = new Dialog(new ContextThemeWrapper(context, R.style.AppTheme_TransparentActivity));
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimationFromRight);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_entry_dialog);
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;

        final EditText valueEditText = dialog.findViewById(R.id.editTextValueNewEntry);
        final EditText categorySelectionEditText = dialog.findViewById(R.id.editTextCategorySelection);
        final EditText descriptionEditText = dialog.findViewById(R.id.editTextConceptNewEntry);
        final EditText datePickerEditText = dialog.findViewById(R.id.editTextEntryDate);
        MaterialButton cancelButton = dialog.findViewById(R.id.buttonCancel);
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
        final String initialDate = creationDate.get(Calendar.DAY_OF_MONTH) + "/" + creationDate.get(Calendar.MONTH) + "/" + creationDate.get(Calendar.YEAR);
        datePickerEditText.setText(initialDate);
        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        datePickerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        creationDate.set(year, month, day);
                        datePickerEditText.setText(df.format(creationDate.getTime()));
                    }
                }, creationDate.get(Calendar.YEAR), creationDate.get(Calendar.MONTH), creationDate.get(Calendar.DAY_OF_MONTH));
                dialog.show();
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
                        typeOfCategory = category.typeOfCategory.OUTGOING.ordinal();
                    else
                        typeOfCategory = category.typeOfCategory.INCOME.ordinal();

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

    public static void editEntryDialog(final Context context, @NonNull final entry lastVersion, final localUtils.OnEntriesChangeInterface onEntriesChange) {
        final Dialog dialog = new Dialog(new ContextThemeWrapper(context, R.style.AppTheme_TransparentActivity));
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimationFromRight);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_entry_dialog);
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;

        final EditText valueEditText = dialog.findViewById(R.id.editTextValueNewEntry);
        valueEditText.setText(String.valueOf(lastVersion.getValor()));
        final EditText categorySelectionEditText = dialog.findViewById(R.id.editTextCategorySelection);
        categorySelectionEditText.setText(lastVersion.getCategoryName());
        final EditText descriptionEditText = dialog.findViewById(R.id.editTextConceptNewEntry);
        descriptionEditText.setText(lastVersion.getDescription());
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
        creationDate.setTime(lastVersion.getCreationDate());
        String initialDate = creationDate.get(Calendar.DAY_OF_MONTH) + "/" + creationDate.get(Calendar.MONTH) + "/" + creationDate.get(Calendar.YEAR);
        datePickerEditText.setText(initialDate);
        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        datePickerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        creationDate.set(year, month, day);
                        datePickerEditText.setText(df.format(creationDate.getTime()));
                    }
                }, creationDate.get(Calendar.YEAR), creationDate.get(Calendar.MONTH), creationDate.get(Calendar.DAY_OF_MONTH));
                dialog.show();
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
                        typeOfCategory = category.typeOfCategory.OUTGOING.ordinal();
                    else
                        typeOfCategory = category.typeOfCategory.INCOME.ordinal();

                    String subcategory = categorySelectionEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    double value = Double.valueOf(valueEditText.getText().toString());
                    GregorianCalendar updatedDate = new GregorianCalendar();
                    updatedDate.setTime(new Date());
                    updatedDate.set(creationDate.get(Calendar.YEAR), creationDate.get(Calendar.MONTH), creationDate.get(Calendar.DAY_OF_MONTH));

                    entry nextVersion = new entry(value, getTypeFromOrdinal(typeOfCategory), subcategory, description, updatedDate.getTime());
                    nextVersion.setId(lastVersion.getId());
                    onEntriesChange.editEntry(nextVersion);
                    dialog.dismiss();
                }
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

    public static void filtersDialog(final Context context, final actionsFragment.filterActions filtersManipulation,
                                     Vector<categoriesSelectionAdapter.categoryCheckBox> categories, final Date minDate,
                                     final Date maxDate, String minValue, String maxValue, String description) {

        final Dialog dialog = new Dialog(new ContextThemeWrapper(context, R.style.AppTheme_TransparentActivity));
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimationFromRight);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.filters_dialog);
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;

        final RecyclerView recyclerViewCategoriesSelection = dialog.findViewById(R.id.recyclerViewCategoriesSelection);
        final categoriesSelectionAdapter categoriesSelectionAdapter = new categoriesSelectionAdapter(categories);
        recyclerViewCategoriesSelection.setAdapter(categoriesSelectionAdapter);
        StaggeredGridLayoutManager categoriesSelectionLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        recyclerViewCategoriesSelection.setLayoutManager(categoriesSelectionLayoutManager);

        final EditText editTextMinValue = dialog.findViewById(R.id.editTextMinValue);
        editTextMinValue.setText(minValue);
        final EditText editTextMaxValue = dialog.findViewById(R.id.editTextMaxValue);
        editTextMaxValue.setText(maxValue);
        final EditText editTextDescriptionFilter = dialog.findViewById(R.id.editTextDescriptionFilter);
        editTextDescriptionFilter.setText(description);

        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        final GregorianCalendar currentMinDate = new GregorianCalendar();
        final editTextWithButton editTextMinDate = dialog.findViewById(R.id.editTextMinDate);
        editTextMinDate.setOnClearListener(new editTextWithButton.OnClearListener() {
            @Override
            public void onClear() {
                currentMinDate.setTime(new Date());
            }
        });
        if (minDate != null) {
            editTextMinDate.setText(df.format(minDate));
            currentMinDate.setTime(minDate);
        }
        editTextMinDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        currentMinDate.set(year, month, day, 0, 0, 0);
                        currentMinDate.set(Calendar.MILLISECOND, 0);

                        editTextMinDate.setText(df.format(currentMinDate.getTime()));
                    }
                }, currentMinDate.get(Calendar.YEAR), currentMinDate.get(Calendar.MONTH), currentMinDate.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        final editTextWithButton editTextMaxDate = dialog.findViewById(R.id.editTextMaxDate);
        final GregorianCalendar currentMaxDate = new GregorianCalendar();
        editTextMaxDate.setOnClearListener(new editTextWithButton.OnClearListener() {
            @Override
            public void onClear() {
                currentMaxDate.setTime(new Date());
            }
        });
        if (maxDate != null) {
            editTextMaxDate.setText(df.format(maxDate));
            currentMaxDate.setTime(maxDate);
        }
        editTextMaxDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        currentMaxDate.set(year, month, day, 0, 0, 0);
                        currentMaxDate.set(Calendar.MILLISECOND, 0);
                        currentMaxDate.add(Calendar.DATE, 1);

                        editTextMaxDate.setText(df.format(currentMaxDate.getTime()));
                    }
                }, currentMaxDate.get(Calendar.YEAR), currentMaxDate.get(Calendar.MONTH), currentMaxDate.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        Button buttonApplyFilters = dialog.findViewById(R.id.buttonApplyFilters);
        buttonApplyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtersManipulation.applyFilters(categoriesSelectionAdapter.getCategories(),
                        editTextMinDate.getText().toString().isEmpty() ? null : currentMinDate.getTime(),
                        editTextMaxDate.getText().toString().isEmpty() ? null : currentMaxDate.getTime(),
                        editTextMinValue.getText().toString(),
                        editTextMaxValue.getText().toString(),
                        editTextDescriptionFilter.getText().toString());
                dialog.dismiss();
            }
        });

        Button buttonRemoveFilters = dialog.findViewById(R.id.buttonCancelFilters);
        buttonRemoveFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtersManipulation.cleanFilters();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}