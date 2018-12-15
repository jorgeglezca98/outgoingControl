package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jorgegonzalezcabrera.outgoing.R;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_fragment, container, false);

        BarChart barChart = view.findViewById(R.id.barChart);
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
        barChart.invalidate();

        Realm database = Realm.getDefaultInstance();
        GregorianCalendar smallerDate = new GregorianCalendar();
        smallerDate.setTime(utils.firstDateOfTheMonth(database.where(entry.class).minimumDate("creationDate")));
        Date lastEntryMade = database.where(entry.class).maximumDate("creationDate");
        GregorianCalendar biggerDate = new GregorianCalendar();
        biggerDate.setTime(smallerDate.getTime());
        biggerDate.add(Calendar.MONTH, 1);

        List<BarEntry> entries = new ArrayList<>();
        float i = 1f;
        do {
            float valor = database.where(entry.class).equalTo("type", entry.type.OUTGOING.ordinal()).between("creationDate", smallerDate.getTime(), biggerDate.getTime()).sum("valor").floatValue();
            entries.add(new BarEntry(i, valor));
            i++;
            smallerDate.add(Calendar.MONTH, 1);
            biggerDate.add(Calendar.MONTH, 1);
        } while (smallerDate.getTime().getTime() < lastEntryMade.getTime());

        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColor(getResources().getColor(R.color.primary2));
        BarData data = new BarData(set);
        data.setBarWidth(0.9f);
        barChart.setData(data);
        return view;
    }
}
