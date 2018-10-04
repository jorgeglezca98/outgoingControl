package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.jorgegonzalezcabrera.outgoing.R;

public class editFieldActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editable_field_activity);
        setFinishOnTouchOutside(false);
        supportPostponeEnterTransition();

        supportStartPostponedEnterTransition();
    }
}