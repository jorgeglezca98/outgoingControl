package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.fragments.initialMoneyInitialConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.fragments.secondPageInitialConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.fragments.thirdPageInitialConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.others.customViewPager;

import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmList;

public class initialConfigurationActivity extends AppCompatActivity {

    private initialMoneyInitialConfiguration firstFragment;
    private secondPageInitialConfiguration secondFragment;
    private thirdPageInitialConfiguration thirdFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_configuration);

        final customViewPager configurationViewPager = findViewById(R.id.viewPagerInitialConfiguration);
        final TabLayout configurationTabLayout = findViewById(R.id.tabLayoutInitialConfiguration);
        configurationTabLayout.setupWithViewPager(configurationViewPager, true);

        final Vector<Fragment> fragments = new Vector<>();
        firstFragment = new initialMoneyInitialConfiguration();
        fragments.add(firstFragment);
        secondFragment = new secondPageInitialConfiguration();
        fragments.add(secondFragment);
        thirdFragment = new thirdPageInitialConfiguration();
        fragments.add(thirdFragment);

        configurationViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        configurationViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(configurationTabLayout));

        final FloatingActionButton backButton = findViewById(R.id.fabBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedTabPosition = configurationTabLayout.getSelectedTabPosition();
                if (selectedTabPosition > 0) {
                    TabLayout.Tab newSelectedTab = configurationTabLayout.getTabAt(selectedTabPosition - 1);
                    if (newSelectedTab != null) {
                        newSelectedTab.select();
                    }
                }
            }
        });
        final FloatingActionButton forwardButton = findViewById(R.id.fabForward);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedTabPosition = configurationTabLayout.getSelectedTabPosition();
                if (selectedTabPosition == (configurationTabLayout.getTabCount() - 1)) {
                    if (thirdFragment.checkData()) {
                        double currentMoney = firstFragment.getData();
                        appConfiguration newConfiguration = new appConfiguration(currentMoney);
                        RealmList<category> categories = secondFragment.getData();
                        categories.addAll(thirdFragment.getData());

                        Realm database = Realm.getDefaultInstance();
                        database.beginTransaction();
                        database.copyToRealm(newConfiguration);
                        database.copyToRealm(categories);
                        database.commitTransaction();

                        Intent intent = new Intent(initialConfigurationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(initialConfigurationActivity.this, "Complete all fields first", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    boolean dataChecked = selectedTabPosition == 0 ? firstFragment.checkData() : secondFragment.checkData();
                    if (dataChecked) {
                        TabLayout.Tab newSelectedTab = configurationTabLayout.getTabAt(selectedTabPosition + 1);
                        if (newSelectedTab != null) {
                            newSelectedTab.select();
                        }
                    } else {
                        Toast.makeText(initialConfigurationActivity.this, "Complete all fields first", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        configurationTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                configurationViewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    backButton.hide();
                }

                if (tab.getPosition() == configurationTabLayout.getTabCount() - 1) {
                    forwardButton.setImageResource(R.drawable.check);
                    forwardButton.animate().rotation(0).start();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == configurationTabLayout.getTabCount() - 1) {
                    forwardButton.setImageResource(R.drawable.up_pointing_arrow);
                    forwardButton.animate().rotation(90).start();
                }
                if (tab.getPosition() == 0) {
                    backButton.show();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
