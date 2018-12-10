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
import com.example.jorgegonzalezcabrera.outgoing.models.moneyController;
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
        RealmResults<moneyController> outgoingCategories = database.where(moneyController.class).findAll();
        for (int i = 0; i < outgoingCategories.size(); i++) {
            double outgoingsByCategory = 0;
            for (int j = 0; j < outgoingCategories.get(i).getSubcategories().size(); j++) {
                String subcategoryName = outgoingCategories.get(i).getSubcategories().get(j).getName();
                outgoingsByCategory += entriesOfTheMonth.where().equalTo("category", subcategoryName).sum("valor").doubleValue();
            }
            double surplusMoneyByCategory = outgoingCategories.get(i).getMaximum() - outgoingsByCategory;
            moneyController moneyController = outgoingCategories.get(i);
            surplusMoneyByCategoryVector.add(new surplusMoneyTableAdapter.surplusMoneyByCategory(moneyController, surplusMoneyByCategory));
        }
        totalOutgoings = entriesOfTheMonth.where().equalTo("type", entry.type.OUTGOING.ordinal()).sum("valor").longValue();
        String outgoingsOfTheMonth = String.format(new Locale("es", "ES"), "%.2f", totalOutgoings) + "€";
        textViewOutgoingsOfTheMonth.setText(outgoingsOfTheMonth);

        totalIncomes = entriesOfTheMonth.where().equalTo("type", entry.type.INCOME.ordinal()).sum("valor").longValue();
        String incomesOfTheMonth = String.format(new Locale("es", "ES"), "%.2f", totalIncomes) + "€";
        textViewIncomesOfTheMonth.setText(incomesOfTheMonth);

        surplusMoneyAdapter = new surplusMoneyTableAdapter(getFragmentManager(), surplusMoneyByCategoryVector);
        recyclerViewSurplusMoney.setAdapter(surplusMoneyAdapter);
        recyclerViewSurplusMoney.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerViewSurplusMoney.addItemDecoration(new ItemOffsetDecoration(context, 5));
    }

    private void updateUI() {
        dateOfLastUpdate = new Date();
        Realm database = Realm.getDefaultInstance();
        appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();

        String currentMoney = String.format(new Locale("es", "ES"), "%.2f", currentConfiguration.getCurrentMoney()) + "€";
        textViewCurrentMoney.setText(currentMoney);

        Date date = utils.firstDateOfTheMonth(new Date());
        RealmResults<entry> entriesOfTheMonth = database.where(entry.class).greaterThanOrEqualTo("creationDate", date).findAll();

        Vector<surplusMoneyTableAdapter.surplusMoneyByCategory> surplusMoneyByCategoryVector = new Vector<>();
        RealmResults<moneyController> outgoingCategories = database.where(moneyController.class).findAll();
        for (int i = 0; i < outgoingCategories.size(); i++) {
            double outgoingsByCategory = 0;
            for (int j = 0; j < outgoingCategories.get(i).getSubcategories().size(); j++) {
                String subcategoryName = outgoingCategories.get(i).getSubcategories().get(j).getName();
                outgoingsByCategory += entriesOfTheMonth.where().equalTo("category", subcategoryName).sum("valor").doubleValue();
            }
            double surplusMoneyByCategory = outgoingCategories.get(i).getMaximum() - outgoingsByCategory;
            moneyController moneyController = outgoingCategories.get(i);
            surplusMoneyByCategoryVector.add(new surplusMoneyTableAdapter.surplusMoneyByCategory(moneyController, surplusMoneyByCategory));
        }
        totalOutgoings = entriesOfTheMonth.where().equalTo("type", entry.type.OUTGOING.ordinal()).sum("valor").longValue();
        String outgoingsOfTheMonth = String.format(new Locale("es", "ES"), "%.2f", totalOutgoings) + "€";
        textViewOutgoingsOfTheMonth.setText(outgoingsOfTheMonth);

        totalIncomes = entriesOfTheMonth.where().equalTo("type", entry.type.INCOME.ordinal()).sum("valor").longValue();
        String incomesOfTheMonth = String.format(new Locale("es", "ES"), "%.2f", totalIncomes) + "€";
        textViewIncomesOfTheMonth.setText(incomesOfTheMonth);

        surplusMoneyAdapter.refresh(surplusMoneyByCategoryVector);
    }


    public void updateData() {
        if (getView() != null) {
            updateUI();
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

    public void updateCategoryRemoved(@NonNull moneyController category) {
        surplusMoneyAdapter.removeCategory(category);
    }

    public void updateCategoryAdded(@NonNull moneyController newMoneyController) {
        surplusMoneyAdapter.addCategory(newMoneyController);
    }

    public void updateCategoryNameChanged(moneyController modifiedMoneyController) {
        surplusMoneyAdapter.updateCategoryName(modifiedMoneyController);
    }

    public void updateCategoryItem(moneyController modifiedMoneyController) {
        surplusMoneyAdapter.updateItem(modifiedMoneyController);
    }
}