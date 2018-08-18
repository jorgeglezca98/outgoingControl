package com.example.jorgegonzalezcabrera.outgoing.models;

import android.support.annotation.NonNull;

import com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.OnNewEntryAddedInterface;
import com.example.jorgegonzalezcabrera.outgoing.applications.myApplication;
import com.example.jorgegonzalezcabrera.outgoing.models.entry.type;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.Nonnull;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getTypeFromOrdinal;

public class periodicEntry extends RealmObject {

    public enum periodicType {WEEKLY, MONTHLY, ANNUAL}

    @PrimaryKey
    private long id;
    private double value;
    private int typeOfCategory;
    private String category;
    private String description;
    private int frequency;
    private Date lastChange;
    private int selectedDate;

    public periodicEntry() {
        this.id = -1;
        this.value = 0;
        this.typeOfCategory = -1;
        this.category = "";
        this.description = "Not described";
        this.frequency = -1;
        this.selectedDate = this.frequency == periodicType.ANNUAL.ordinal() ? 0 : 1;
        this.lastChange = new Date();
    }

    public periodicEntry(double value, @Nonnull entry.type typeOfCategory, @Nonnull String category,
                         String description, @Nonnull periodicType frequency, int selectedDate) {
        this.id = myApplication.periodicEntryId.incrementAndGet();
        this.value = value;
        this.typeOfCategory = typeOfCategory.ordinal();
        this.category = category;
        this.description = description == null ? "Not described" : description;
        this.frequency = frequency.ordinal();
        this.selectedDate = selectedDate;
        this.lastChange = new Date();
    }

    public long getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public entry.type getTypeOfCategory() {
        return getTypeFromOrdinal(typeOfCategory);
    }

    public void setTypeOfCategory(entry.type typeOfCategory) {
        this.typeOfCategory = typeOfCategory.ordinal();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public periodicType getFrequency() {
        if (frequency == periodicType.WEEKLY.ordinal()) {
            return periodicType.WEEKLY;
        } else if (frequency == periodicType.MONTHLY.ordinal()) {
            return periodicType.MONTHLY;
        } else {
            return periodicType.ANNUAL;
        }
    }

    public void setFrequency(periodicType frequency) {
        this.frequency = frequency.ordinal();
    }

    public int getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(int selectedDate) {
        this.selectedDate = selectedDate;
    }

    public entry getEntry() {
        return new entry(value, typeOfCategory == type.OUTGOING.ordinal() ? type.OUTGOING : type.INCOME, category, description);
    }

    public Date getLastChange() {
        return lastChange;
    }

    private void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }

    public static void createEntry(final periodicEntry periodicEntry, OnNewEntryAddedInterface entryAddedInterface, final GregorianCalendar currentDate) {
        final entry newEntry = periodicEntry.getEntry();

        final Realm database = Realm.getDefaultInstance();
        final appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();
        if (currentConfiguration != null) {
            if (newEntry.getType() == entry.type.OUTGOING) {
                currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() - newEntry.getValor());
            } else {
                currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() + newEntry.getValor());
            }
        }

        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                if (currentConfiguration != null) {
                    database.copyToRealmOrUpdate(currentConfiguration);
                }
                database.copyToRealm(newEntry);
                periodicEntry.setLastChange(currentDate.getTime());
                database.copyToRealmOrUpdate(periodicEntry);
            }
        });

        entryAddedInterface.OnNewEntryAdded(newEntry);
    }

}
