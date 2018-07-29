package com.example.jorgegonzalezcabrera.outgoing.models;

import javax.annotation.Nonnull;

import io.realm.RealmObject;

public class incomeCategory extends RealmObject {

    private String name;

    public incomeCategory() {
        this.name = "";
    }

    public incomeCategory(@Nonnull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

}
