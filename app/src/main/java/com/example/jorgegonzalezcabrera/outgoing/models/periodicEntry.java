package com.example.jorgegonzalezcabrera.outgoing.models;

import com.example.jorgegonzalezcabrera.outgoing.applications.myApplication;
import com.example.jorgegonzalezcabrera.outgoing.models.entry.type;

import java.util.Date;

import javax.annotation.Nonnull;

import io.realm.RealmList;
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
    private RealmList<Integer> selectedDates;

    public periodicEntry() {
        this.id = -1;
        this.value = 0;
        this.typeOfCategory = -1;
        this.category = "";
        this.description = "Not described";
        this.frequency = -1;
        this.selectedDates = new RealmList<>();
        this.lastChange = new Date();
    }

    public periodicEntry(double value, @Nonnull entry.type typeOfCategory, @Nonnull String category,
                         String description, @Nonnull periodicType frequency, @Nonnull RealmList<Integer> selectedDates) {
        this.id = myApplication.periodicEntryId.incrementAndGet();
        this.value = value;
        this.typeOfCategory = typeOfCategory.ordinal();
        this.category = category;
        this.description = description == null ? "Not described" : description;
        this.frequency = frequency.ordinal();
        this.selectedDates = selectedDates;
        if (selectedDates.size() == 0) {
            selectedDates.add(this.frequency == periodicType.ANNUAL.ordinal() ? 0 : 1);
        }
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

    public RealmList<Integer> getSelectedDates() {
        return selectedDates;
    }

    public void setSelectedDates(RealmList<Integer> selectedDates) {
        this.selectedDates = selectedDates;
    }

    public entry getEntry() {
        return new entry(value, typeOfCategory == type.OUTGOING.ordinal() ? type.OUTGOING : type.INCOME, category, description);
    }

    public Date getLastChange() {
        return lastChange;
    }

    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }
}
