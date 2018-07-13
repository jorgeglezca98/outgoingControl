package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity;
import com.example.jorgegonzalezcabrera.outgoing.adapters.surplusMoneyTableAdapter;
import com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;

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
        View view = inflater.inflate(R.layout.main_fragment,container,false);

        bindUI(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            NewEntryAddedInterface = (OnNewEntryAddedInterface) context;
        }catch (Exception e){
            NewEntryAddedInterface = new OnNewEntryAddedInterface() {
                @Override
                public void OnNewEntryAdded(entry newEntry) {

                }
            };
        }
    }

    public void bindUI(View view){
        database = Realm.getDefaultInstance();
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                currentConfiguration = database.where(appConfiguration.class).findFirst();
            }
        });

        textViewCurrentMoney = view.findViewById(R.id.textViewCurrentMoney);
        textViewCurrentMoney.setText(String.format("%.2f", currentConfiguration.getCurrentMoney()) + "€");

        surplusMoneyByCategoryVector = new Vector<>();
        totalOutgoings = 0;
        double aux;
        textViewOutgoingsOfTheMonth = view.findViewById(R.id.textViewOutgoingsOfTheMonth);
        for (int i = 0; i < currentConfiguration.getOutgoingCategoriesCategories().size(); i++) {
            aux = 0;
            for (int j = 0; j < currentConfiguration.getOutgoingCategoriesCategories().get(i).getSubcategories().size(); j++) {
                aux += database.where(entry.class).equalTo("category", currentConfiguration.getOutgoingCategoriesCategories().get(i).getSubcategories().get(j).getName()).sum("valor").doubleValue();
            }
            totalOutgoings += aux;
            surplusMoneyByCategoryVector.add(new surplusMoneyTableAdapter.surplusMoneyByCategory(currentConfiguration.getOutgoingCategoriesCategories().get(i), currentConfiguration.getOutgoingCategoriesCategories().get(i).getMaximum() - aux));

        }
        textViewOutgoingsOfTheMonth.setText(String.format("%.2f", totalOutgoings) + "€");

        totalIncomes = 0;
        textViewIncomesOfTheMonth = view.findViewById(R.id.textViewIncomeOfTheMonth);
        for (int i = 0; i < currentConfiguration.getIncomeCategories().size(); i++) {
            totalIncomes += database.where(entry.class).equalTo("category", currentConfiguration.getIncomeCategories().get(i).getName()).sum("valor").doubleValue();
        }
        textViewIncomesOfTheMonth.setText(String.format("%.2f", totalIncomes) + "€");

        recyclerViewSurplusMoney = view.findViewById(R.id.recyclerViewSurplusMoney);
        recyclerViewSurplusMoney.setAdapter(new surplusMoneyTableAdapter(surplusMoneyByCategoryVector));
        recyclerViewSurplusMoney.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newEntryDialog(getContext(), currentConfiguration.getOutgoingCategoriesCategories(),
                        currentConfiguration.getIncomeCategories(), new dialogs.OnNewEntryAccepted() {
                            @Override
                            public void OnClick(final String subcategory, final int type, final double value, final String description) {
                                database.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        final entry newEntry = new entry(value, type, subcategory, description);
                                        if (type == entry.type.OUTGOING.ordinal()) {
                                            updateAfterOutgoing(value,subcategory);
                                        } else {
                                            updateAfterIncome(value);
                                        }
                                        database.copyToRealmOrUpdate(currentConfiguration);
                                        database.copyToRealm(newEntry);
                                        NewEntryAddedInterface.OnNewEntryAdded(newEntry);
                                    }
                                });
                            }
                        });
            }
        });
    }

    public void updateAfterOutgoing(double value, String subcategory){
        currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() - value);
        textViewCurrentMoney.setText(String.format("%.2f", currentConfiguration.getCurrentMoney()) + "€");
        totalOutgoings += value;
        textViewOutgoingsOfTheMonth.setText(String.format("%.2f", totalOutgoings) + "€");
        ((surplusMoneyTableAdapter) recyclerViewSurplusMoney.getAdapter()).updateData(subcategory, value);
    }

    public void updateAfterIncome(double value){
        currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() + value);
        textViewCurrentMoney.setText(String.format("%.2f", currentConfiguration.getCurrentMoney()) + "€");
        totalIncomes += value;
        textViewIncomesOfTheMonth.setText(String.format("%.2f", totalIncomes) + "€");
    }

    public interface OnNewEntryAddedInterface{
        void OnNewEntryAdded(entry newEntry);
    }
}