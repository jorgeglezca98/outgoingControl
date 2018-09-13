package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.surplusMoneyTableAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.others.ItemOffsetDecoration;
import com.example.jorgegonzalezcabrera.outgoing.utilities.utils;

import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmResults;

public class mainFragment extends Fragment {

    private TextView textViewCurrentMoney;
    private TextView textViewOutgoingsOfTheMonth;
    private TextView textViewIncomesOfTheMonth;
    RecyclerView recyclerViewSurplusMoney;
    private surplusMoneyTableAdapter surplusMoneyAdapter;
    private double totalOutgoings;
    private double totalIncomes;
    private Context context;
    private Date dateOfLastUpdate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        textViewCurrentMoney = view.findViewById(R.id.textViewCurrentMoney);
        textViewOutgoingsOfTheMonth = view.findViewById(R.id.textViewOutgoingsOfTheMonth);
        textViewIncomesOfTheMonth = view.findViewById(R.id.textViewIncomeOfTheMonth);
        recyclerViewSurplusMoney = view.findViewById(R.id.recyclerViewSurplusMoney);

        bindUI();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void bindUI() {
        dateOfLastUpdate = new Date();
        Realm database = Realm.getDefaultInstance();
        appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();

        String currentMoney = String.format(new Locale("es", "ES"), "%.2f", currentConfiguration.getCurrentMoney()) + "€";
        textViewCurrentMoney.setText(currentMoney);

        Date date = utils.firstDateOfTheMonth(new Date());
        RealmResults<entry> entriesOfTheMonth = database.where(entry.class).greaterThanOrEqualTo("creationDate", date).findAll();

        Vector<surplusMoneyTableAdapter.surplusMoneyByCategory> surplusMoneyByCategoryVector = new Vector<>();
        totalOutgoings = 0;
        for (int i = 0; i < currentConfiguration.getOutgoingCategories().size(); i++) {
            double outgoingsByCategory = 0;
            for (int j = 0; j < currentConfiguration.getOutgoingCategories().get(i).getSubcategories().size(); j++) {
                String subcategoryName = currentConfiguration.getOutgoingCategories().get(i).getSubcategories().get(j).getName();
                outgoingsByCategory += entriesOfTheMonth.where().equalTo("category", subcategoryName).sum("valor").doubleValue();
            }
            totalOutgoings += outgoingsByCategory;
            double surplusMoneyByCategory = currentConfiguration.getOutgoingCategories().get(i).getMaximum() - outgoingsByCategory;
            outgoingCategory outgoingCategory = currentConfiguration.getOutgoingCategories().get(i);
            surplusMoneyByCategoryVector.add(new surplusMoneyTableAdapter.surplusMoneyByCategory(outgoingCategory, surplusMoneyByCategory));
        }

        String outgoingsOfTheMonth = String.format(new Locale("es", "ES"), "%.2f", totalOutgoings) + "€";
        textViewOutgoingsOfTheMonth.setText(outgoingsOfTheMonth);

        totalIncomes = 0;
        for (int i = 0; i < currentConfiguration.getIncomeCategories().size(); i++) {
            String subcategoryName = currentConfiguration.getIncomeCategories().get(i).getName();
            totalIncomes += entriesOfTheMonth.where().equalTo("category", subcategoryName).sum("valor").doubleValue();
        }
        String incomesOfTheMonth = String.format(new Locale("es", "ES"), "%.2f", totalIncomes) + "€";
        textViewIncomesOfTheMonth.setText(incomesOfTheMonth);

        if (surplusMoneyAdapter == null) {
            surplusMoneyAdapter = new surplusMoneyTableAdapter(getFragmentManager(), surplusMoneyByCategoryVector);
            recyclerViewSurplusMoney.setAdapter(surplusMoneyAdapter);
            recyclerViewSurplusMoney.setLayoutManager(new GridLayoutManager(context, 2));
            recyclerViewSurplusMoney.addItemDecoration(new ItemOffsetDecoration(context, 5));
        } else {
            surplusMoneyAdapter.refresh(surplusMoneyByCategoryVector);
        }
    }

    public void updateData() {
        if (getView() != null) {
            bindUI();
        }
    }

