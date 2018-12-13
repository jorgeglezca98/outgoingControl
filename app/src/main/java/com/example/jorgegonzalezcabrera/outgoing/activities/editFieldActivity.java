package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.category;

import io.realm.Realm;

import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CONTAINER_TRANSITION_NAME_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.FIELD_TRANSITION_NAME_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.FINAL_VALUE_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.HINT_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.ID_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.INITIAL_VALUE_KEY;

public class editFieldActivity extends AppCompatActivity {

    private LinearLayout container;

    public interface editIncomeCategoryInterface {
        void editCategoryField(String initialValue, ConstraintLayout container, TextView field, String hint, int requestCode, long id);
    }

    private final static String ERROR_MESSAGE_EMPTY_FIELD = "Mandatory field";
    private final static String ERROR_MESSAGE_REPEATED_NAME = "Category name already used";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editable_field_activity);
        setFinishOnTouchOutside(false);
        supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        String containerTransitionName = extras.getString(CONTAINER_TRANSITION_NAME_KEY);
        String fieldTransitionName = extras.getString(FIELD_TRANSITION_NAME_KEY);
        String initialValue = extras.getString(INITIAL_VALUE_KEY);
        String fieldHint = extras.getString(HINT_KEY);
        final long id = extras.getLong(ID_KEY);

        container = findViewById(R.id.container);
        container.setTransitionName(containerTransitionName);

        final EditText fieldEditText = findViewById(R.id.editTextField);
        fieldEditText.setTransitionName(fieldTransitionName);
        fieldEditText.setText(initialValue);

        final TextInputLayout textInputLayoutField = findViewById(R.id.textInputLayoutField);
        textInputLayoutField.setHint(fieldHint);

        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                container.animate().alpha(0).setDuration(500);
                supportFinishAfterTransition();
            }
        });

        Button buttonApply = findViewById(R.id.buttonApply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fieldEditText.getText().toString().isEmpty()) {
                    category categoryWithSameName = Realm.getDefaultInstance().where(category.class).equalTo("name",fieldEditText.getText().toString()).findFirst();
                    if(categoryWithSameName == null || categoryWithSameName.getId() == id) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(FINAL_VALUE_KEY, fieldEditText.getText().toString());
                        returnIntent.putExtra(ID_KEY, id);
                        setResult(Activity.RESULT_OK, returnIntent);
                        container.animate().alpha(0).setDuration(500);
                        supportFinishAfterTransition();
                    } else {
                        textInputLayoutField.setError(ERROR_MESSAGE_REPEATED_NAME);
                    }
                } else {
                    textInputLayoutField.setError(ERROR_MESSAGE_EMPTY_FIELD);
                }
            }
        });

        supportStartPostponedEnterTransition();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        container.animate().alpha(0).setDuration(500);
        supportFinishAfterTransition();
    }
}