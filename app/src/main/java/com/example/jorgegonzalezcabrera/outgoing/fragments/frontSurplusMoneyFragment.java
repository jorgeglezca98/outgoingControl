package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.surplusMoneyTableAdapter.surplusMoneyByCategory;

import java.util.Locale;

public class frontSurplusMoneyFragment extends Fragment {

    private ProgressBar progressBarSurplusMoney;
    private TextView textViewPercentageSurplusMoney;
    private TextView textViewCategoryName;
    private surplusMoneyByCategory surplusMoneyByCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.front_item_of_surplus_money, container, false);

        progressBarSurplusMoney = view.findViewById(R.id.progressBarSurplusMoney);
        textViewPercentageSurplusMoney = view.findViewById(R.id.textViewPercentageSurplusMoney);
        textViewCategoryName = view.findViewById(R.id.textViewCategoryName);
        bind();

        return view;
    }

    public void setData(surplusMoneyByCategory surplusMoneyByCategory) {
        this.surplusMoneyByCategory = surplusMoneyByCategory;
    }

    private void bind() {

        if (surplusMoneyByCategory != null) {
            double spentMoney = surplusMoneyByCategory.category.getMaximum() - surplusMoneyByCategory.surplusMoney;
            String percentageSurplusMoney = String.format(new Locale("es", "ES"), "%.2f", spentMoney) + " / ";
            percentageSurplusMoney += String.format(new Locale("es", "ES"), "%.2f", surplusMoneyByCategory.category.getMaximum());

            textViewPercentageSurplusMoney.setText(percentageSurplusMoney);
            textViewCategoryName.setText(surplusMoneyByCategory.category.getName());
        }

    }
}
