package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;

public class entryPeriodicity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.periodicity_fragment, container, false);

        EditText editTextQuantityOf = v.findViewById(R.id.editTextQuantityOf);
        EditText editTextPeriodicityType = v.findViewById(R.id.editTextPeriodicityType);
        RecyclerView daysOfExecution = v.findViewById(R.id.daysOfExecution);
        Switch askFirst = v.findViewById(R.id.askFirst);
        TextView editInitialDate = v.findViewById(R.id.editInitialDate);
        RadioGroup radioGroupFinalDate = v.findViewById(R.id.radioGroupFinalDate);

        return v;
    }
}
