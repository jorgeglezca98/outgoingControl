package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.jorgegonzalezcabrera.outgoing.R;

import java.util.ArrayList;
import java.util.Date;

import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.ID_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_ASK_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_CATEGORY_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_DESCRIPTION_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_END_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_FREQUENCY_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_LAST_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_QUANTITY_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_REPETITIONS_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_START_KEY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.PERIODIC_ENTRY_VALUE_KEY;

public class editPeriodicEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editable_periodic_entry_activity);
        setFinishOnTouchOutside(false);
        supportPostponeEnterTransition();

        final Bundle extras = getIntent().getExtras();
        long id = extras.getLong(ID_KEY);
        double value = extras.getDouble(PERIODIC_ENTRY_VALUE_KEY);
        long categoryId = extras.getLong(PERIODIC_ENTRY_CATEGORY_KEY);
        String description = extras.getString(PERIODIC_ENTRY_DESCRIPTION_KEY);
        int quantity = extras.getInt(PERIODIC_ENTRY_QUANTITY_KEY);
        int frequency = extras.getInt(PERIODIC_ENTRY_FREQUENCY_KEY);
        ArrayList<Integer> repetitions = extras.getIntegerArrayList(PERIODIC_ENTRY_REPETITIONS_KEY);
        Date start = (Date) extras.get(PERIODIC_ENTRY_START_KEY);
        Date end = (Date) extras.get(PERIODIC_ENTRY_END_KEY);
        boolean ask = extras.getBoolean(PERIODIC_ENTRY_ASK_KEY);
        Date last = (Date) extras.get(PERIODIC_ENTRY_LAST_KEY);

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        supportFinishAfterTransition();
    }
}
