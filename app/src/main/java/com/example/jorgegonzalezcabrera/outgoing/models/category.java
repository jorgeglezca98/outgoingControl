package com.example.jorgegonzalezcabrera.outgoing.models;

import com.example.jorgegonzalezcabrera.outgoing.applications.myApplication;

import javax.annotation.Nonnull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class category extends RealmObject {

    public static final int INCOME = 0;
    public static final int OUTGOING = 1;

    @PrimaryKey
    private long id;
    private String name;
    private boolean operative;
    private int type;

    public category(@Nonnull String name, int type) {
        this.id = myApplication.categoryId.incrementAndGet();
        this.name = name;
        this.operative = true;
        this.type = type;
        //TODO: exceptions
    }

    public category() {
        this.id = -1;
        this.name = "";
        this.operative = true;
        this.type = OUTGOING;
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

    public boolean isOperative() {
        return operative;
    }

    public void setOperative(boolean operative) {
        this.operative = operative;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
