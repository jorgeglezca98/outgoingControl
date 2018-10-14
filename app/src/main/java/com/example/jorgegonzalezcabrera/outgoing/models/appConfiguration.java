package com.example.jorgegonzalezcabrera.outgoing.models;

import com.example.jorgegonzalezcabrera.outgoing.applications.myApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class appConfiguration extends RealmObject {

    @PrimaryKey
    private long id;
    private double currentMoney;

    public appConfiguration(double currentMoney) {
        this.id = myApplication.appConfigurationId.incrementAndGet();
        this.currentMoney = currentMoney;
    }

    public appConfiguration() {
        this.id = -1;
        this.currentMoney = -1;
    }

    public double getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(double currentMoney) {
        this.currentMoney = currentMoney;
    }

    public long getId() {
        return id;
    }
}