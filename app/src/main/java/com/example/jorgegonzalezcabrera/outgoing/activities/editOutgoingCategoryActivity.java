package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.erasableItemsAdapter;

import java.util.ArrayList;
import java.util.Vector;

import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CATEGORY_MAXIMUM_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CATEGORY_MAXIMUM_TRANSITION_NAME_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CATEGORY_NAME_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CATEGORY_NAME_TRANSITION_NAME_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CATEGORY_SUBCATEGORIES_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CONTAINER_TRANSITION_NAME_KEY;

public class editOutgoingCategoryActivity extends AppCompatActivity {

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
        Vector<String> formattedSubcategories = new Vector<>(subcategories);

        EditText editTextCategoryName = findViewById(R.id.editTextCategoryName);
        editTextCategoryName.setTransitionName(categoryNameTransitionName);
        editTextCategoryName.setText(categoryName);

        EditText editTextMaxValue = findViewById(R.id.editTextMaxValue);
        editTextMaxValue.setTransitionName(categoryMaxTransitionName);
        editTextMaxValue.setText(categoryMax);

        RecyclerView recyclerViewSubcategories = findViewById(R.id.subcategoriesContainer);
        erasableItemsAdapter subcategoriesAdapter = new erasableItemsAdapter("Subcategory name");
        recyclerViewSubcategories.setAdapter(subcategoriesAdapter);
        recyclerViewSubcategories.setLayoutManager(new LinearLayoutManager(this));
        subcategoriesAdapter.updateItems(formattedSubcategories);

        ConstraintLayout constraintLayout = findViewById(R.id.layoutEditableOutgoingCategory);
        constraintLayout.setTransitionName(containerTransitionName);

        supportStartPostponedEnterTransition();
    }
}
