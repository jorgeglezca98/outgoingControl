package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs;
import com.example.jorgegonzalezcabrera.outgoing.fragments.actionsFragment;
import com.example.jorgegonzalezcabrera.outgoing.fragments.mainFragment;
import com.example.jorgegonzalezcabrera.outgoing.fragments.settingFragment;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry.periodicType;
import com.example.jorgegonzalezcabrera.outgoing.others.customizedTimerTask;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs.newEntryDialog;
import static com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs.newPeriodicEntryDialog;
import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getTypeFromOrdinal;
import static com.example.jorgegonzalezcabrera.outgoing.utilities.utils.dpToPixels;

public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;
    private FragmentStatePagerAdapter viewPagerAdapter;
    private actionsFragment actionsFragment;
    private mainFragment mainFragment;
    private Realm database;
    private OnNewEntryAddedInterface onNewEntryAddedInterface;
    private boolean floatingMenuOpen;
    private FloatingActionButton fabMenu;
    private FloatingActionButton fabAddEntry;
    private FloatingActionButton fabAddPeriodicEntry;
    private FloatingActionButton fabFilterActions;
    private TextView labelFilterActions;
    private TextView labelAddEntry;
    private TextView labelAddPeriodicEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Realm.getDefaultInstance();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.list));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.setting));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.viewPager);
        final Vector<Fragment> fragments = new Vector<>();
        mainFragment = new mainFragment();
        fragments.add(mainFragment);
        actionsFragment = new actionsFragment();
        fragments.add(actionsFragment);
        settingFragment settingFragment = new settingFragment();
        fragments.add(settingFragment);
        viewPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (fragments.get(tab.getPosition()) == actionsFragment && floatingMenuOpen) {
                    fabFilterActions.animate().translationY(dpToPixels(MainActivity.this, -146.0f));
                    labelFilterActions.animate().translationY(dpToPixels(MainActivity.this, -146.0f));
                    labelFilterActions.animate().alpha(1.0f).setDuration(450);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (fragments.get(tab.getPosition()) == actionsFragment && floatingMenuOpen) {
                    fabFilterActions.animate().translationY(dpToPixels(MainActivity.this, 0.0f));
                    labelFilterActions.animate().translationY(dpToPixels(MainActivity.this, 0.0f));
                    labelFilterActions.animate().alpha(0.0f).setDuration(300);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        onNewEntryAddedInterface = new OnNewEntryAddedInterface() {
            @Override
            public void OnNewEntryAdded(entry newEntry) {
                actionsFragment.updateData(newEntry);
                mainFragment.updateData(newEntry);
            }
        };

        floatingMenuOpen = false;
        fabMenu = findViewById(R.id.fab);
        fabAddEntry = findViewById(R.id.fabAddEntry);
        fabAddPeriodicEntry = findViewById(R.id.fabAddPeriodicEntry);
        labelAddEntry = findViewById(R.id.labelAddEntry);
        labelAddPeriodicEntry = findViewById(R.id.labelAddPeriodicEntry);
        fabFilterActions = findViewById(R.id.fabFilterActions);
        labelFilterActions = findViewById(R.id.labelFilterActions);

        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (floatingMenuOpen) {
                    closeFloatingMenu();
                } else {
                    openFloatingMenu();
                }
            }
        });

        fabAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final appConfiguration updatedConfiguration = database.where(appConfiguration.class).findFirst();
                newEntryDialog(MainActivity.this, new dialogs.OnNewEntryAccepted() {
                    @Override
                    public void OnClick(final String subcategory, final int type, final double value, final String description) {
                        final entry newEntry = new entry(value, getTypeFromOrdinal(type), subcategory, description);
                        if (updatedConfiguration != null) {
                            if (getTypeFromOrdinal(type) == entry.type.OUTGOING) {
                                updatedConfiguration.setCurrentMoney(updatedConfiguration.getCurrentMoney() - newEntry.getValor());
                            } else {
                                updatedConfiguration.setCurrentMoney(updatedConfiguration.getCurrentMoney() + newEntry.getValor());
                            }
                        }
                        database.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(@NonNull Realm realm) {
                                if (updatedConfiguration != null) {
                                    database.copyToRealmOrUpdate(updatedConfiguration);
                                }
                                database.copyToRealm(newEntry);
                            }
                        });

                        onNewEntryAddedInterface.OnNewEntryAdded(newEntry);
                    }
                });

                closeFloatingMenu();
            }
        });

        fabAddPeriodicEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPeriodicEntryDialog(MainActivity.this);
                closeFloatingMenu();
            }
        });

        fabFilterActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionsFragment.expandFilters();
                closeFloatingMenu();
            }
        });

        setTimers();
    }

    private void closeFloatingMenu() {
        fabMenu.animate().rotation(0);

        fabAddEntry.animate().translationY(dpToPixels(MainActivity.this, 0.0f));
        labelAddEntry.animate().translationY(dpToPixels(MainActivity.this, 0.0f));
        labelAddEntry.animate().alpha(0.0f).setDuration(300);

        fabAddPeriodicEntry.animate().translationY(dpToPixels(MainActivity.this, 0.0f));
        labelAddPeriodicEntry.animate().translationY(dpToPixels(MainActivity.this, 0.0f));
        labelAddPeriodicEntry.animate().alpha(0.0f).setDuration(300);

        fabFilterActions.animate().translationY(dpToPixels(MainActivity.this, 0.0f));
        labelFilterActions.animate().translationY(dpToPixels(MainActivity.this, 0.0f));
        labelFilterActions.animate().alpha(0.0f).setDuration(300);
        floatingMenuOpen = !floatingMenuOpen;
    }

    private void openFloatingMenu() {
        fabMenu.animate().rotation(45);

        fabAddEntry.animate().translationY(dpToPixels(MainActivity.this, -56.0f));
        labelAddEntry.animate().translationY(dpToPixels(MainActivity.this, -56.0f));
        labelAddEntry.animate().alpha(1.0f).setDuration(150);

        fabAddPeriodicEntry.animate().translationY(dpToPixels(MainActivity.this, -101.0f));
        labelAddPeriodicEntry.animate().translationY(dpToPixels(MainActivity.this, -101.0f));
        labelAddPeriodicEntry.animate().alpha(1.0f).setDuration(300);

        if (actionsFragment == viewPagerAdapter.getItem(viewPager.getCurrentItem())) {
            fabFilterActions.animate().translationY(dpToPixels(MainActivity.this, -146.0f));
            labelFilterActions.animate().translationY(dpToPixels(MainActivity.this, -146.0f));
            labelFilterActions.animate().alpha(1.0f).setDuration(450);
        }
        floatingMenuOpen = !floatingMenuOpen;
    }

    public interface OnNewEntryAddedInterface {
        void OnNewEntryAdded(entry newEntry);
    }

    public void setTimers() {
        Timer timer = new Timer(true);

        RealmResults<periodicEntry> periodicEntries = database.where(periodicEntry.class).findAll();

        GregorianCalendar lastChange = new GregorianCalendar();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());
        for (int i = 0; i < periodicEntries.size(); i++) {
            periodicEntry periodicEntriesItem = periodicEntries.get(i);
            if (periodicEntriesItem != null) {
                lastChange.setTime(periodicEntriesItem.getLastChange());

                if (periodicEntriesItem.getFrequency() == periodicType.ANNUAL) {
                    lastChange.add(Calendar.YEAR, 1);
                    while (lastChange.before(currentDate)) {
                        periodicEntry.createEntry(periodicEntriesItem, onNewEntryAddedInterface, currentDate);
                        lastChange.add(Calendar.YEAR, 1);
                    }
                } else if (periodicEntriesItem.getFrequency() == periodicType.MONTHLY) {
                    lastChange.add(Calendar.MONTH, 1);
                    while (lastChange.before(currentDate)) {
                        periodicEntry.createEntry(periodicEntriesItem, onNewEntryAddedInterface, currentDate);
                        lastChange.add(Calendar.MONTH, 1);
                    }
                } else if (periodicEntriesItem.getFrequency() == periodicType.WEEKLY) {
                    lastChange.add(Calendar.DATE, 7);
                    while (lastChange.before(currentDate)) {
                        periodicEntry.createEntry(periodicEntriesItem, onNewEntryAddedInterface, currentDate);
                        lastChange.add(Calendar.DATE, 7);
                    }
                }
            }
        }

        currentDate.add(Calendar.DATE, 1);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        timer.schedule(new customizedTimerTask(onNewEntryAddedInterface), currentDate.getTime(), 86400000);
    }

}