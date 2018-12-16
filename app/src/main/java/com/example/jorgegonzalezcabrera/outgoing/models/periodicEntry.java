package com.example.jorgegonzalezcabrera.outgoing.models;

import android.support.annotation.NonNull;

import com.example.jorgegonzalezcabrera.outgoing.applications.myApplication;
import com.example.jorgegonzalezcabrera.outgoing.models.category.typeOfCategory;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.OnEntriesChangeInterface;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

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
    private long categoryId;
    private String description;

    private int quantityOf;
    private int frequency;
    private RealmList<Integer> daysOfRepetition;
    private Date startDate;
    private Date endDate;
    private boolean askBefore;

    private Date lastChange;

    public periodicEntry() {
        this.id = myApplication.periodicEntryId.incrementAndGet();
        this.value = 0;
        this.categoryId = -1;
        this.description = "";

        this.quantityOf = 0;
        this.frequency = periodicType.NONE.ordinal();
        this.daysOfRepetition = new RealmList<>();
        this.startDate = null;
        this.endDate = null;
        this.askBefore = false;

        this.lastChange = new Date();
    }

    public periodicEntry(double value, long categoryId, String description, int quantityOf,
                         @Nonnull periodicType frequency, Date startDate, Date endDate,
                         RealmList<Integer> daysOfRepetition, boolean askBefore) {
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
        this.askBefore = askBefore;

        this.lastChange = startDate;
    }

    public long getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public long getCategoryId() {
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

    public void setCategoryId(long categoryId) {
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
        if (frequency == periodicType.DAILY.ordinal()) {
            return periodicType.DAILY;
        } else if (frequency == periodicType.WEEKLY.ordinal()) {
            return periodicType.WEEKLY;
        } else if (frequency == periodicType.MONTHLY.ordinal()) {
            return periodicType.MONTHLY;
        } else {
            return periodicType.ANNUAL;
        }
    }

    public static GregorianCalendar setLastDayByTimes(int each, int times, periodicType frequency, GregorianCalendar startDate) {
        GregorianCalendar lastDate = new GregorianCalendar();
        lastDate.setTime(startDate.getTime());

        int typeOfFrequency;
        if (frequency == periodicType.DAILY) {
            typeOfFrequency = Calendar.DATE;
        } else if (frequency == periodicType.ANNUAL) {
            if (lastDate.get(Calendar.MONTH) == Calendar.FEBRUARY && lastDate.get(Calendar.DAY_OF_MONTH) == 29 && each != 4) {
                return null;
            }
            typeOfFrequency = Calendar.YEAR;
        } else {
            return null;
        }

        for (int i = 0; i < times; i++) {
            lastDate.add(typeOfFrequency, each);
        }

        return lastDate;
    }

    public static GregorianCalendar setLastDayByTimes(int each, int times, periodicType frequency, GregorianCalendar startDate, RealmList<Integer> daysOfRepetition) {
        GregorianCalendar lastDate = new GregorianCalendar();
        lastDate.setTime(startDate.getTime());
        if (frequency == periodicType.WEEKLY) {
            if (daysOfRepetition.isEmpty() || (Collections.max(daysOfRepetition) > 6)) {
                return null;
            }
            int pendingRepetitions = times;
            if (daysOfRepetition.contains(lastDate.get(Calendar.DAY_OF_WEEK))) {
                pendingRepetitions--;
            }
            while (pendingRepetitions % daysOfRepetition.size() != 0) {
                if (lastDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    lastDate.add(Calendar.WEEK_OF_YEAR, each);
                    lastDate.add(Calendar.DATE, -6);
                    pendingRepetitions -= daysOfRepetition.size();
                } else {
                    lastDate.add(Calendar.DATE, 1);
                }
                if (daysOfRepetition.contains(lastDate.get(Calendar.DAY_OF_WEEK))) {
                    pendingRepetitions--;
                }
            }
            while (pendingRepetitions >= daysOfRepetition.size()) {
                lastDate.add(Calendar.WEEK_OF_YEAR, each);
                pendingRepetitions -= daysOfRepetition.size();
            }
        } else if (frequency == periodicType.MONTHLY) {
            if (daysOfRepetition.isEmpty() || (Collections.max(daysOfRepetition) > 28)) {
                return null;
            }
            int pendingRepetitions = times;
            if (daysOfRepetition.contains(lastDate.get(Calendar.DAY_OF_MONTH))) {
                pendingRepetitions--;
            }
            while (pendingRepetitions % daysOfRepetition.size() != 0) {
                if (lastDate.get(Calendar.DAY_OF_MONTH) >= 28) {
                    lastDate.add(Calendar.MONTH, each);
                    lastDate.add(Calendar.DATE, (1 - lastDate.get(Calendar.DAY_OF_MONTH)));
                    pendingRepetitions -= daysOfRepetition.size();
                } else {
                    lastDate.add(Calendar.DATE, 1);
                }
                if (daysOfRepetition.contains(lastDate.get(Calendar.DAY_OF_MONTH))) {
                    pendingRepetitions--;
                }
            }
            while (pendingRepetitions >= daysOfRepetition.size()) {
                lastDate.add(Calendar.MONTH, each);
                pendingRepetitions -= daysOfRepetition.size();
            }
        } else {
            return null;
        }

        return lastDate;
    }

    public entry getEntry() {
        category category;
        Realm.getDefaultInstance().beginTransaction();
        category = Realm.getDefaultInstance().where(category.class).equalTo("id", categoryId).findFirst();
        Realm.getDefaultInstance().commitTransaction();
        return new entry(value, category.getType() == typeOfCategory.OUTGOING.ordinal() ? typeOfCategory.OUTGOING : typeOfCategory.INCOME, category.getName(), description);
    }

    public void update(Date currentDate, OnEntriesChangeInterface entryAddedInterface) {
        GregorianCalendar formattedCurrentDate = new GregorianCalendar();
        formattedCurrentDate.setTime(currentDate);
        GregorianCalendar formattedLastChange = new GregorianCalendar();
        formattedLastChange.setTime(lastChange);
        Realm database = Realm.getDefaultInstance();
        if (endDate == null || currentDate.before(endDate)) {
            if (startDate.before(currentDate)) {
                if (frequency == periodicType.DAILY.ordinal()) {
                    formattedLastChange.add(Calendar.DATE, quantityOf);
                    while (formattedLastChange.before(formattedCurrentDate)) {
                        createEntry(entryAddedInterface, formattedLastChange);
                        database.beginTransaction();
                        lastChange = formattedLastChange.getTime();
                        database.commitTransaction();
                        formattedLastChange.add(Calendar.DATE, quantityOf);
                    }
                } else if (frequency == periodicType.WEEKLY.ordinal()) {
                    if (formattedLastChange.get(Calendar.DAY_OF_WEEK) < daysOfRepetition.last()) {
                        int i = 0;
                        while (i < daysOfRepetition.size() && formattedLastChange.get(Calendar.DAY_OF_WEEK) >= daysOfRepetition.get(i)) {
                            i++;
                        }
                        formattedLastChange.set(Calendar.DAY_OF_WEEK, daysOfRepetition.get(i));
                    } else {
                        formattedLastChange.set(Calendar.DAY_OF_WEEK, daysOfRepetition.first());
                        formattedLastChange.add(Calendar.DATE, 7);
                    }
                    while (formattedLastChange.before(formattedCurrentDate)) {
                        createEntry(entryAddedInterface, formattedLastChange);
                        database.beginTransaction();
                        lastChange = formattedLastChange.getTime();
                        database.commitTransaction();
                        if (formattedLastChange.get(Calendar.DAY_OF_WEEK) < daysOfRepetition.last()) {
                            int i = 0;
                            while (i < daysOfRepetition.size() && formattedLastChange.get(Calendar.DAY_OF_WEEK) >= daysOfRepetition.get(i)) {
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
                        while (i < daysOfRepetition.size() && formattedLastChange.get(Calendar.DAY_OF_MONTH) >= daysOfRepetition.get(i)) {
                            i++;
                        }
                        formattedLastChange.set(Calendar.DAY_OF_MONTH, daysOfRepetition.get(i));
                    } else {
                        formattedLastChange.set(Calendar.DAY_OF_MONTH, daysOfRepetition.first());
                        formattedLastChange.add(Calendar.MONTH, 1);
                    }
                    while (formattedLastChange.before(formattedCurrentDate)) {
                        createEntry(entryAddedInterface, formattedLastChange);
                        database.beginTransaction();
                        lastChange = formattedLastChange.getTime();
                        database.commitTransaction();
                        if (formattedLastChange.get(Calendar.DAY_OF_MONTH) < daysOfRepetition.last()) {
                            int i = 0;
                            while (i < daysOfRepetition.size() && formattedLastChange.get(Calendar.DAY_OF_MONTH) >= daysOfRepetition.get(i)) {
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
                        createEntry(entryAddedInterface, formattedLastChange);
                        database.beginTransaction();
                        lastChange = formattedLastChange.getTime();
                        database.commitTransaction();
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

        final Realm database = Realm.getDefaultInstance();
        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                setLastChange(currentDate.getTime());
                database.copyToRealmOrUpdate(periodicEntry.this);
            }
        });

        entryAddedInterface.addEntry(newEntry);
    }

}
