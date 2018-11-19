package com.example.jorgegonzalezcabrera.outgoing.models;

import android.support.annotation.NonNull;

import com.example.jorgegonzalezcabrera.outgoing.applications.myApplication;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.OnEntriesChangeInterface;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.annotation.Nonnull;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class periodicEntry extends RealmObject {

    public enum periodicType {NONE, DAILY, WEEKLY, MONTHLY, ANNUAL}

    @PrimaryKey
    private long id;
    private double value;
    private int categoryId;
    private String description;

    private int quantityOf;
    private int frequency;
    private RealmList<Integer> daysOfRepetition;
    private Date startDate;
    private Date endDate;
    private boolean askBefore;

    private Date lastChange;

    public periodicEntry() {
        this.id = -1;
        this.value = 0;
        this.categoryId = -1;
        this.description = "Not described";

        this.quantityOf = 0;
        this.frequency = periodicType.NONE.ordinal();
        this.daysOfRepetition = new RealmList<>();
        this.startDate = null;
        this.endDate = null;
        this.askBefore = false;

        this.lastChange = new Date();
    }

    public periodicEntry(double value, int categoryId, String description, int quantityOf,
                         @Nonnull periodicType frequency, Date startDate, Date endDate,
                         RealmList<Integer> daysOfRepetition) {
        this.id = myApplication.periodicEntryId.incrementAndGet();
        this.value = value;
        this.categoryId = categoryId;
        this.description = description == null ? "Not described" : description;

        this.quantityOf = quantityOf;
        this.frequency = frequency.ordinal();
        this.daysOfRepetition = daysOfRepetition;
        Collections.sort(this.daysOfRepetition);
        this.startDate = startDate;
        this.endDate = endDate;
        this.askBefore = false;

        this.lastChange = startDate;
    }

    public long getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantityOf() {
        return quantityOf;
    }

    public RealmList<Integer> getDaysOfRepetition() {
        return daysOfRepetition;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isAskBefore() {
        return askBefore;
    }

    public Date getLastChange() {
        return lastChange;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantityOf(int quantityOf) {
        this.quantityOf = quantityOf;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setDaysOfRepetition(RealmList<Integer> daysOfRepetition) {
        this.daysOfRepetition = daysOfRepetition;
        Collections.sort(this.daysOfRepetition);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        if (this.lastChange.before(startDate))
            this.lastChange = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setAskBefore(boolean askBefore) {
        this.askBefore = askBefore;
    }

    private void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
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

    public entry getEntry() {
        category category;
        Realm.getDefaultInstance().beginTransaction();
        category = Realm.getDefaultInstance().where(category.class).equalTo("id", categoryId).findFirst();
        Realm.getDefaultInstance().commitTransaction();
        return new entry(value, category.getType() == entry.type.OUTGOING.ordinal() ? entry.type.OUTGOING : entry.type.INCOME, category.getName(), description);
    }

    public void update(Date currentDate, OnEntriesChangeInterface entryAddedInterface) {
        GregorianCalendar formattedCurrentDate = new GregorianCalendar();
        formattedCurrentDate.setTime(currentDate);
        GregorianCalendar formattedLastChange = new GregorianCalendar();
        formattedLastChange.setTime(lastChange);
        if (currentDate.before(endDate)) {
            if (startDate.before(currentDate)) {
                if (frequency == periodicType.DAILY.ordinal()) {
                    formattedLastChange.add(Calendar.DATE, quantityOf);
                    while (formattedLastChange.before(formattedCurrentDate)) {
                        createEntry(entryAddedInterface, formattedCurrentDate);
                        lastChange = formattedLastChange.getTime();
                        formattedLastChange.add(Calendar.DATE, quantityOf);
                    }
                } else if (frequency == periodicType.WEEKLY.ordinal()) {
                    if (formattedLastChange.get(Calendar.DAY_OF_WEEK) < daysOfRepetition.last()) {
                        int i = 0;
                        while (i < daysOfRepetition.size() && formattedLastChange.get(Calendar.DAY_OF_WEEK) <= daysOfRepetition.get(i)) {
                            i++;
                        }
                        formattedLastChange.set(Calendar.DAY_OF_WEEK, daysOfRepetition.get(i));
                    } else {
                        formattedLastChange.set(Calendar.DAY_OF_WEEK, daysOfRepetition.first());
                        formattedLastChange.add(Calendar.DATE, 7);
                    }
                    while (formattedLastChange.before(formattedCurrentDate)) {
                        createEntry(entryAddedInterface, formattedCurrentDate);
                        lastChange = formattedLastChange.getTime();
                        if (formattedLastChange.get(Calendar.DAY_OF_WEEK) < daysOfRepetition.last()) {
                            int i = 0;
                            while (i < daysOfRepetition.size() && formattedLastChange.get(Calendar.DAY_OF_WEEK) <= daysOfRepetition.get(i)) {
                                i++;
                            }
                            formattedLastChange.set(Calendar.DAY_OF_WEEK, daysOfRepetition.get(i));
                        } else {
                            formattedLastChange.set(Calendar.DAY_OF_WEEK, daysOfRepetition.first());
                            formattedLastChange.add(Calendar.DATE, 7);
                        }
                    }
                } else if (frequency == periodicType.MONTHLY.ordinal()) {
                    if (formattedLastChange.get(Calendar.DAY_OF_MONTH) < daysOfRepetition.last()) {
                        int i = 0;
                        while (i < daysOfRepetition.size() && formattedLastChange.get(Calendar.DAY_OF_MONTH) <= daysOfRepetition.get(i)) {
                            i++;
                        }
                        formattedLastChange.set(Calendar.DAY_OF_MONTH, daysOfRepetition.get(i));
                    } else {
                        formattedLastChange.set(Calendar.DAY_OF_MONTH, daysOfRepetition.first());
                        formattedLastChange.add(Calendar.MONTH, 1);
                    }
                    while (formattedLastChange.before(formattedCurrentDate)) {
                        createEntry(entryAddedInterface, formattedCurrentDate);
                        lastChange = formattedLastChange.getTime();
                        if (formattedLastChange.get(Calendar.DAY_OF_MONTH) < daysOfRepetition.last()) {
                            int i = 0;
                            while (i < daysOfRepetition.size() && formattedLastChange.get(Calendar.DAY_OF_MONTH) <= daysOfRepetition.get(i)) {
                                i++;
                            }
                            formattedLastChange.set(Calendar.DAY_OF_MONTH, daysOfRepetition.get(i));
                        } else {
                            formattedLastChange.set(Calendar.DAY_OF_MONTH, daysOfRepetition.first());
                            formattedLastChange.add(Calendar.MONTH, 1);
                        }
                    }
                } else if (frequency == periodicType.ANNUAL.ordinal()) {
                    formattedLastChange.add(Calendar.YEAR, quantityOf); //Todo: comprobar que no hay problemas con bisiestos
                    while (formattedLastChange.before(formattedCurrentDate)) {
                        createEntry(entryAddedInterface, formattedCurrentDate);
                        lastChange = formattedLastChange.getTime();
                        formattedLastChange.add(Calendar.YEAR, quantityOf);
                    }
                }
            }
        } else {
            deleteFromRealm();
        }
    }

    private void createEntry(OnEntriesChangeInterface entryAddedInterface, final GregorianCalendar currentDate) {
        final entry newEntry = getEntry();

        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        setLastChange(currentDate.getTime());

        final Realm database = Realm.getDefaultInstance();
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                database.copyToRealmOrUpdate(periodicEntry.this);
            }
        });

        entryAddedInterface.addEntry(newEntry);
    }

}
