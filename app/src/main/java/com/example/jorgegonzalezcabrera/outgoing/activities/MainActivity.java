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
import android.support.v7.widget.CardView;
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
import com.example.jorgegonzalezcabrera.outgoing.activities.editFieldActivity.editIncomeCategoryInterface;
import com.example.jorgegonzalezcabrera.outgoing.adapters.editableOutgoingCategoriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.dialogs.dialogs;
import com.example.jorgegonzalezcabrera.outgoing.fragments.actionsFragment;
import com.example.jorgegonzalezcabrera.outgoing.fragments.mainFragment;
import com.example.jorgegonzalezcabrera.outgoing.fragments.settingFragment;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.moneyController;
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
        , editableOutgoingCategoriesAdapter.editOutgoingCategoryInterface
        , editIncomeCategoryInterface {

    private ViewPager viewPager;
    private FragmentStatePagerAdapter viewPagerAdapter;
    private actionsFragment actionsFragment;
    private mainFragment mainFragment;
    private settingFragment settingFragment;
    private Realm database;
    private localUtils.OnEntriesChangeInterface onEntriesChangeInterface;
    private boolean floatingMenuOpen;
    private FloatingActionButton fabMenu;
    private FloatingActionButton fabAddEntry;
    private FloatingActionButton fabAddPeriodicEntry;
    private CardView labelAddEntry;
    private CardView labelAddPeriodicEntry;

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
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.setting));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.viewPager);
        final Vector<Fragment> fragments = new Vector<>();
        mainFragment = new mainFragment();
        fragments.add(mainFragment);
        actionsFragment = new actionsFragment();
        fragments.add(actionsFragment);
        settingFragment = new settingFragment();
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
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

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

    private void closeFloatingMenu() {
        fabMenu.animate().rotation(0);

        fabAddEntry.animate().translationY(dpToPixels(MainActivity.this, 0.0f));
        labelAddEntry.animate().translationY(dpToPixels(MainActivity.this, 0.0f));
        labelAddEntry.animate().alpha(0.0f).setDuration(300);

        fabAddPeriodicEntry.animate().translationY(dpToPixels(MainActivity.this, 0.0f));
        labelAddPeriodicEntry.animate().translationY(dpToPixels(MainActivity.this, 0.0f));
        labelAddPeriodicEntry.animate().alpha(0.0f).setDuration(300);

        floatingMenuOpen = false;
    }

    private void openFloatingMenu() {
        fabMenu.animate().rotation(180);

        fabAddEntry.animate().translationY(dpToPixels(MainActivity.this, -56.0f));
        labelAddEntry.animate().translationY(dpToPixels(MainActivity.this, -56.0f));
        labelAddEntry.animate().alpha(1.0f).setDuration(150);

        fabAddPeriodicEntry.animate().translationY(dpToPixels(MainActivity.this, -101.0f));
        labelAddPeriodicEntry.animate().translationY(dpToPixels(MainActivity.this, -101.0f));
        labelAddPeriodicEntry.animate().alpha(1.0f).setDuration(300);

        floatingMenuOpen = true;
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
    public void changeMoneyControllerSubcategories(@NonNull final moneyController moneyControllers, @NonNull final Vector<String> newCategories) {
        mainFragment.updateCategorySubcategoriesChanged(moneyControllers);
        settingFragment.modifyOutgoingCategory(moneyControllers);
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                moneyControllers.getSubcategories().clear();
                for (int i = 0; i < newCategories.size(); i++) {
                    category newCategory = database.where(category.class).contains("name", newCategories.get(i)).findFirst();
                    moneyControllers.getSubcategories().add(newCategory);
                }
                database.copyToRealmOrUpdate(moneyControllers);
            }
        });

    }

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
    public static final int REQUEST_EDIT_MONEY_CONTROLLER_MAXIMUM = 5;
    public static final int REQUEST_EDIT_OUTGOING_CATEGORY_NAME = 6;
    public static final int REQUEST_EDIT_SUBCATEGORY = 7;

    @Override
    public void edit(moneyController moneyController, ConstraintLayout container, EditText categoryName, EditText categoryMaximum) {
        Intent intent = new Intent(this, editOutgoingCategoryActivity.class);

        List<String> subcategories = new ArrayList<>();
        for (int i = 0; i < moneyController.getSubcategories().size(); i++) {
            subcategories.add(moneyController.getSubcategories().get(i).getName());
        }

        intent.putExtra(CONTAINER_TRANSITION_NAME_KEY, container.getTransitionName());
        intent.putExtra(CATEGORY_NAME_TRANSITION_NAME_KEY, categoryName.getTransitionName());
        intent.putExtra(CATEGORY_MAXIMUM_TRANSITION_NAME_KEY, categoryMaximum.getTransitionName());
        intent.putExtra(CATEGORY_NAME_KEY, moneyController.getName());
        intent.putExtra(CATEGORY_MAXIMUM_KEY, moneyController.getMaximum());
        intent.putStringArrayListExtra(CATEGORY_SUBCATEGORIES_KEY, (ArrayList<String>) subcategories);

        Pair<View, String> p1 = Pair.create((View) categoryName, categoryName.getTransitionName());
        Pair<View, String> p2 = Pair.create((View) categoryMaximum, categoryMaximum.getTransitionName());
        Pair<View, String> p3 = Pair.create((View) container, container.getTransitionName());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2, p3);

        startActivityForResult(intent, REQUEST_ADD_MONEY_CONTROLLER, options.toBundle());
    }

    public static final String FIELD_TRANSITION_NAME_KEY = "fieldTransitionName";
    public static final String INITIAL_VALUE_KEY = "initialValueT";
    public static final String FINAL_VALUE_KEY = "finalValue";
    public static final String HINT_KEY = "hint";
    public static final String ID_KEY = "id";
    public static final String REQUEST_CODE_KEY = "requestCode";

    @Override
    public void editCategoryField(String initialValue, ConstraintLayout container, TextView field, String hint, int requestCode, long id) {
        Intent intent = new Intent(this, editFieldActivity.class);

        intent.putExtra(CONTAINER_TRANSITION_NAME_KEY, container.getTransitionName());
        intent.putExtra(FIELD_TRANSITION_NAME_KEY, field.getTransitionName());
        intent.putExtra(INITIAL_VALUE_KEY, initialValue);
        intent.putExtra(HINT_KEY, hint);
        intent.putExtra(ID_KEY, id);
        intent.putExtra(REQUEST_CODE_KEY, requestCode);

        Pair<View, String> p1 = Pair.create((View) field, field.getTransitionName());
        Pair<View, String> p2 = Pair.create((View) container, container.getTransitionName());

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2);

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
                    formattedSubcategories.add(new category(subcategories.get(i), category.OUTGOING));
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
        } else if (requestCode == REQUEST_EDIT_OUTGOING_CATEGORY_NAME) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                final String newName = extras.getString(FINAL_VALUE_KEY);
                long id = extras.getLong(ID_KEY);
                final moneyController modifiedMoneyController = database.where(moneyController.class).equalTo("id", id).findFirst();
                final String oldName = modifiedMoneyController.getName();
                if (!newName.equals(oldName)) {
                    database.executeTransaction(new Realm.Transaction() {
                        public void execute(Realm realm) {
                            modifiedMoneyController.setName(newName);
                            realm.copyToRealmOrUpdate(modifiedMoneyController);
                        }
                    });
                    mainFragment.updateCategoryNameChanged(modifiedMoneyController);
                    settingFragment.modifyOutgoingCategory(modifiedMoneyController);
                }
            }
        } else if (requestCode == REQUEST_EDIT_MONEY_CONTROLLER_MAXIMUM) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                final Double newMaximum = extras.getDouble(FINAL_VALUE_KEY);
                long id = extras.getLong(ID_KEY);
                final moneyController modifiedMoneyController = database.where(moneyController.class).equalTo("id", id).findFirst();
                if (newMaximum != modifiedMoneyController.getMaximum()) {
                    database.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            modifiedMoneyController.setMaximum(newMaximum);
                            database.copyToRealmOrUpdate(modifiedMoneyController);
                        }
                    });
                    mainFragment.updateCategoryMaximumChanged(modifiedMoneyController);
                    settingFragment.modifyOutgoingCategory(modifiedMoneyController);
                }
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