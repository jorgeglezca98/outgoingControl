package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;

public class initialMoneyInitialConfiguration extends Fragment {

    private EditText editTextInitialMoney;
    private TextView informationTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment_initial_configuration, container, false);

        editTextInitialMoney = view.findViewById(R.id.editTextCurrentMoney);
        informationTextView = view.findViewById(R.id.currentMoneyDescription);
        ImageButton informationButton = view.findViewById(R.id.buttonInformation);
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                informationTextView.setVisibility(informationTextView.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
            }
        });

        return view;
    }

    public boolean checkData(){
        return !editTextInitialMoney.getText().toString().isEmpty();
    }

    public double getData() {
        if(checkData()){
            return Double.parseDouble(editTextInitialMoney.getText().toString());
        } else {
            return 0.0d;
        }
    }
}
