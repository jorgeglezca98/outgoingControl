package com.example.jorgegonzalezcabrera.outgoing.models;

import com.example.jorgegonzalezcabrera.outgoing.applications.myApplication;

import java.util.Date;

import javax.annotation.Nonnull;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class periodicEntry extends RealmObject {

    public enum categoryType{OUTGOING,INCOME}
    public enum periodicType{WEEKLY,MONTHLY,ANNUAL}

    @PrimaryKey
    private long id;
    private double value;
    private categoryType categoryType;
    private String category;
    private String description;
    private periodicType periodicType;
    private int[] selectedDates;

    public periodicEntry() {
        //TODO: I am not really sure it is the right way
        this.id = 0;
        this.value = 0;
        this.categoryType = categoryType.OUTGOING;
        this.category = "";
        this.description = "Not described";
        this.periodicType = periodicType.WEEKLY;
        this.selectedDates = new int[0];
    }

    public periodicEntry(double value, categoryType categoryType, String category,
                         @Nonnull String description, periodicType periodicType, int[] selectedDates) {
        this.id = myApplication.periodicEntryId.incrementAndGet();
        this.value = value;
        this.categoryType = categoryType;
        this.category = category;
        this.description = description.isEmpty() ? "Not described" : description;
        this.periodicType = periodicType;
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

    public categoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(categoryType categoryType) {
        this.categoryType = categoryType;
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

    public periodicType getPeriodicType() {
        return periodicType;
    }

    public void setPeriodicType(periodicType periodicType) {
        this.periodicType = periodicType;
    }

    public int[] getSelectedDates() {
        return selectedDates;
    }

    public void setSelectedDates(int[] selectedDates) {
        this.selectedDates = selectedDates;
    }
}
