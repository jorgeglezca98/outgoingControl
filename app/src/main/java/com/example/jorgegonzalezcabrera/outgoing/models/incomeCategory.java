package com.example.jorgegonzalezcabrera.outgoing.models;

import io.realm.RealmObject;

public class incomeCategory extends RealmObject {

    private String name;
    private double exactValue; //Optional field

    public incomeCategory() {
        this.name = "";
        this.exactValue = 0;
    }

    // An 0 exactValue will be interpreted as an irregular income
    public incomeCategory(String name, double exactValue) {
        this.name = name;
        this.exactValue = exactValue;
    }

    public incomeCategory(String name) {
        this.name = name;
        this.exactValue = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getExactValue() {
        return exactValue;
    }

    public void setExactValue(double exactValue) {
        this.exactValue = exactValue;
    }
}
