package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.fragments.actionsFragment;
import com.example.jorgegonzalezcabrera.outgoing.fragments.mainFragment;
import com.example.jorgegonzalezcabrera.outgoing.fragments.settingFragment;
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

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener, mainFragment.OnNewEntryAddedInterface {

    private ViewPager viewPager;
    private actionsFragment actionsFragment;
    private mainFragment mainFragment;
    private Realm database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        database = Realm.getDefaultInstance();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Main"));
        tabLayout.addTab(tabLayout.newTab().setText("Actions"));
        tabLayout.addTab(tabLayout.newTab().setText("Settings"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.viewPager);
        final Vector<Fragment> fragments = new Vector<>();
        mainFragment = new mainFragment();
        fragments.add(mainFragment);
        actionsFragment = new actionsFragment();
        fragments.add(actionsFragment);
        com.example.jorgegonzalezcabrera.outgoing.fragments.settingFragment settingFragment = new settingFragment();
        fragments.add(settingFragment);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTimers();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

    @Override
    public void OnNewEntryAdded(entry newEntry) {
        actionsFragment.updateData(newEntry);
        mainFragment.updateData(newEntry);
    }


    public void setTimers() {
        Timer timer = new Timer(true);

        RealmResults<periodicEntry> periodicEntries = database.where(periodicEntry.class).findAll();

        GregorianCalendar lastChange = new GregorianCalendar();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());
        for (int i = 0; i < periodicEntries.size(); i++) {
            periodicEntry periodicEntry = periodicEntries.get(i);
            if (periodicEntry != null) {
                lastChange.setTime(periodicEntry.getLastChange());

                if (periodicEntry.getFrequency() == periodicType.ANNUAL) {
                    lastChange.add(Calendar.YEAR, 1);
                    while (lastChange.before(currentDate)) {
                        customizedTimerTask.createEntry(periodicEntry, this, currentDate);
                        lastChange.add(Calendar.YEAR, 1);
                    }
                } else if (periodicEntry.getFrequency() == periodicType.MONTHLY) {
                    lastChange.add(Calendar.MONTH, 1);
                    while (lastChange.before(currentDate)) {
                        customizedTimerTask.createEntry(periodicEntry, this, currentDate);
                        lastChange.add(Calendar.MONTH, 1);
                    }
                } else if (periodicEntry.getFrequency() == periodicType.WEEKLY) {
                    lastChange.add(Calendar.DAY_OF_YEAR, 7);
                    while (lastChange.before(currentDate)) {
                        customizedTimerTask.createEntry(periodicEntry, this, currentDate);
                        lastChange.add(Calendar.DAY_OF_YEAR, 7);
                    }
                }
            }
        }

        currentDate.add(Calendar.DAY_OF_YEAR, 1);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        timer.schedule(new customizedTimerTask(this), currentDate.getTime(), 86400000);
    }

}

//TODO: manage the periodic entry have just introduced