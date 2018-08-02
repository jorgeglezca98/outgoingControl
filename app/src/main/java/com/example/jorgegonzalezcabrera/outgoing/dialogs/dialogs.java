package com.example.jorgegonzalezcabrera.outgoing.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.categoriesSpinnerAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.entry.type;

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

}