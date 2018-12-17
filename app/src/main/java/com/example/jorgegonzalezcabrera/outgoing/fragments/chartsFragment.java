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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class chartsFragment extends Fragment {

    private BarData barOutgoingData;
    private BarData barIncomeData;
    private PieData pieOutgoingData;
    private PieData pieIncomeData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_fragment, container, false);

        Realm database = Realm.getDefaultInstance();

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

        final PieChart pieChart = view.findViewById(R.id.pieChart);

        RealmList<category> outgoingCategories = new RealmList<>();
        outgoingCategories.addAll(database.where(category.class).equalTo("type", category.typeOfCategory.OUTGOING.ordinal()).findAll());
        RealmList<category> incomeCategories = new RealmList<>();
        incomeCategories.addAll(database.where(category.class).equalTo("type", category.typeOfCategory.INCOME.ordinal()).findAll());

        Date firstEntryMade = database.where(entry.class).minimumDate("creationDate");
        if (firstEntryMade != null) {
            firstEntryMade = utils.firstDateOfTheMonth(firstEntryMade);
            GregorianCalendar smallerDate = new GregorianCalendar();
            smallerDate.setTime(firstEntryMade);
            Date lastEntryMade = database.where(entry.class).maximumDate("creationDate");
            GregorianCalendar biggerDate = new GregorianCalendar();
            biggerDate.setTime(smallerDate.getTime());
            biggerDate.add(Calendar.MONTH, 1);

            List<BarEntry> barOutgoingEntries = new ArrayList<>();
            List<BarEntry> barIncomeEntries = new ArrayList<>();
            List<PieEntry> pieOutgoingEntries = new ArrayList<>();
            List<PieEntry> pieIncomeEntries = new ArrayList<>();
            float i = 1f;
            do {
                float outgoingValueByMonth = 0;
                for (int j = 0; j < outgoingCategories.size(); j++) {
                    float outgoingsPerMonthAndCategory = database.where(entry.class).equalTo("categoryName", outgoingCategories.get(j).getName()).between("creationDate", smallerDate.getTime(), biggerDate.getTime()).sum("valor").floatValue();
                    outgoingValueByMonth += outgoingsPerMonthAndCategory;
                    pieOutgoingEntries.add(new PieEntry(outgoingsPerMonthAndCategory));
                }
                float incomeValueByMonth = 0;
                for (int j = 0; j < incomeCategories.size(); j++) {
                    float incomesPerMonthAndCategory = database.where(entry.class).equalTo("categoryName", incomeCategories.get(j).getName()).between("creationDate", smallerDate.getTime(), biggerDate.getTime()).sum("valor").floatValue();
                    incomeValueByMonth += incomesPerMonthAndCategory;
                    pieIncomeEntries.add(new PieEntry(incomesPerMonthAndCategory));
                }
                barOutgoingEntries.add(new BarEntry(i, outgoingValueByMonth));
                barIncomeEntries.add(new BarEntry(i, incomeValueByMonth));

                i++;
                smallerDate.add(Calendar.MONTH, 1);
                biggerDate.add(Calendar.MONTH, 1);
            } while (smallerDate.getTime().getTime() < lastEntryMade.getTime());

            BarDataSet barOutgoingSet = new BarDataSet(barOutgoingEntries, "BarDataSet");
            barOutgoingSet.setColor(getResources().getColor(R.color.primary2));
            barOutgoingData = new BarData(barOutgoingSet);
            barOutgoingData.setBarWidth(0.9f);
            barChart.setData(barOutgoingData);

            BarDataSet barIncomeSet = new BarDataSet(barIncomeEntries, "BarDataSet");
            barIncomeSet.setColor(getResources().getColor(R.color.primary2));
            barIncomeData = new BarData(barIncomeSet);
            barIncomeData.setBarWidth(0.9f);

            PieDataSet pieOutgoingSet = new PieDataSet(pieOutgoingEntries, "PieDataSet");
            pieOutgoingSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            pieOutgoingData = new PieData(pieOutgoingSet);
            pieChart.setData(pieOutgoingData);

            PieDataSet pieIncomeSet = new PieDataSet(pieIncomeEntries, "PieDataSet");
            pieIncomeSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            pieIncomeData = new PieData(pieIncomeSet);

            pieChart.invalidate();
            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {

                }

                @Override
                public void onNothingSelected() {

                }
            });
            barChart.invalidate();
        }

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    barChart.setData(barOutgoingData);
                    barChart.invalidate();
                    pieChart.setData(pieOutgoingData);
                    pieChart.invalidate();
                } else if (tab.getPosition() == 1) {
                    barChart.setData(barIncomeData);
                    barChart.invalidate();
                    pieChart.setData(pieIncomeData);
                    pieChart.invalidate();
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
