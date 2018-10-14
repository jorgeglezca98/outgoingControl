package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jorgegonzalezcabrera.outgoing.R;

import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.CONTAINER_TRANSITION_NAME_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.FIELD_TRANSITION_NAME_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.FINAL_VALUE_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.HINT_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.ID_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.INITIAL_VALUE_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_CODE_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_EDIT_MONEY_CONTROLLER_MAXIMUM;

public class editFieldActivity extends AppCompatActivity {

    public interface editIncomeCategoryInterface {
        void editCategoryField(String initialValue, ConstraintLayout container, EditText field, String hint, int requestCode, long id);
    }

    private final static String ERROR_MESSAGE = "Mandatory field";

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
        final int requestCode = extras.getInt(REQUEST_CODE_KEY);
        final long id = extras.getLong(ID_KEY);

        ConstraintLayout container = findViewById(R.id.container);
        container.setTransitionName(containerTransitionName);

        final EditText fieldEditText = findViewById(R.id.editTextField);
        fieldEditText.setTransitionName(fieldTransitionName);
        fieldEditText.setText(initialValue);
        if (requestCode == REQUEST_EDIT_MONEY_CONTROLLER_MAXIMUM) {
            fieldEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        final TextInputLayout textInputLayoutField = findViewById(R.id.textInputLayoutField);
        textInputLayoutField.setHint(fieldHint);

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
                if (!fieldEditText.getText().toString().isEmpty()) {
                    Intent returnIntent = new Intent();
                    if (requestCode == REQUEST_EDIT_MONEY_CONTROLLER_MAXIMUM) {
                        returnIntent.putExtra(FINAL_VALUE_KEY, Double.valueOf(fieldEditText.getText().toString()));
                    } else {
                        returnIntent.putExtra(FINAL_VALUE_KEY, fieldEditText.getText().toString());
                    }
                    returnIntent.putExtra(ID_KEY, id);
                    setResult(Activity.RESULT_OK, returnIntent);
                    supportFinishAfterTransition();
                } else {
                    textInputLayoutField.setError(ERROR_MESSAGE);
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