package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.erasableItemsAdapter;
import com.example.jorgegonzalezcabrera.outgoing.adapters.newOutgoingCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.incomeCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;

import io.realm.Realm;
import io.realm.RealmList;

public class initialConfigurationActivity extends AppCompatActivity {

    private RecyclerView outgoingCategoriesRecyclerView;
    private RecyclerView incomeCategoriesRecyclerView;
    private EditText editTextInitialMoney;
    private newOutgoingCategoriesAdapter outgoingCategoriesAdapter;
    private erasableItemsAdapter incomeCategoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_configuration);

        outgoingCategoriesRecyclerView = findViewById(R.id.recyclerViewOutgoingCategories);
        outgoingCategoriesAdapter = new newOutgoingCategoriesAdapter(this);
        outgoingCategoriesRecyclerView.setAdapter(outgoingCategoriesAdapter);
        final LinearLayoutManager outgoingCategoriesLayoutManager = new LinearLayoutManager(this);
        outgoingCategoriesRecyclerView.setLayoutManager(outgoingCategoriesLayoutManager);

        incomeCategoriesRecyclerView = findViewById(R.id.recyclerViewIncomeCategories);
        incomeCategoriesAdapter = new erasableItemsAdapter();
        incomeCategoriesRecyclerView.setAdapter(incomeCategoriesAdapter);
        final LinearLayoutManager incomeCategoriesLayoutManager = new LinearLayoutManager(this);
        incomeCategoriesRecyclerView.setLayoutManager(incomeCategoriesLayoutManager);

        editTextInitialMoney = findViewById(R.id.editTextInitialMoney);
        ImageButton imageButtonAddOutgoingCategory = findViewById(R.id.imageButtonAddOutgoingCategory);
        ImageButton imageButtonDeleteOutgoingCategory = findViewById(R.id.imageButtonDeleteOutgoingCategory);
        ImageButton imageButtonAddIncomeCategory = findViewById(R.id.imageButtonAddIncomeCategory);
        ImageButton imageButtonDeleteIncomeCategory = findViewById(R.id.imageButtonDeleteIncomeCategory);
        Button buttonApplyConfiguration = findViewById(R.id.buttonApplyConfiguration);

        imageButtonAddOutgoingCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outgoingCategoriesAdapter.addOne();
            }
        });

        imageButtonDeleteOutgoingCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outgoingCategoriesAdapter.deleteLast();
            }
        });

        imageButtonAddIncomeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomeCategoriesAdapter.addOne();
            }
        });

        imageButtonDeleteIncomeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incomeCategoriesAdapter.deleteLast();
            }
        });

        buttonApplyConfiguration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()) {
                    double currentMoney = Double.valueOf(editTextInitialMoney.getText().toString());
                    appConfiguration newConfiguration = new appConfiguration(currentMoney, getOutgoingCategories(), getIncomeCategories());

                    Realm database = Realm.getDefaultInstance();
                    database.beginTransaction();
                    database.copyToRealm(newConfiguration);
                    database.commitTransaction();

                    Intent intent = new Intent(initialConfigurationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private RealmList<outgoingCategory> getOutgoingCategories() {
        RealmList<outgoingCategory> result = new RealmList<>();
        newOutgoingCategoriesAdapter.ViewHolder viewHolder;

        for (int i = 0; i < outgoingCategoriesAdapter.getItemCount(); i++) {
            viewHolder = (newOutgoingCategoriesAdapter.ViewHolder) outgoingCategoriesRecyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                double max = Double.valueOf(viewHolder.max.getText().toString());
                String categoryName = viewHolder.name.getText().toString();
                result.add(new outgoingCategory(viewHolder.getSubcategories(), max, categoryName));
            }
        }
        return result;
    }

    private RealmList<incomeCategory> getIncomeCategories() {
        RealmList<incomeCategory> result = new RealmList<>();
        erasableItemsAdapter.ViewHolder viewHolder;
        for (int i = 0; i < incomeCategoriesAdapter.getItemCount(); i++) {
            viewHolder = (erasableItemsAdapter.ViewHolder) incomeCategoriesRecyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                result.add(new incomeCategory(viewHolder.name.getText().toString()));
            }
        }
        return result;
    }

    private boolean checkData() {
        boolean result = true;
        if (editTextInitialMoney.getText().toString().isEmpty()) {
            result = false;
        } else {
            newOutgoingCategoriesAdapter.ViewHolder outgoingViewHolder;
            for (int i = 0; i < outgoingCategoriesAdapter.getItemCount(); i++) {
                outgoingViewHolder = (newOutgoingCategoriesAdapter.ViewHolder) outgoingCategoriesRecyclerView.findViewHolderForAdapterPosition(i);
                if (outgoingViewHolder.name.getText().toString().isEmpty()) {
                    outgoingViewHolder.name.setHintTextColor(getResources().getColor(R.color.colorWrong));
                    result = false;
                } else if (outgoingViewHolder.max.getText().toString().isEmpty()) {
                    result = false;
                }
            }

            erasableItemsAdapter.ViewHolder incomeViewHolder;
            for (int i = 0; i < incomeCategoriesAdapter.getItemCount(); i++) {
                incomeViewHolder = (erasableItemsAdapter.ViewHolder) incomeCategoriesRecyclerView.findViewHolderForAdapterPosition(i);
                if (incomeViewHolder.name.getText().toString().isEmpty()) {
                    incomeViewHolder.name.setHintTextColor(getResources().getColor(R.color.colorWrong));
                    result = false;
                }
            }
        }
        return result;
    }
}
