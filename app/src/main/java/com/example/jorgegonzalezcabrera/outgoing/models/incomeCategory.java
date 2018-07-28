package com.example.jorgegonzalezcabrera.outgoing.models;

import io.realm.RealmObject;

public class incomeCategory extends RealmObject {

    private String name;

    public incomeCategory() {
        this.name = "";
    }

    // An 0 exactValue will be interpreted as an irregular income
    public incomeCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
