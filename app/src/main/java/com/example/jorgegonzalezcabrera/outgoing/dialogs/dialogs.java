package com.example.jorgegonzalezcabrera.outgoing.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.entry.type;
import com.example.jorgegonzalezcabrera.outgoing.models.incomeCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.subcategory;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class dialogs {

    public interface OnNewEntryAccepted {
        void OnClick(String subcategory, int type, double value, String description);
    }

    public static void newEntryDialog(Context context,@Nonnull List<outgoingCategory> outgoingCategories,
                                      @Nonnull List<incomeCategory> incomeCategories, final OnNewEntryAccepted myInterface) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_entry_dialog);

        final EditText valueEditText = dialog.findViewById(R.id.editTextValueNewEntry);
        final Spinner categorySpinner = dialog.findViewById(R.id.spinnerCategorySelection);
        final EditText descriptionEditText = dialog.findViewById(R.id.editTextConceptNewEntry);
        Button cancelButton = dialog.findViewById(R.id.buttonCancel);
        Button applyButton = dialog.findViewById(R.id.buttonApplyNewEntry);

        List<String> categories = new ArrayList<>();
        for (int i = 0; i < outgoingCategories.size(); i++) {
            for (int j = 0; j < outgoingCategories.get(i).getSubcategories().size(); j++) {
                subcategory subcategory = outgoingCategories.get(i).getSubcategories().get(j);
                if(subcategory!=null){
                    categories.add(subcategory.getName());
                }
            }
        }
        final int lastOutgoingCategoryPosition = categories.size() - 1;
        for (int i = 0; i < incomeCategories.size(); i++) {
            categories.add(incomeCategories.get(i).getName());
        }

        final ArrayAdapter<String> categoriesSpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categories);
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
                    if (categorySpinner.getSelectedItemPosition() <= lastOutgoingCategoryPosition)
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