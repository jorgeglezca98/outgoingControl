package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.surplusMoneyTableAdapter.surplusMoneyByCategory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class backSurplusMoneyFragment extends Fragment {

    private TextView textViewSurplusPerDay;
    private TextView textViewDaysForRecovery;
    private TextView textViewMoneyAvailablePerDay;
    private surplusMoneyByCategory surplusMoneyByCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.back_item_of_surplus_item, container, false);

        textViewSurplusPerDay = view.findViewById(R.id.textViewSurplusPerDay);
        textViewDaysForRecovery = view.findViewById(R.id.textViewDaysForRecovery);
        textViewMoneyAvailablePerDay = view.findViewById(R.id.textViewMoneyAvailablePerDay);
        bind();

        return view;
    }

    public void setData(surplusMoneyByCategory surplusMoneyByCategory) {
        this.surplusMoneyByCategory = surplusMoneyByCategory;
        if (getView() != null) {
            bind();
        }
    }

    private void bind() {
        if (surplusMoneyByCategory != null) {
            GregorianCalendar now = new GregorianCalendar();
            now.setTime(new Date());
            //TODO: this date can be the day after the data was collected
            double maximumPerDay = surplusMoneyByCategory.category.getMaximum() / now.getActualMaximum(Calendar.DAY_OF_MONTH);
            double spentMoney = surplusMoneyByCategory.category.getMaximum() - surplusMoneyByCategory.surplusMoney;
            double surplusMoneyPerDay = maximumPerDay * now.get(Calendar.DAY_OF_MONTH) - spentMoney;
            long daysForRecovery = surplusMoneyPerDay > 0 ? 0 : -Math.round(surplusMoneyPerDay / maximumPerDay);

            String stringMoneyAvailablePerDay = String.format(new Locale("es", "ES"), "%.2f", maximumPerDay) + " €";
            textViewMoneyAvailablePerDay.setText(stringMoneyAvailablePerDay);
            String stringSurplusPerDay = String.format(new Locale("es", "ES"), "%.2f", surplusMoneyPerDay) + " €";
            textViewSurplusPerDay.setText(stringSurplusPerDay);
            String stringDaysForRecovery = daysForRecovery + " days";
            textViewDaysForRecovery.setText(stringDaysForRecovery);
        }
    }
}
