package com.example.jorgegonzalezcabrera.outgoing.models;

import com.example.jorgegonzalezcabrera.outgoing.applications.myApplication;

import javax.annotation.Nonnull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class incomeCategory extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;

    public incomeCategory() {
        this.id = -1;
        this.name = "";
    }

    public incomeCategory(@Nonnull String name) {
        this.id = myApplication.incomeCategoryId.incrementAndGet();
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

}
