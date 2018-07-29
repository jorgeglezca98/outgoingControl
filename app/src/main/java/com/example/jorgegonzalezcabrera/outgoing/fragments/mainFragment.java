package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.surplusMoneyTableAdapter;
import com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;

import java.util.Locale;
import java.util.Vector;

import io.realm.Realm;

import static com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs.newEntryDialog;

public class mainFragment extends Fragment {

    appConfiguration currentConfiguration;
    Realm database;
    TextView textViewCurrentMoney;
    TextView textViewOutgoingsOfTheMonth;
    TextView textViewIncomesOfTheMonth;
    RecyclerView recyclerViewSurplusMoney;
    double totalOutgoings;
    double totalIncomes;
    Vector<surplusMoneyTableAdapter.surplusMoneyByCategory> surplusMoneyByCategoryVector;
    OnNewEntryAddedInterface NewEntryAddedInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        bindUI(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            NewEntryAddedInterface = (OnNewEntryAddedInterface) context;
        } catch (Exception e) {
            NewEntryAddedInterface = new OnNewEntryAddedInterface() {
                @Override
                public void OnNewEntryAdded(entry newEntry) {

                }
            };
        }
    }

    public void bindUI(View view) {
        database = Realm.getDefaultInstance();
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                currentConfiguration = database.where(appConfiguration.class).findFirst();
            }
        });

        textViewCurrentMoney = view.findViewById(R.id.textViewCurrentMoney);
        textViewCurrentMoney.setText(String.format(new Locale("es", "ES"), "%.2f", currentConfiguration.getCurrentMoney()) + "€");

        surplusMoneyByCategoryVector = new Vector<>();
        totalOutgoings = 0;
        double aux;
        textViewOutgoingsOfTheMonth = view.findViewById(R.id.textViewOutgoingsOfTheMonth);
        for (int i = 0; i < currentConfiguration.getOutgoingCategories().size(); i++) {
            aux = 0;
            for (int j = 0; j < currentConfiguration.getOutgoingCategories().get(i).getSubcategories().size(); j++) {
                aux += database.where(entry.class).equalTo("category", currentConfiguration.getOutgoingCategories().get(i).getSubcategories().get(j).getName()).sum("valor").doubleValue();
            }
            totalOutgoings += aux;
            surplusMoneyByCategoryVector.add(new surplusMoneyTableAdapter.surplusMoneyByCategory(currentConfiguration.getOutgoingCategories().get(i), currentConfiguration.getOutgoingCategories().get(i).getMaximum() - aux));

        }
        textViewOutgoingsOfTheMonth.setText(String.format(new Locale("es", "ES"), "%.2f", totalOutgoings) + "€");

        totalIncomes = 0;
        textViewIncomesOfTheMonth = view.findViewById(R.id.textViewIncomeOfTheMonth);
        for (int i = 0; i < currentConfiguration.getIncomeCategories().size(); i++) {
            totalIncomes += database.where(entry.class).equalTo("category", currentConfiguration.getIncomeCategories().get(i).getName()).sum("valor").doubleValue();
        }
        textViewIncomesOfTheMonth.setText(String.format(new Locale("es", "ES"), "%.2f", totalIncomes) + "€");

        recyclerViewSurplusMoney = view.findViewById(R.id.recyclerViewSurplusMoney);
        recyclerViewSurplusMoney.setAdapter(new surplusMoneyTableAdapter(surplusMoneyByCategoryVector));
        recyclerViewSurplusMoney.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newEntryDialog(getContext(), currentConfiguration.getOutgoingCategories(),
                        currentConfiguration.getIncomeCategories(), new dialogs.OnNewEntryAccepted() {
                            @Override
                            public void OnClick(final String subcategory, final int type, final double value, final String description) {
                                entry.type aux = type==entry.type.OUTGOING.ordinal() ? entry.type.OUTGOING : entry.type.INCOME ;
                                final entry newEntry = new entry(value, aux, subcategory, description);
                                if (type == entry.type.OUTGOING.ordinal()) {
                                    updateAfterOutgoing(newEntry);
                                } else {
                                    updateAfterIncome(newEntry);
                                }
                            }
                        });
            }
        });
    }

    public void updateAfterOutgoing(final entry newEntry) {
        if(currentConfiguration!=null) {
            currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() - newEntry.getValor());
            textViewCurrentMoney.setText(String.format(new Locale("es", "ES"), "%.2f", currentConfiguration.getCurrentMoney()) + "€");
            totalOutgoings += newEntry.getValor();
            textViewOutgoingsOfTheMonth.setText(String.format(new Locale("es", "ES"), "%.2f", totalOutgoings) + "€");
            ((surplusMoneyTableAdapter) recyclerViewSurplusMoney.getAdapter()).updateData(newEntry.getCategory(), newEntry.getValor());

            database.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    database.copyToRealmOrUpdate(currentConfiguration);
                    database.copyToRealm(newEntry);
                }
            });

            NewEntryAddedInterface.OnNewEntryAdded(newEntry);
        } else{
            appConfiguration configuration;
            Realm.getDefaultInstance().beginTransaction();
            configuration = Realm.getDefaultInstance().where(appConfiguration.class).findFirst();
            if(configuration!=null){
                configuration.setCurrentMoney(configuration.getCurrentMoney() - newEntry.getValor());
                Realm.getDefaultInstance().copyToRealmOrUpdate(configuration);
                Realm.getDefaultInstance().copyToRealm(newEntry);
            }
            Realm.getDefaultInstance().commitTransaction();
        }
    }

    public void updateAfterIncome(final entry newEntry) {
        if(currentConfiguration!=null) {
            currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() + newEntry.getValor());
            textViewCurrentMoney.setText(String.format(new Locale("es", "ES"), "%.2f", currentConfiguration.getCurrentMoney()) + "€");
            totalIncomes += newEntry.getValor();
            textViewIncomesOfTheMonth.setText(String.format(new Locale("es", "ES"), "%.2f", totalIncomes) + "€");

            database.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    database.copyToRealmOrUpdate(currentConfiguration);
                    database.copyToRealm(newEntry);
                }
            });

            NewEntryAddedInterface.OnNewEntryAdded(newEntry);
        } else {
            appConfiguration configuration;
            Realm.getDefaultInstance().beginTransaction();
            configuration = Realm.getDefaultInstance().where(appConfiguration.class).findFirst();
            Realm.getDefaultInstance().commitTransaction();
            if(configuration!=null){
                configuration.setCurrentMoney(configuration.getCurrentMoney() + newEntry.getValor());
                Realm.getDefaultInstance().beginTransaction();
                Realm.getDefaultInstance().copyToRealmOrUpdate(configuration);
                Realm.getDefaultInstance().copyToRealm(newEntry);
                Realm.getDefaultInstance().commitTransaction();
            }
        }
    }

    public interface OnNewEntryAddedInterface {
        void OnNewEntryAdded(entry newEntry);
    }
}