    public void updateDataAdded(final entry newEntry) {
        if (getView() != null) {
            appConfiguration currentConfiguration = Realm.getDefaultInstance().where(appConfiguration.class).findFirst();
            String currentMoney = String.format(new Locale("es", "ES"), "%.2f", currentConfiguration.getCurrentMoney()) + "€";
            textViewCurrentMoney.setText(currentMoney);
            if (utils.areFromTheSameMonth(newEntry.getCreationDate(), dateOfLastUpdate)) {
                if (newEntry.getType() == entry.type.OUTGOING) {
                    totalOutgoings += newEntry.getValor();
                    String outgoingsOfTheMonth = String.format(new Locale("es", "ES"), "%.2f", totalOutgoings) + "€";
                    textViewOutgoingsOfTheMonth.setText(outgoingsOfTheMonth);
                    surplusMoneyAdapter.updateData(newEntry.getCategory(), newEntry.getValor(), true);
                } else {
                    totalIncomes += newEntry.getValor();
                    String incomesOfTheMonth = String.format(new Locale("es", "ES"), "%.2f", totalIncomes) + "€";
                    textViewIncomesOfTheMonth.setText(incomesOfTheMonth);
                }
            }
        }
    }

    public void updateDataRemoved(entry removedEntry) {
        if (getView() != null) {
            appConfiguration currentConfiguration = Realm.getDefaultInstance().where(appConfiguration.class).findFirst();
            String currentMoney = String.format(new Locale("es", "ES"), "%.2f", currentConfiguration.getCurrentMoney()) + "€";
            textViewCurrentMoney.setText(currentMoney);
            if (utils.areFromTheSameMonth(removedEntry.getCreationDate(), dateOfLastUpdate)) {
                if (removedEntry.getType() == entry.type.OUTGOING) {
                    totalOutgoings -= removedEntry.getValor();
                    String outgoingsOfTheMonth = String.format(new Locale("es", "ES"), "%.2f", totalOutgoings) + "€";
                    textViewOutgoingsOfTheMonth.setText(outgoingsOfTheMonth);
                    surplusMoneyAdapter.updateData(removedEntry.getCategory(), removedEntry.getValor(), false);
                } else {
                    totalIncomes -= removedEntry.getValor();
                    String incomesOfTheMonth = String.format(new Locale("es", "ES"), "%.2f", totalIncomes) + "€";
                    textViewIncomesOfTheMonth.setText(incomesOfTheMonth);
                }
            }
        }
    }

    public void updateDataModified(entry currentVersion, entry nextVersion) {
        if (getView() != null) {
            appConfiguration currentConfiguration = Realm.getDefaultInstance().where(appConfiguration.class).findFirst();
            String currentMoney = String.format(new Locale("es", "ES"), "%.2f", currentConfiguration.getCurrentMoney()) + "€";
            textViewCurrentMoney.setText(currentMoney);

            boolean isCurrentVersionFromThisMonth = utils.areFromTheSameMonth(dateOfLastUpdate, currentVersion.getCreationDate());
            boolean isNextVersionFromThisMonth = utils.areFromTheSameMonth(dateOfLastUpdate, nextVersion.getCreationDate());

            if (isCurrentVersionFromThisMonth) {
                if (currentVersion.getType() == entry.type.OUTGOING) {
                    totalOutgoings -= currentVersion.getValor();
                } else {
                    totalIncomes -= currentVersion.getValor();
                }
            }

            if (isNextVersionFromThisMonth) {
                if (nextVersion.getType() == entry.type.OUTGOING) {
                    totalOutgoings += nextVersion.getValor();
                } else {
                    totalIncomes += nextVersion.getValor();
                }
            }

            if (isCurrentVersionFromThisMonth || isNextVersionFromThisMonth) {
                String outgoingsOfTheMonth = String.format(new Locale("es", "ES"), "%.2f", totalOutgoings) + "€";
                textViewOutgoingsOfTheMonth.setText(outgoingsOfTheMonth);
                String incomesOfTheMonth = String.format(new Locale("es", "ES"), "%.2f", totalIncomes) + "€";
                textViewIncomesOfTheMonth.setText(incomesOfTheMonth);
                surplusMoneyAdapter.modifyData(currentVersion, nextVersion, dateOfLastUpdate);
            }
        }
    }
}