package com.example.jorgegonzalezcabrera.outgoing.models;

import com.example.jorgegonzalezcabrera.outgoing.applications.myApplication;

import javax.annotation.Nonnull;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class appConfiguration extends RealmObject {

    @PrimaryKey
    private long id;
    private double currentMoney;
    private RealmList<outgoingCategory> outgoingCategories;
    private RealmList<incomeCategory> incomeCategories;

    public appConfiguration(double currentMoney, @Nonnull RealmList<outgoingCategory> outgoingCategories, @Nonnull RealmList<incomeCategory> incomeCategories) {
        this.id = myApplication.appConfigurationId.incrementAndGet();
        this.currentMoney = currentMoney;
        this.outgoingCategories = new RealmList<>();
        for (int i = 0; i < outgoingCategories.size(); i++) {
            if (outgoingCategories.get(i) != null)
                this.outgoingCategories.add(outgoingCategories.get(i));
        }
        this.incomeCategories = new RealmList<>();
        for (int i = 0; i < incomeCategories.size(); i++) {
            if (incomeCategories.get(i) != null)
                this.incomeCategories.add(incomeCategories.get(i));
        }
    }

    public appConfiguration() {
        this.id = -1;
        this.currentMoney = -1;
        this.outgoingCategories = new RealmList<>();
        this.incomeCategories = new RealmList<>();
    }

    public double getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(double currentMoney) {
        Realm.getDefaultInstance().beginTransaction();
        this.currentMoney = currentMoney;
        Realm.getDefaultInstance().commitTransaction();
    }

    public RealmList<outgoingCategory> getOutgoingCategories() {
        return outgoingCategories;
    }

    public void setOutgoingCategories(@Nonnull RealmList<outgoingCategory> outgoingCategories) {
        Realm.getDefaultInstance().beginTransaction();
        this.outgoingCategories = new RealmList<>();
        for (int i = 0; i < outgoingCategories.size(); i++) {
            if (outgoingCategories.get(i) != null)
                this.outgoingCategories.add(outgoingCategories.get(i));
        }
        Realm.getDefaultInstance().commitTransaction();
    }

    public RealmList<incomeCategory> getIncomeCategories() {
        return incomeCategories;
    }

    public void setIncomeCategories(@Nonnull RealmList<incomeCategory> incomeCategories) {
        Realm.getDefaultInstance().beginTransaction();
        this.incomeCategories = new RealmList<>();
        for (int i = 0; i < incomeCategories.size(); i++) {
            if (incomeCategories.get(i) != null)
                this.incomeCategories.add(incomeCategories.get(i));
        }
        Realm.getDefaultInstance().commitTransaction();
    }

    public long getId() {
        return id;
    }
}
