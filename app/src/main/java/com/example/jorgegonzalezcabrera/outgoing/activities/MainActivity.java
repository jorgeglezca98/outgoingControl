package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.surplusMoneyTableAdapter;
import com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;

import java.util.Vector;

import io.realm.Realm;

import static com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs.newEntryDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    appConfiguration currentConfiguration;
    Realm database;
    TextView textViewCurrentMoney;
    TextView textViewOutgoingsOfTheMonth;
    TextView textViewIncomesOfTheMonth;
    RecyclerView recyclerViewSurplusMoney;
    double totalOutgoings;
    double totalIncomes;
    Vector<surplusMoneyTableAdapter.surplusMoneyByCategory> surplusMoneyByCategoryVector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        bindUI();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newEntryDialog(MainActivity.this, currentConfiguration.getOutgoingCategoriesCategories(),
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
                                    }
                                });
                            }
                        });
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void bindUI(){
        database = Realm.getDefaultInstance();
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                currentConfiguration = database.where(appConfiguration.class).findFirst();
            }
        });

        textViewCurrentMoney = findViewById(R.id.textViewCurrentMoney);
        textViewCurrentMoney.setText(String.format("%.2f", currentConfiguration.getCurrentMoney()));

        surplusMoneyByCategoryVector = new Vector<>();
        totalOutgoings = 0;
        double aux;
        textViewOutgoingsOfTheMonth = findViewById(R.id.textViewOutgoingsOfTheMonth);
        for (int i = 0; i < currentConfiguration.getOutgoingCategoriesCategories().size(); i++) {
            aux = 0;
            for (int j = 0; j < currentConfiguration.getOutgoingCategoriesCategories().get(i).getSubcategories().size(); j++) {
                aux += database.where(entry.class).equalTo("category", currentConfiguration.getOutgoingCategoriesCategories().get(i).getSubcategories().get(j).getName()).sum("valor").doubleValue();
            }
            totalOutgoings += aux;
            surplusMoneyByCategoryVector.add(new surplusMoneyTableAdapter.surplusMoneyByCategory(currentConfiguration.getOutgoingCategoriesCategories().get(i), currentConfiguration.getOutgoingCategoriesCategories().get(i).getMaximum() - aux));

        }
        textViewOutgoingsOfTheMonth.setText(String.format("%.2f", totalOutgoings));

        totalIncomes = 0;
        textViewIncomesOfTheMonth = findViewById(R.id.textViewIncomeOfTheMonth);
        for (int i = 0; i < currentConfiguration.getIncomeCategories().size(); i++) {
            totalIncomes += database.where(entry.class).equalTo("category", currentConfiguration.getIncomeCategories().get(i).getName()).sum("valor").doubleValue();
        }
        textViewIncomesOfTheMonth.setText(String.format("%.2f", totalIncomes));

        recyclerViewSurplusMoney = findViewById(R.id.recyclerViewSurplusMoney);
        recyclerViewSurplusMoney.setAdapter(new surplusMoneyTableAdapter(surplusMoneyByCategoryVector));
        recyclerViewSurplusMoney.setLayoutManager(new LinearLayoutManager(this));
    }

    public void updateAfterOutgoing(double value, String subcategory){
        currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() - value);
        textViewCurrentMoney.setText(String.format("%.2f", currentConfiguration.getCurrentMoney()));
        totalOutgoings += value;
        textViewOutgoingsOfTheMonth.setText(String.format("%.2f", totalOutgoings));
        ((surplusMoneyTableAdapter) recyclerViewSurplusMoney.getAdapter()).updateData(subcategory, value);
    }

    public void updateAfterIncome(double value){
        currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() + value);
        textViewCurrentMoney.setText(String.format("%.2f", currentConfiguration.getCurrentMoney()));
        totalIncomes += value;
        textViewIncomesOfTheMonth.setText(String.format("%.2f", totalIncomes));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

//TODO: make sure that the data is updated in the database
//TODO: permitir que se accedan valores regulares
//TODO: the models should have a primarykey to make updates