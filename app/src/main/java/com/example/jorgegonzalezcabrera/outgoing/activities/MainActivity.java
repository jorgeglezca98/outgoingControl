package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.mainPagerAdapter;
import com.example.jorgegonzalezcabrera.outgoing.fragments.actionsFragment;
import com.example.jorgegonzalezcabrera.outgoing.fragments.mainFragment;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;

import java.util.Vector;

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener, mainFragment.OnNewEntryAddedInterface {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Vector<Fragment> fragments;
    private actionsFragment actionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Main"));
        tabLayout.addTab(tabLayout.newTab().setText("Actions"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.viewPager);
        fragments = new Vector<>();
        fragments.add(new mainFragment());
        actionsFragment = new actionsFragment();
        fragments.add(actionsFragment);
        viewPager.setAdapter(new mainPagerAdapter(getSupportFragmentManager(),fragments));
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

    @Override
    public void OnNewEntryAdded(entry newEntry) {
        actionsFragment.updateData(newEntry);
    }
}

//TODO: make sure that the data is updated in the database
//TODO: permitir que se accedan valores regulares
//TODO: the models should have a primarykey to make updates