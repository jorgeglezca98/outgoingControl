package com.example.jorgegonzalezcabrera.outgoing.models;

import com.example.jorgegonzalezcabrera.outgoing.applications.myApplication;

import javax.annotation.Nonnull;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class outgoingCategory extends RealmObject {

    @PrimaryKey
    private long id;
    private RealmList<category> subcategories;
    private double maximum;
    private String name;

    public outgoingCategory() {
        this.id = -1;
        this.subcategories = new RealmList<>();
        this.maximum = 0;
        this.name = "";
    }

    public outgoingCategory(@Nonnull RealmList<category> subcategories, double maximum, @Nonnull String name) {
        this.id = myApplication.outgoingCategoryId.incrementAndGet();
        this.subcategories = new RealmList<>();
        for (int i = 0; i < subcategories.size(); i++) {
            if (subcategories.get(i) != null) {
                this.subcategories.add(subcategories.get(i));
            }
        }
        this.maximum = maximum;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setAvailableId() {
        this.id = myApplication.outgoingCategoryId.incrementAndGet();
    }

    public RealmList<category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(@Nonnull RealmList<category> subcategories) {
        this.subcategories = new RealmList<>();
        for (int i = 0; i < subcategories.size(); i++) {
            if (subcategories.get(i) != null) {
                this.subcategories.add(subcategories.get(i));
            }
        }
    }

    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public boolean check() {
        return id >= 0 && subcategories != null && subcategories.size() != 0 && maximum > 0 && !name.isEmpty();
    }

}
