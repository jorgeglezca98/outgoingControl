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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.activities.editFieldActivity.editIncomeCategoryInterface;
import com.example.jorgegonzalezcabrera.outgoing.adapters.editableOutgoingCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs;
import com.example.jorgegonzalezcabrera.outgoing.fragments.actionsFragment;
import com.example.jorgegonzalezcabrera.outgoing.fragments.chartsFragment;
import com.example.jorgegonzalezcabrera.outgoing.fragments.mainFragment;
import com.example.jorgegonzalezcabrera.outgoing.fragments.settingFragment;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.moneyController;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs.newEntryDialog;
import static com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry.periodicType.ANNUAL;
import static com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry.periodicType.DAILY;
import static com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry.periodicType.MONTHLY;
import static com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry.periodicType.WEEKLY;

public class MainActivity extends AppCompatActivity implements localUtils.OnEntriesChangeInterface
        , localUtils.OnCategoriesChangeInterface
        , editableOutgoingCategoriesAdapter.editOutgoingCategoryInterface
        , editIncomeCategoryInterface
        , localUtils.changePeriodicEntriesInterface {

    private ViewPager viewPager;
    private actionsFragment actionsFragment;
    private mainFragment mainFragment;
    private settingFragment settingFragment;
    private chartsFragment chartsFragment;
    private Realm database;
    private localUtils.OnEntriesChangeInterface onEntriesChangeInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        database = Realm.getDefaultInstance();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.primary1));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.list));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.chart));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.setting));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.viewPager);
        final Vector<Fragment> fragments = new Vector<>();
        mainFragment = new mainFragment();
        fragments.add(mainFragment);
        actionsFragment = new actionsFragment();
        fragments.add(actionsFragment);
        chartsFragment = new chartsFragment();
        fragments.add(chartsFragment);
        settingFragment = new settingFragment();
        fragments.add(settingFragment);
        FragmentStatePagerAdapter viewPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
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
        viewPager.setOffscreenPageLimit(4);

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

        onEntriesChangeInterface = this;

        FloatingActionButton fabAddEntry = findViewById(R.id.fab);

        fabAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newEntryDialog(MainActivity.this, onEntriesChangeInterface);
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
                Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
                rotation.setRepeatCount(Animation.INFINITE);
                updateData();
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.filters:
                dialogs.filtersDialog(MainActivity.this,
                        actionsFragment.getFiltersManipulation(),
                        actionsFragment.getCategories(),
                        actionsFragment.getMinDate(),
                        actionsFragment.getMaxDate(),
                        actionsFragment.getMinValue(),
                        actionsFragment.getMaxValue(),
                        actionsFragment.getDescription());
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
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
                database.beginTransaction();
                currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() + removedEntry.getValor());
                database.commitTransaction();
            } else {
                database.beginTransaction();
                currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() - removedEntry.getValor());
                database.commitTransaction();
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
    public void removeAndReplaceCategory(@NonNull final category removedCategory, @NonNull String newSubcategory) {
        actionsFragment.removeCategoryInFilters(removedCategory.getName());
        RealmList<entry> entries = new RealmList<>();
        entries.addAll(database.where(entry.class).equalTo("category", removedCategory.getName()).findAll());
        for (int i = 0; i < entries.size(); i++) {
            entry entry = entries.get(i);
            entry nextVersion = new entry(entry.getValor(), entry.getType(), newSubcategory, entry.getDescription(), entry.getCreationDate());
            nextVersion.setId(entry.getId());
            onEntriesChangeInterface.editEntry(nextVersion);
        }
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                removedCategory.deleteFromRealm();
            }
        });
    }

    @Override
    public void removeAndKeepCategory(@NonNull final category removedCategory) {
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                removedCategory.setOperative(false);
                database.copyToRealmOrUpdate(removedCategory);
            }
        });
    }

    @Override
    public void removeMoneyController(@NonNull final moneyController removedCategory) {
        mainFragment.updateCategoryRemoved(removedCategory);
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                removedCategory.deleteFromRealm();
            }
        });
    }

    @Override
    public void remove(final periodicEntry periodicEntry) {
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                periodicEntry.deleteFromRealm();
            }
        });
    }

    public static final String CONTROLLER_ID_KEY = "controllerID";
    public final static String CATEGORY_NAME_KEY = "categoryName";
    public final static String CATEGORY_MAXIMUM_KEY = "categoryMax";
    public final static String CATEGORY_SUBCATEGORIES_KEY = "categorySubcategories";
    public final static String CONTAINER_TRANSITION_NAME_KEY = "containerTransitionName";
    public final static String CATEGORY_NAME_TRANSITION_NAME_KEY = "categoryNameTransitionName";
    public final static String CATEGORY_MAXIMUM_TRANSITION_NAME_KEY = "categoryMaximumTransitionName";

    public final static int REQUEST_ADD_OUTGOING_CATEGORY = 1;
    public static final int REQUEST_ADD_INCOME_CATEGORY = 2;
    public static final int REQUEST_ADD_MONEY_CONTROLLER = 3;
    public static final int REQUEST_EDIT_CATEGORY = 4;
    public static final int REQUEST_EDIT_MONEY_CONTROLLER = 5;
    public static final int REQUEST_EDIT_PERIODIC_ENTRY = 6;
    public static final int REQUEST_NEW_PERIODIC_ENTRY = 7;

    public static final String FIELD_TRANSITION_NAME_KEY = "fieldTransitionName";
    public static final String INITIAL_VALUE_KEY = "initialValue";
    public static final String FINAL_VALUE_KEY = "finalValue";
    public static final String HINT_KEY = "hint";
    public static final String ID_KEY = "id";
    public static final String REQUEST_CODE_KEY = "requestCode";


    @Override
    public void editMoneyController(moneyController moneyController, ConstraintLayout container, int requestCode) {
        Intent intent = new Intent(this, editOutgoingCategoryActivity.class);

        List<String> subcategories = new ArrayList<>();
        for (int i = 0; i < moneyController.getSubcategories().size(); i++) {
            subcategories.add(moneyController.getSubcategories().get(i).getName());
        }

        intent.putExtra(CONTROLLER_ID_KEY, moneyController.getId());
        intent.putExtra(CONTAINER_TRANSITION_NAME_KEY, container.getTransitionName());
        intent.putExtra(CATEGORY_NAME_KEY, moneyController.getName());
        intent.putExtra(CATEGORY_MAXIMUM_KEY, moneyController.getMaximum());
        intent.putStringArrayListExtra(CATEGORY_SUBCATEGORIES_KEY, (ArrayList<String>) subcategories);
        intent.putExtra(REQUEST_CODE_KEY, requestCode);

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, container, container.getTransitionName());

        startActivityForResult(intent, requestCode, options.toBundle());
    }

    @Override
    public void editCategoryField(String initialValue, ConstraintLayout container, String hint, int requestCode, long id) {
        Intent intent = new Intent(this, editFieldActivity.class);

        intent.putExtra(CONTAINER_TRANSITION_NAME_KEY, container.getTransitionName());
        intent.putExtra(INITIAL_VALUE_KEY, initialValue);
        intent.putExtra(HINT_KEY, hint);
        intent.putExtra(ID_KEY, id);
        intent.putExtra(REQUEST_CODE_KEY, requestCode);

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, container, container.getTransitionName());

        startActivityForResult(intent, requestCode, options.toBundle());
    }

    public static final String PERIODIC_ENTRY_VALUE_KEY = "periodicEntryValue";
    public static final String PERIODIC_ENTRY_CATEGORY_KEY = "periodicEntryCategory";
    public static final String PERIODIC_ENTRY_DESCRIPTION_KEY = "periodicEntryDescription";
    public static final String PERIODIC_ENTRY_QUANTITY_KEY = "periodicEntryQuantity";
    public static final String PERIODIC_ENTRY_FREQUENCY_KEY = "periodicEntryFrequency";
    public static final String PERIODIC_ENTRY_REPETITIONS_KEY = "periodicEntryRepetitions";
    public static final String PERIODIC_ENTRY_START_KEY = "periodicEntryStart";
    public static final String PERIODIC_ENTRY_END_KEY = "periodicEntryEnd";
    public static final String PERIODIC_ENTRY_ASK_KEY = "periodicEntryAsk";

    @Override
    public void edit(periodicEntry periodicEntry, ConstraintLayout container, int requestCode) {
        Intent intent = new Intent(this, editPeriodicEntryActivity.class);

        if (requestCode == REQUEST_EDIT_PERIODIC_ENTRY) {
            intent.putExtra(ID_KEY, periodicEntry.getId());
            intent.putExtra(PERIODIC_ENTRY_VALUE_KEY, periodicEntry.getValue());
            intent.putExtra(PERIODIC_ENTRY_CATEGORY_KEY, periodicEntry.getCategoryId());
            intent.putExtra(PERIODIC_ENTRY_DESCRIPTION_KEY, periodicEntry.getDescription());
            intent.putExtra(PERIODIC_ENTRY_QUANTITY_KEY, periodicEntry.getQuantityOf());
            intent.putExtra(PERIODIC_ENTRY_FREQUENCY_KEY, periodicEntry.getFrequency().ordinal());
            intent.putIntegerArrayListExtra(PERIODIC_ENTRY_REPETITIONS_KEY, new ArrayList<>(periodicEntry.getDaysOfRepetition()));
            intent.putExtra(PERIODIC_ENTRY_START_KEY, periodicEntry.getStartDate());
            intent.putExtra(PERIODIC_ENTRY_END_KEY, periodicEntry.getEndDate());
            intent.putExtra(PERIODIC_ENTRY_ASK_KEY, periodicEntry.isAskBefore());
            intent.putExtra(CONTAINER_TRANSITION_NAME_KEY, container.getTransitionName());
        } else {
            intent.putExtra(ID_KEY, periodicEntry.getId());
            intent.putExtra(PERIODIC_ENTRY_VALUE_KEY, 0);
            intent.putExtra(PERIODIC_ENTRY_CATEGORY_KEY, -1);
            intent.putExtra(PERIODIC_ENTRY_DESCRIPTION_KEY, "");
            intent.putExtra(PERIODIC_ENTRY_QUANTITY_KEY, 1);
            intent.putExtra(PERIODIC_ENTRY_FREQUENCY_KEY, DAILY.ordinal());
            intent.putIntegerArrayListExtra(PERIODIC_ENTRY_REPETITIONS_KEY, new ArrayList<Integer>());
            intent.putExtra(PERIODIC_ENTRY_START_KEY, new Date());
            intent.putExtra(PERIODIC_ENTRY_ASK_KEY, false);
            intent.putExtra(CONTAINER_TRANSITION_NAME_KEY, container.getTransitionName());
        }

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, container, container.getTransitionName());

        startActivityForResult(intent, requestCode, options.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        if (requestCode == REQUEST_EDIT_CATEGORY) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                final String newName = extras.getString(FINAL_VALUE_KEY);
                long id = extras.getLong(ID_KEY);

                final category modifiedOutgoingCategory = database.where(category.class).equalTo("id", id).findFirst();
                final String oldName = modifiedOutgoingCategory.getName();

                if (!newName.equals(modifiedOutgoingCategory.getName())) {
                    database.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            modifiedOutgoingCategory.setName(newName);
                            database.copyToRealmOrUpdate(modifiedOutgoingCategory);
                        }
                    });

                    actionsFragment.editCategoryInFilters(newName, oldName);
                    settingFragment.modifyCategory(modifiedOutgoingCategory);

                    RealmList<entry> entries = new RealmList<>();
                    entries.addAll(database.where(entry.class).equalTo("category", oldName).findAll());
                    for (int i = 0; i < entries.size(); i++) {
                        entry entry = entries.get(i);
                        entry nextVersion = new entry(entry.getValor(), entry.getType(), newName, entry.getDescription(), entry.getCreationDate());
                        nextVersion.setId(entry.getId());
                        onEntriesChangeInterface.editEntry(nextVersion);
                    }
                    mainFragment.updateData();
                }
            }
        } else if (requestCode == REQUEST_ADD_OUTGOING_CATEGORY) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String name = extras.getString(FINAL_VALUE_KEY);
                final category newCategory = new category(name, category.OUTGOING);

                database.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        database.copyToRealm(newCategory);
                    }
                });

                category storedCategory = database.where(category.class).equalTo("id", newCategory.getId()).findFirst();
                actionsFragment.addCategoryInFilters(storedCategory.getName());
                settingFragment.confirmAddedCategory(storedCategory);
            } else {
                settingFragment.newOutgoingCategoryCanceled();
            }
        } else if (requestCode == REQUEST_ADD_INCOME_CATEGORY) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String name = extras.getString(FINAL_VALUE_KEY);
                final category newCategory = new category(name, category.INCOME);

                database.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        database.copyToRealm(newCategory);
                    }
                });

                category storedCategory = database.where(category.class).equalTo("id", newCategory.getId()).findFirst();
                actionsFragment.addCategoryInFilters(storedCategory.getName());
                settingFragment.confirmAddedCategory(storedCategory);
            } else {
                settingFragment.newIncomeCategoryCanceled();
            }
        } else if (requestCode == REQUEST_ADD_MONEY_CONTROLLER) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String name = extras.getString(CATEGORY_NAME_KEY);
                Double max = extras.getDouble(CATEGORY_MAXIMUM_KEY);
                ArrayList<String> subcategories = extras.getStringArrayList(CATEGORY_SUBCATEGORIES_KEY);
                RealmList<category> formattedSubcategories = new RealmList<>();
                for (int i = 0; i < subcategories.size(); i++) {
                    formattedSubcategories.add(database.where(category.class).contains("name", subcategories.get(i)).findFirst());
                }
                final moneyController newMoneyController = new moneyController(formattedSubcategories, max, name);
                database.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        database.copyToRealm(newMoneyController);
                    }
                });
                moneyController storedMoneyController = database.where(moneyController.class).equalTo("id", newMoneyController.getId()).findFirst();
                mainFragment.updateCategoryAdded(storedMoneyController);
                settingFragment.confirmAddedMoneyController(storedMoneyController);
            } else {
                settingFragment.newMoneyControllerCanceled();
            }
        } else if (requestCode == REQUEST_EDIT_MONEY_CONTROLLER) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();

                Long id = extras.getLong(CONTROLLER_ID_KEY);
                final String name = extras.getString(CATEGORY_NAME_KEY);
                final Double max = extras.getDouble(CATEGORY_MAXIMUM_KEY);
                ArrayList<String> subcategories = extras.getStringArrayList(CATEGORY_SUBCATEGORIES_KEY);
                final RealmList<category> formattedSubcategories = new RealmList<>();
                for (int i = 0; i < subcategories.size(); i++) {
                    formattedSubcategories.add(database.where(category.class).contains("name", subcategories.get(i)).findFirst());
                }

                final moneyController oldMoneyController = database.where(moneyController.class).equalTo("id", id).findFirst();
                String oldName = oldMoneyController.getName();
                database.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        oldMoneyController.setName(name);
                        oldMoneyController.setMaximum(max);
                        oldMoneyController.setSubcategories(formattedSubcategories);
                    }
                });

                moneyController storedMoneyController = database.where(moneyController.class).equalTo("id", id).findFirst();
                mainFragment.updateCategoryItem(storedMoneyController);
                if (!oldName.equals(name)) {
                    settingFragment.modifyOutgoingCategory(storedMoneyController);
                }
            }
        } else if (requestCode == REQUEST_EDIT_PERIODIC_ENTRY) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                long id = extras.getLong(ID_KEY);
                final double value = extras.getDouble(PERIODIC_ENTRY_VALUE_KEY);
                final long categoryId = extras.getLong(PERIODIC_ENTRY_CATEGORY_KEY);
                final String description = extras.getString(PERIODIC_ENTRY_DESCRIPTION_KEY);
                final int quantity = extras.getInt(PERIODIC_ENTRY_QUANTITY_KEY);
                final int frequency = extras.getInt(PERIODIC_ENTRY_FREQUENCY_KEY);
                final ArrayList<Integer> repetitions = extras.getIntegerArrayList(PERIODIC_ENTRY_REPETITIONS_KEY);
                final Date start = (Date) extras.get(PERIODIC_ENTRY_START_KEY);
                final Date end = (Date) extras.get(PERIODIC_ENTRY_END_KEY);
                final boolean ask = extras.getBoolean(PERIODIC_ENTRY_ASK_KEY);

                final periodicEntry changedPeriodicEntry = database.where(periodicEntry.class).equalTo("id", id).findFirst();
                database.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        changedPeriodicEntry.setValue(value);
                        changedPeriodicEntry.setCategoryId(categoryId);
                        changedPeriodicEntry.setDescription(description);
                        changedPeriodicEntry.setQuantityOf(quantity);
                        changedPeriodicEntry.setFrequency(frequency);
                        RealmList<Integer> daysOfRepetitions = new RealmList<>();
                        daysOfRepetitions.addAll(repetitions);
                        changedPeriodicEntry.setDaysOfRepetition(daysOfRepetitions);
                        changedPeriodicEntry.setStartDate(start);
                        changedPeriodicEntry.setEndDate(end);
                        changedPeriodicEntry.setAskBefore(ask);
                    }
                });

                settingFragment.modifyPeriodicEntry(changedPeriodicEntry);
            }
        } else if (requestCode == REQUEST_NEW_PERIODIC_ENTRY) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                final double value = extras.getDouble(PERIODIC_ENTRY_VALUE_KEY);
                final long categoryId = extras.getLong(PERIODIC_ENTRY_CATEGORY_KEY);
                final String description = extras.getString(PERIODIC_ENTRY_DESCRIPTION_KEY);
                final int quantity = extras.getInt(PERIODIC_ENTRY_QUANTITY_KEY);
                final int frequency = extras.getInt(PERIODIC_ENTRY_FREQUENCY_KEY);
                final ArrayList<Integer> repetitions = extras.getIntegerArrayList(PERIODIC_ENTRY_REPETITIONS_KEY);
                final Date start = (Date) extras.get(PERIODIC_ENTRY_START_KEY);
                final Date end = (Date) extras.get(PERIODIC_ENTRY_END_KEY);
                final boolean ask = extras.getBoolean(PERIODIC_ENTRY_ASK_KEY);

                RealmList<Integer> daysOfRepetitions = new RealmList<>();
                daysOfRepetitions.addAll(repetitions);

                periodicEntry.periodicType formattedFrequency;
                if (DAILY.ordinal() == frequency) {
                    formattedFrequency = DAILY;
                } else if (WEEKLY.ordinal() == frequency) {
                    formattedFrequency = WEEKLY;
                } else if (MONTHLY.ordinal() == frequency) {
                    formattedFrequency = MONTHLY;
                } else {
                    formattedFrequency = ANNUAL;
                }
                final periodicEntry periodicEntry = new periodicEntry(value, categoryId, description, quantity, formattedFrequency, start, end, daysOfRepetitions, ask);
                database.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        database.copyToRealm(periodicEntry);
                    }
                });

                settingFragment.confirmPeriodicEntry(periodicEntry);
            } else {
                settingFragment.newPeriodicEntryCanceled();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void updateData() {
        mainFragment.updateData();

        RealmResults<periodicEntry> periodicEntries = database.where(periodicEntry.class).findAll();

        Date currentDate = new Date();
        for (int i = 0; i < periodicEntries.size(); i++) {
            periodicEntries.get(i).update(currentDate, onEntriesChangeInterface);
        }
    }

}