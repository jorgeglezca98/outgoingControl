package com.example.jorgegonzalezcabrera.outgoing.models;

import javax.annotation.Nonnull;

import io.realm.RealmList;
import io.realm.RealmObject;

public class outgoingCategory extends RealmObject {

    private RealmList<subcategory> subcategories;
    private double maximum;
    private String name;

    public outgoingCategory() {
        this.subcategories = new RealmList<>();
        this.maximum = 0;
        this.name = "";
    }

    public outgoingCategory(@Nonnull RealmList<subcategory> subcategories, double maximum, @Nonnull String name) {
        this.subcategories = new RealmList<>();
        for (int i = 0; i < subcategories.size(); i++) {
            if (subcategories.get(i) != null) {
                this.subcategories.add(subcategories.get(i));
            }
        }
        if (this.subcategories.isEmpty()) {
            this.subcategories.add(new subcategory(name));
        }
        this.maximum = maximum;
        this.name = name;
    }

    public RealmList<subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(@Nonnull RealmList<subcategory> subcategories) {
        this.subcategories = new RealmList<>();
        for (int i = 0; i < subcategories.size(); i++) {
            if (subcategories.get(i) != null) {
                this.subcategories.add(subcategories.get(i));
            }
        }
        if (this.subcategories.isEmpty()) {
            this.subcategories.add(new subcategory(name));
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
}
