package com.example.jorgegonzalezcabrera.outgoing.models;

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

    public outgoingCategory(RealmList<subcategory> subcategories, double maximum, String name) {
        if(subcategories==null || subcategories.isEmpty()){
            this.subcategories = new RealmList<>();
            this.subcategories.add(new subcategory(name));
        } else{
            this.subcategories = subcategories;
        }
        this.maximum = maximum;
        this.name = name;
    }

    public RealmList<subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(RealmList<subcategory> subcategories) {
        this.subcategories = subcategories;
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

    public void setName(String name) {
        this.name = name;
    }
}
