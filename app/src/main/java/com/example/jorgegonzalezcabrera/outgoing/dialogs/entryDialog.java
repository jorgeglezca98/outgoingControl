package com.example.jorgegonzalezcabrera.outgoing.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getFunctioningIncomeCategories;
import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getFunctioningOutgoingCategories;

public class entryDialog extends Dialog {

    private localUtils.OnEntriesChangeInterface onEntriesChange;
    private EditText valueEditText;
    private EditText categorySelectionEditText;
    private EditText descriptionEditText;
    private EditText datePickerEditText;
    private Button applyButton;
    private entry lastVersion;
    private Context unmodifiedContext;

    public entryDialog(@NonNull Context context, final localUtils.OnEntriesChangeInterface onEntriesChange) {
        //General dialog's configuration.
        super(new ContextThemeWrapper(context, R.style.AppTheme_TransparentActivity));
        getWindow().setWindowAnimations(R.style.DialogAnimationFromRight);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.new_entry_dialog);
        getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;

        this.onEntriesChange = onEntriesChange;
        this.unmodifiedContext = context;

        valueEditText = findViewById(R.id.editTextValueNewEntry);
        categorySelectionEditText = findViewById(R.id.editTextCategorySelection);
        descriptionEditText = findViewById(R.id.editTextConceptNewEntry);
        datePickerEditText = findViewById(R.id.editTextEntryDate);

        // Category field behavior.
        final PopupMenu popup = new PopupMenu(getContext(), categorySelectionEditText);
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

        // Bind buttons.
        MaterialButton cancelButton = findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
        applyButton = findViewById(R.id.buttonApplyNewEntry);
    }

    public entryDialog(@NonNull Context context, @NonNull entry lastVersion, final localUtils.OnEntriesChangeInterface onEntriesChange) {
        this(context, onEntriesChange);
        this.lastVersion = lastVersion;
    }

    public entry getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(entry lastVersion) {
        this.lastVersion = lastVersion;
    }

    @Override
    protected void onStart() {
        final GregorianCalendar creationDate = new GregorianCalendar();

        if (lastVersion != null) {
            valueEditText.setText(String.valueOf(lastVersion.getValor()));
            categorySelectionEditText.setText(lastVersion.getCategoryName());
            descriptionEditText.setText(lastVersion.getDescription());
            creationDate.setTime(lastVersion.getCreationDate());
        } else {
            valueEditText.setText(String.valueOf(""));
            categorySelectionEditText.setText("");
            descriptionEditText.setText("");
            creationDate.setTime(new Date());
        }

        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        datePickerEditText.setText(df.format(creationDate.getTime()));
        datePickerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DatePickerDialog(unmodifiedContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        creationDate.set(year, month, day);
                        datePickerEditText.setText(df.format(creationDate.getTime()));
                    }
                }, creationDate.get(Calendar.YEAR), creationDate.get(Calendar.MONTH), creationDate.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!valueEditText.getText().toString().isEmpty() && !categorySelectionEditText.getText().toString().isEmpty()) {
                    category.typeOfCategory typeOfCategory;
                    if (getFunctioningOutgoingCategories().contains(categorySelectionEditText.getText().toString()))
                        typeOfCategory = category.typeOfCategory.OUTGOING;
                    else
                        typeOfCategory = category.typeOfCategory.INCOME;

                    String subcategory = categorySelectionEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    double value = Double.valueOf(valueEditText.getText().toString());

                    if (lastVersion == null) {
                        onEntriesChange.addEntry(new entry(value, typeOfCategory, subcategory, description, creationDate.getTime()));
                    } else {
                        entry nextVersion = new entry(value, typeOfCategory, subcategory, description, creationDate.getTime());
                        nextVersion.setId(lastVersion.getId());
                        onEntriesChange.editEntry(nextVersion);
                    }

                    dismiss();
                }
            }
        });

        super.onStart();
    }

}
