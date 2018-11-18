package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getFunctioningIncomeCategories;
import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getFunctioningOutgoingCategories;
import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getTypeFromOrdinal;

public class entryInformation extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_entry_information,container,false);

        final EditText valueEditText = v.findViewById(R.id.editTextValueNewEntry);
        final EditText categorySelectionEditText = v.findViewById(R.id.editTextCategorySelection);
        final EditText descriptionEditText = v.findViewById(R.id.editTextConceptNewEntry);
        final EditText datePickerEditText = v.findViewById(R.id.editTextEntryDate);

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

        final GregorianCalendar creationDate = new GregorianCalendar();
        creationDate.setTime(new Date());
        String initialDate = creationDate.get(Calendar.DAY_OF_MONTH) + "/" + creationDate.get(Calendar.MONTH) + "/" + creationDate.get(Calendar.YEAR);
        datePickerEditText.setText(initialDate);
        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "ES"));
        datePickerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        creationDate.set(year, month, day);
                        datePickerEditText.setText(df.format(creationDate.getTime()));
                    }
                }, creationDate.get(Calendar.YEAR), creationDate.get(Calendar.MONTH), creationDate.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        return v;
    }
}
