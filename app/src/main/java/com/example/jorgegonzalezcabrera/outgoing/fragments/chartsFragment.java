package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.utilities.utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;

public class chartsFragment extends Fragment {

    private BarData outgoingData;
    private BarData incomeData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_fragment, container, false);

        final BarChart barChart = view.findViewById(R.id.barChart);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getLegend().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setFitBars(true);
        barChart.invalidate();
        Description d = new Description();
        d.setText("");
        barChart.setDescription(d);

        Realm database = Realm.getDefaultInstance();
        Date firstEntryMade = database.where(entry.class).minimumDate("creationDate");
        if (firstEntryMade != null) {
            firstEntryMade = utils.firstDateOfTheMonth(firstEntryMade);
            GregorianCalendar smallerDate = new GregorianCalendar();
            smallerDate.setTime(firstEntryMade);
            Date lastEntryMade = database.where(entry.class).maximumDate("creationDate");
            GregorianCalendar biggerDate = new GregorianCalendar();
            biggerDate.setTime(smallerDate.getTime());
            biggerDate.add(Calendar.MONTH, 1);

            List<BarEntry> outgoings = new ArrayList<>();
            List<BarEntry> incomes = new ArrayList<>();
            float i = 1f;
            do {
                float outgoingValueByMonth = database.where(entry.class).equalTo("type", category.typeOfCategory.OUTGOING.ordinal()).between("creationDate", smallerDate.getTime(), biggerDate.getTime()).sum("valor").floatValue();
                float incomeValueByMonth = database.where(entry.class).equalTo("type", category.typeOfCategory.INCOME.ordinal()).between("creationDate", smallerDate.getTime(), biggerDate.getTime()).sum("valor").floatValue();
                outgoings.add(new BarEntry(i, outgoingValueByMonth));
                incomes.add(new BarEntry(i, incomeValueByMonth));
                i++;
                smallerDate.add(Calendar.MONTH, 1);
                biggerDate.add(Calendar.MONTH, 1);
            } while (smallerDate.getTime().getTime() < lastEntryMade.getTime());

            BarDataSet outgoingSet = new BarDataSet(outgoings, "BarDataSet");
            outgoingSet.setColor(getResources().getColor(R.color.primary2));
            outgoingData = new BarData(outgoingSet);
            outgoingData.setBarWidth(0.9f);
            barChart.setData(outgoingData);

            BarDataSet incomeSet = new BarDataSet(incomes, "BarDataSet");
            incomeSet.setColor(getResources().getColor(R.color.primary2));
            incomeData = new BarData(incomeSet);
            incomeData.setBarWidth(0.9f);

            barChart.invalidate();
        }

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    barChart.setData(outgoingData);
                    barChart.invalidate();
                } else if (tab.getPosition() == 1) {
                    barChart.setData(incomeData);
                    barChart.invalidate();
                } else if (tab.getPosition() == 2) {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}
