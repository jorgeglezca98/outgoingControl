package com.example.jorgegonzalezcabrera.outgoing.models;

import java.util.Date;

import javax.annotation.Nonnull;

import io.realm.RealmObject;

public class entry extends RealmObject {

    enum type{OUTGOING,INCOME}

    private double valor;
    private int type; //**Puede que acabe sobrando
    private String category;
    private Date date;
    private String description;

    public entry() {
        this.valor = 0;
        this.type = -1;
        this.category = "";
        this.date = new Date();
        this.description = "Not described";
    }

    public entry(double valor, int type,@Nonnull String category,@Nonnull String description) {
        this.valor = valor;
        this.type = type;
        this.category = category;
        this.date = new Date();
        this.description = description;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
