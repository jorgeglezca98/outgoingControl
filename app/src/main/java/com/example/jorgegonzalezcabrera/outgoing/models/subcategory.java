package com.example.jorgegonzalezcabrera.outgoing.models;

import com.example.jorgegonzalezcabrera.outgoing.applications.myApplication;

import javax.annotation.Nonnull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class subcategory extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;

    public subcategory(@Nonnull String name) {
        this.id = myApplication.outgoingCategorySubcategoryId.incrementAndGet();
        this.name = name;
    }

    public subcategory() {
        this.id = -1;
        this.name = "";
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
