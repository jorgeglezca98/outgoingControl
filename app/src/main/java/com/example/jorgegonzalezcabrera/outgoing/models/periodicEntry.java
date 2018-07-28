package com.example.jorgegonzalezcabrera.outgoing.models;

import com.example.jorgegonzalezcabrera.outgoing.applications.myApplication;

import java.util.Date;

import javax.annotation.Nonnull;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class periodicEntry extends RealmObject {

    public enum periodicType{WEEKLY,MONTHLY,ANNUAL}

    @PrimaryKey
    private long id;
    private double value;
    private int typeOfCategory;
    private String category;
    private String description;
    private int frequency;
    private Date lastChange;
    private RealmList<Integer> selectedDates; //I am not really sure of this field

    public periodicEntry() {
        //TODO: I am not really sure it is the right way
        this.id = 0;
        this.value = 0;
        this.typeOfCategory = entry.type.OUTGOING.ordinal();
        this.category = "";
        this.description = "Not described";
        this.frequency = periodicType.WEEKLY.ordinal();
        this.selectedDates = new RealmList<>();
    }

    public periodicEntry(double value, entry.type typeOfCategory,@Nonnull String category,
                         @Nonnull String description, periodicType frequency, RealmList<Integer> selectedDates) {
        this.id = myApplication.periodicEntryId.incrementAndGet();
        this.value = value;
        this.typeOfCategory = typeOfCategory.ordinal();
        this.category = category;
        this.description = description.isEmpty() ? "Not described" : description;
        this.frequency = frequency.ordinal();
        this.selectedDates = selectedDates;
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
        if (typeOfCategory == entry.type.OUTGOING.ordinal()) {
            return entry.type.OUTGOING;
        } else {
            return entry.type.INCOME;
        }
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
        } else if(frequency == periodicType.MONTHLY.ordinal()){
            return periodicType.MONTHLY;
        } else{
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
        return new entry(value,typeOfCategory,category,description);
    }

    public Date getLastChange() {
        return lastChange;
    }

    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }
}
