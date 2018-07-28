package com.example.jorgegonzalezcabrera.outgoing.models;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class appConfiguration extends RealmObject {

    @PrimaryKey
    private long id;
    private double currentMoney;
    private RealmList<outgoingCategory> outgoingCategoriesCategories;
    private RealmList<incomeCategory> incomeCategories;

    public appConfiguration(double currentMoney, RealmList<outgoingCategory> outgoingCategoriesCategories, RealmList<incomeCategory> incomeCategories) {
        this.id = 0;
        this.currentMoney = currentMoney;
        this.outgoingCategoriesCategories = outgoingCategoriesCategories;
        this.incomeCategories = incomeCategories;
    }

    public appConfiguration() {
        this.id = 0;
        this.currentMoney = 0;
        this.outgoingCategoriesCategories = new RealmList<>();
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

    public RealmList<outgoingCategory> getOutgoingCategoriesCategories() {
        return outgoingCategoriesCategories;
    }

    public void setOutgoingCategoriesCategories(RealmList<outgoingCategory> outgoingCategoriesCategories) {
        this.outgoingCategoriesCategories = outgoingCategoriesCategories;
    }

    public RealmList<incomeCategory> getIncomeCategories() {
        return incomeCategories;
    }

    public void setIncomeCategories(RealmList<incomeCategory> incomeCategories) {
        this.incomeCategories = incomeCategories;
    }
}
