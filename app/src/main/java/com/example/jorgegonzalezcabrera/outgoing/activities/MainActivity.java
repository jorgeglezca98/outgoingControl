package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.editableOutgoingCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.fragments.actionsFragment;
import com.example.jorgegonzalezcabrera.outgoing.fragments.mainFragment;
import com.example.jorgegonzalezcabrera.outgoing.fragments.settingFragment;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.incomeCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry.periodicType;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs.newEntryDialog;
import static com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs.newPeriodicEntryDialog;
import static com.example.jorgegonzalezcabrera.outgoing.utilities.utils.dpToPixels;

public class MainActivity extends AppCompatActivity implements localUtils.OnEntriesChangeInterface
        , localUtils.OnCategoriesChangeInterface
        , editableOutgoingCategoriesAdapter.editOutgoingCategoryInterface {

    private ViewPager viewPager;
    private FragmentStatePagerAdapter viewPagerAdapter;
    private actionsFragment actionsFragment;
    private mainFragment mainFragment;
    private Realm database;
    private localUtils.OnEntriesChangeInterface onEntriesChangeInterface;
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

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

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

        onEntriesChangeInterface = this;

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
                newEntryDialog(MainActivity.this, onEntriesChangeInterface);
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

        updateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshMenuItem:
                View menuItem = findViewById(R.id.refreshMenuItem);
                Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
                rotation.setRepeatCount(Animation.INFINITE);
                menuItem.startAnimation(rotation);
                updateData();
                menuItem.clearAnimation();
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
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
        fabMenu.animate().rotation(180);

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

    @Override
    public void addEntry(@NonNull final entry newEntry) {
        final appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();
        if (currentConfiguration != null) {
            database.beginTransaction();
            if (newEntry.getType() == entry.type.OUTGOING) {
                currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() - newEntry.getValor());
            } else {
                currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() + newEntry.getValor());
            }
            database.commitTransaction();
        }

        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                if (currentConfiguration != null) {
                    database.copyToRealmOrUpdate(currentConfiguration);
                }
                database.copyToRealm(newEntry);
            }
        });
        entry newEntryFromRealm = database.where(entry.class).equalTo("id", newEntry.getId()).findFirst();
        actionsFragment.updateDataAdded(newEntryFromRealm);
        mainFragment.updateDataAdded(newEntryFromRealm);
    }

    @Override
    public void removeEntry(@NonNull final entry removedEntry) {
        final appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();
        if (currentConfiguration != null) {
            if (removedEntry.getType() == entry.type.OUTGOING) {
                currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() + removedEntry.getValor());
            } else {
                currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() - removedEntry.getValor());
            }
        }

        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                if (currentConfiguration != null) {
                    database.copyToRealmOrUpdate(currentConfiguration);
                }
            }
        });

        mainFragment.updateDataRemoved(removedEntry);

        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                removedEntry.deleteFromRealm();
            }
        });
    }

    @Override
    public void editEntry(@NonNull final entry nextVersion) {
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                entry currentVersion = database.where(entry.class).equalTo("id", nextVersion.getId()).findFirst();

                final appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();
                if (currentConfiguration != null) {
                    if (currentVersion.getType() == entry.type.OUTGOING) {
                        currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() + currentVersion.getValor());
                    } else {
                        currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() - currentVersion.getValor());
                    }

                    if (nextVersion.getType() == entry.type.OUTGOING) {
                        currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() - nextVersion.getValor());
                    } else {
                        currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() + nextVersion.getValor());
                    }
                }

                actionsFragment.updateDataModified(nextVersion);
                mainFragment.updateDataModified(currentVersion, nextVersion);

                if (currentConfiguration != null) {
                    database.copyToRealmOrUpdate(currentConfiguration);
                }
                database.copyToRealmOrUpdate(nextVersion);
            }
        });
    }

    @Override
    public void removeAndReplaceCategory(@NonNull final outgoingCategory removedOutgoingCategory, @NonNull final String newCategory) {
        mainFragment.updateCategoryRemoved(removedOutgoingCategory);
        actionsFragment.removeCategoryInFilters(removedOutgoingCategory);
        RealmList<entry> entries = new RealmList<>();
        for (int i = 0; i < removedOutgoingCategory.getSubcategories().size(); i++) {
            entries.addAll(database.where(entry.class).equalTo("category", removedOutgoingCategory.getSubcategories().get(i).getName()).findAll());
        }
        for (int i = 0; i < entries.size(); i++) {
            entry entry = entries.get(i);
            entry nextVersion = new entry(entry.getValor(), entry.getType(), newCategory, entry.getDescription(), entry.getCreationDate());
            nextVersion.setId(entry.getId());
            onEntriesChangeInterface.editEntry(nextVersion);
        }
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                removedOutgoingCategory.deleteFromRealm();
            }
        });
    }

    @Override
    public void removeAndKeepCategory(@NonNull final outgoingCategory removedOutgoingCategory) {
        mainFragment.updateCategoryRemoved(removedOutgoingCategory);
        final appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                currentConfiguration.getOutgoingCategories().remove(removedOutgoingCategory);
                currentConfiguration.getRemovedOutgoingCategories().add(removedOutgoingCategory);
                database.copyToRealmOrUpdate(currentConfiguration);
            }
        });
    }

    @Override
    public void removeAndReplaceCategory(@NonNull final incomeCategory removedIncomeCategory, @NonNull String newCategory) {
        actionsFragment.removeCategoryInFilters(removedIncomeCategory);
        RealmList<entry> entries = new RealmList<>();
        entries.addAll(database.where(entry.class).equalTo("category", removedIncomeCategory.getName()).findAll());
        for (int i = 0; i < entries.size(); i++) {
            entry entry = entries.get(i);
            entry nextVersion = new entry(entry.getValor(), entry.getType(), newCategory, entry.getDescription(), entry.getCreationDate());
            nextVersion.setId(entry.getId());
            onEntriesChangeInterface.editEntry(nextVersion);
        }
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                removedIncomeCategory.deleteFromRealm();
            }
        });
    }

    @Override
    public void removeAndKeepCategory(@NonNull final incomeCategory removedIncomeCategory) {
        final appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                currentConfiguration.getIncomeCategories().remove(removedIncomeCategory);
                currentConfiguration.getRemovedIncomeCategories().add(removedIncomeCategory);
                database.copyToRealmOrUpdate(currentConfiguration);
            }
        });
    }

    @Override
    public void addedCategory(@NonNull final outgoingCategory newOutgoingCategory) {
        final appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                currentConfiguration.getOutgoingCategories().add(newOutgoingCategory);
                database.copyToRealmOrUpdate(currentConfiguration);
            }
        });
        outgoingCategory storedOutgoingCategory = database.where(outgoingCategory.class).equalTo("id", newOutgoingCategory.getId()).findFirst();
        mainFragment.updateCategoryAdded(storedOutgoingCategory);
        actionsFragment.addCategoryInFilters(storedOutgoingCategory);
    }

    @Override
    public void addedCategory(@NonNull final incomeCategory newIncomeCategory) {
        final appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                currentConfiguration.getIncomeCategories().add(newIncomeCategory);
                database.copyToRealmOrUpdate(currentConfiguration);
            }
        });
        incomeCategory storedOutgoingCategory = database.where(incomeCategory.class).equalTo("id", newIncomeCategory.getId()).findFirst();
        actionsFragment.addCategoryInFilters(storedOutgoingCategory);
    }

    public final static String CATEGORY_NAME_KEY = "categoryName";
    public final static String CATEGORY_MAXIMUM_KEY = "categoryMax";
    public final static String CATEGORY_SUBCATEGORIES_KEY = "categorySubcategories";
    public final static String CONTAINER_TRANSITION_NAME_KEY = "containerTransitionName";
    public final static String CATEGORY_NAME_TRANSITION_NAME_KEY = "categoryNameTransitionName";
    public final static String CATEGORY_MAXIMUM_TRANSITION_NAME_KEY = "categoryMaximumTransitionName";

    public final static int REQUEST_EDIT = 1;
    public final static int REQUEST_ADD = 2;

    @Override
    public void edit(outgoingCategory outgoingCategory, ConstraintLayout container, EditText categoryName, EditText categoryMaximum) {
        Intent intent = new Intent(this, editOutgoingCategoryActivity.class);

        List<String> subcategories = new ArrayList<>();
        for (int i = 0; i < outgoingCategory.getSubcategories().size(); i++) {
            subcategories.add(outgoingCategory.getSubcategories().get(i).getName());
        }

        intent.putExtra(CONTAINER_TRANSITION_NAME_KEY, container.getTransitionName());
        intent.putExtra(CATEGORY_NAME_TRANSITION_NAME_KEY, categoryName.getTransitionName());
        intent.putExtra(CATEGORY_MAXIMUM_TRANSITION_NAME_KEY, categoryMaximum.getTransitionName());
        intent.putExtra(CATEGORY_NAME_KEY, outgoingCategory.getName());
        intent.putExtra(CATEGORY_MAXIMUM_KEY, outgoingCategory.getMaximum());
        intent.putStringArrayListExtra(CATEGORY_SUBCATEGORIES_KEY, (ArrayList<String>) subcategories);

        Pair<View, String> p1 = Pair.create((View) categoryName, categoryName.getTransitionName());
        Pair<View, String> p2 = Pair.create((View) categoryMaximum, categoryMaximum.getTransitionName());
        Pair<View, String> p3 = Pair.create((View) container, container.getTransitionName());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2, p3);

        startActivityForResult(intent, REQUEST_EDIT, options.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_EDIT) {
            if (resultCode == RESULT_OK) {

            }
        } else if (requestCode == REQUEST_ADD) {
            if (resultCode == RESULT_OK) {

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void updateData() {
        mainFragment.updateData();

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
                        periodicEntry.createEntry(periodicEntriesItem, onEntriesChangeInterface, lastChange);
                        lastChange.add(Calendar.YEAR, 1);
                    }
                } else if (periodicEntriesItem.getFrequency() == periodicType.MONTHLY) {
                    lastChange.add(Calendar.MONTH, 1);
                    while (lastChange.before(currentDate)) {
                        periodicEntry.createEntry(periodicEntriesItem, onEntriesChangeInterface, lastChange);
                        lastChange.add(Calendar.MONTH, 1);
                    }
                } else if (periodicEntriesItem.getFrequency() == periodicType.WEEKLY) {
                    lastChange.add(Calendar.DATE, 7);
                    while (lastChange.before(currentDate)) {
                        periodicEntry.createEntry(periodicEntriesItem, onEntriesChangeInterface, lastChange);
                        lastChange.add(Calendar.DATE, 7);
                    }
                }
            }
        }
    }

}