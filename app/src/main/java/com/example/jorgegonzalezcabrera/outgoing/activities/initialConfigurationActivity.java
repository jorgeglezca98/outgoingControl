package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.newIncomeCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.adapters.newOutgoingCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.incomeCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.subcategory;

import io.realm.Realm;
import io.realm.RealmList;

public class initialConfigurationActivity extends AppCompatActivity {

    private RecyclerView outgoingCategoriesRecyclerView;
    private RecyclerView incomeCategoriesRecyclerView;
    private ImageButton imageButtonAddOutgoingCategory;
    private ImageButton imageButtonDeleteOutgoingCategory;
    private ImageButton imageButtonAddIncomeCategory;
    private ImageButton imageButtonDeleteIncomeCategory;
    private EditText editTextInitialMoney;
    private Button buttonApplyConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_configuration);

        outgoingCategoriesRecyclerView = findViewById(R.id.recyclerViewOutgoingCategories);
        final newOutgoingCategoriesAdapter outgoingCategoriesAdapter = new newOutgoingCategoriesAdapter(this);
        outgoingCategoriesRecyclerView.setAdapter(outgoingCategoriesAdapter);
        final LinearLayoutManager OutcomingCategoriesLayoutManager = new LinearLayoutManager(this);
        outgoingCategoriesRecyclerView.setLayoutManager(OutcomingCategoriesLayoutManager);

        incomeCategoriesRecyclerView = findViewById(R.id.recyclerViewIncomeCategories);
        final newIncomeCategoriesAdapter incomeCategoriesAdapter = new newIncomeCategoriesAdapter();
        incomeCategoriesRecyclerView.setAdapter(incomeCategoriesAdapter);
        final LinearLayoutManager incomeCategorieslayoutManager = new LinearLayoutManager(this);
        incomeCategoriesRecyclerView.setLayoutManager(incomeCategorieslayoutManager);

        editTextInitialMoney = findViewById(R.id.editTextInitialMoney);
        imageButtonAddOutgoingCategory = findViewById(R.id.imageButtonAddOutgoingCategory);
        imageButtonDeleteOutgoingCategory = findViewById(R.id.imageButtonDeleteOutgoingCategory);
        imageButtonAddIncomeCategory = findViewById(R.id.imageButtonAddIncomeCategory);
        imageButtonDeleteIncomeCategory = findViewById(R.id.imageButtonDeleteIncomeCategory);
        buttonApplyConfiguration = findViewById(R.id.buttonApplyConfiguration);

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
                    appConfiguration newConfiguration = new appConfiguration(Double.valueOf(editTextInitialMoney.getText().toString()),
                            getOutgoingCategories(), getIncomeCategories());

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

    //TODO: merge this two methods
    private RealmList<outgoingCategory> getOutgoingCategories() {
        RealmList<outgoingCategory> result = new RealmList<>();
        newOutgoingCategoriesAdapter.ViewHolder viewHolder;
        for (int i = 0; i < outgoingCategoriesRecyclerView.getAdapter().getItemCount(); i++) {
            viewHolder = (newOutgoingCategoriesAdapter.ViewHolder) outgoingCategoriesRecyclerView.findViewHolderForAdapterPosition(i);
            result.add(new outgoingCategory(new RealmList<subcategory>(), Double.valueOf(viewHolder.max.getText().toString()), viewHolder.name.getText().toString()));
        }
        return result;
    }

    private RealmList<incomeCategory> getIncomeCategories() {
        RealmList<incomeCategory> result = new RealmList<>();
        newIncomeCategoriesAdapter.ViewHolder viewHolder;
        for (int i = 0; i < incomeCategoriesRecyclerView.getAdapter().getItemCount(); i++) {
            viewHolder = (newIncomeCategoriesAdapter.ViewHolder) incomeCategoriesRecyclerView.findViewHolderForAdapterPosition(i);
            result.add(new incomeCategory(viewHolder.name.getText().toString(), Double.valueOf(viewHolder.exactValue.getText().toString())));
        }
        return result;
    }

    private boolean checkData() {
        if (editTextInitialMoney.getText().toString().isEmpty()) {
            return false;
        } else {
            newOutgoingCategoriesAdapter.ViewHolder outgoingViewHolder;
            for (int i = 0; i < outgoingCategoriesRecyclerView.getAdapter().getItemCount(); i++) {
                outgoingViewHolder = (newOutgoingCategoriesAdapter.ViewHolder) outgoingCategoriesRecyclerView.findViewHolderForAdapterPosition(i);
                if (outgoingViewHolder.name.getText().toString().isEmpty()) {
                    outgoingViewHolder.name.setHintTextColor(getResources().getColor(R.color.colorWrong));
                    return false;
                } else if (outgoingViewHolder.max.getText().toString().isEmpty()) {
                    return false;
                }
            }
            newIncomeCategoriesAdapter.ViewHolder incomeViewHolder;
            for (int i = 0; i < incomeCategoriesRecyclerView.getAdapter().getItemCount(); i++) {
                incomeViewHolder = (newIncomeCategoriesAdapter.ViewHolder) incomeCategoriesRecyclerView.findViewHolderForAdapterPosition(i);
                if (incomeViewHolder.name.getText().toString().isEmpty()) {
                    incomeViewHolder.name.setHintTextColor(getResources().getColor(R.color.colorWrong));
                    return false;
                } else if (incomeViewHolder.exactValue.getText().toString().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}
