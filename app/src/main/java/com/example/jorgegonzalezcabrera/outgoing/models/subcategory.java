package com.example.jorgegonzalezcabrera.outgoing.models;

import io.realm.RealmObject;

public class subcategory extends RealmObject {
    private String name;

    public subcategory(String name) {
        this.name = name;
    }

    public subcategory() {
        name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
