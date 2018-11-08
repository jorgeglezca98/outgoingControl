package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.categoriesSelectionAdapter;

import java.util.ArrayList;

import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CATEGORY_MAXIMUM_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CATEGORY_MAXIMUM_TRANSITION_NAME_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CATEGORY_NAME_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CATEGORY_NAME_TRANSITION_NAME_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CATEGORY_SUBCATEGORIES_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CONTAINER_TRANSITION_NAME_KEY;

public class editOutgoingCategoryActivity extends AppCompatActivity {


    private EditText editTextCategoryName;
    private EditText editTextMaxValue;
    private categoriesSelectionAdapter subcategoriesAdapter;
    private TextInputLayout categoryNameContainer;
    private TextInputLayout categoryMaxContainer;

    private static final String ERROR_MESSAGE = "Mandatory field";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editable_outgoing_category_activity);
        setFinishOnTouchOutside(false);
        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        String containerTransitionName = extras.getString(CONTAINER_TRANSITION_NAME_KEY);
        String categoryNameTransitionName = extras.getString(CATEGORY_NAME_TRANSITION_NAME_KEY);
        String categoryMaxTransitionName = extras.getString(CATEGORY_MAXIMUM_TRANSITION_NAME_KEY);
        String categoryName = extras.getString(CATEGORY_NAME_KEY);
        String categoryMax = String.valueOf(extras.getDouble(CATEGORY_MAXIMUM_KEY));

        ArrayList<String> subcategories = extras.getStringArrayList(CATEGORY_SUBCATEGORIES_KEY);

        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        editTextCategoryName.setTransitionName(categoryNameTransitionName);
        editTextCategoryName.setText(categoryName);

        categoryNameContainer = findViewById(R.id.categoryNameContainer);
        categoryMaxContainer = findViewById(R.id.categoryMaxContainer);

        editTextMaxValue = findViewById(R.id.editTextMaxValue);
        editTextMaxValue.setTransitionName(categoryMaxTransitionName);
        editTextMaxValue.setText(categoryMax);

        final RecyclerView recyclerViewSubcategories = findViewById(R.id.recyclerViewSubcategories);
        subcategoriesAdapter = new categoriesSelectionAdapter(categoriesSelectionAdapter.FUNCTIONING_OUTGOING_CATEGORIES);
        subcategoriesAdapter.markAsChecked(subcategories);
        recyclerViewSubcategories.setAdapter(subcategoriesAdapter);
        recyclerViewSubcategories.setLayoutManager(new LinearLayoutManager(this));

        ConstraintLayout constraintLayout = findViewById(R.id.layoutEditableOutgoingCategory);
        constraintLayout.setTransitionName(containerTransitionName);

        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                supportFinishAfterTransition();
            }
        });

        Button buttonApply = findViewById(R.id.buttonApply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkDataAndShowErrors()) {
                    Intent returnIntent = new Intent();

                    ArrayList<String> finalSubcategories = new ArrayList<>();
                    for (int i = 0; i < subcategoriesAdapter.getCategories().size(); i++) {
                        if (subcategoriesAdapter.getCategories().get(i).selected) {
                            finalSubcategories.add(subcategoriesAdapter.getCategories().get(i).name);
                        }
                    }

                    returnIntent.putExtra(CATEGORY_NAME_KEY, editTextCategoryName.getText().toString());
                    returnIntent.putExtra(CATEGORY_MAXIMUM_KEY, Double.valueOf(editTextMaxValue.getText().toString()));
                    returnIntent.putStringArrayListExtra(CATEGORY_SUBCATEGORIES_KEY, finalSubcategories);
                    setResult(Activity.RESULT_OK, returnIntent);
                    supportFinishAfterTransition();
                }
            }
        });

        supportStartPostponedEnterTransition();
    }

    private boolean checkDataAndShowErrors() {
        boolean result = true;

        if (editTextCategoryName.getText().toString().isEmpty()) {
            categoryNameContainer.setError(ERROR_MESSAGE);
            result = false;
        } else {
            categoryNameContainer.setError(null);
        }

        if (editTextMaxValue.getText().toString().isEmpty()) {
            categoryMaxContainer.setError(ERROR_MESSAGE);
            result = false;
        } else {
            categoryMaxContainer.setError(null);
        }

        return result;
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        supportFinishAfterTransition();
    }
}
