package com.example.jorgegonzalezcabrera.outgoing.models;

import javax.annotation.Nonnull;

import io.realm.RealmObject;

public class subcategory extends RealmObject {
    private String name;

    public subcategory(@Nonnull String name) {
        this.name = name;
    }

    public subcategory() {
        name = "";
    }

    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }
}
