package com.example.jorgegonzalezcabrera.outgoing.models;

import java.util.Date;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import io.realm.RealmObject;

import static com.example.jorgegonzalezcabrera.outgoing.models.entry.type.INCOME;
import static com.example.jorgegonzalezcabrera.outgoing.models.entry.type.OUTGOING;

public class entry extends RealmObject {

    public enum type{OUTGOING,INCOME}

    private double valor;
    private int type;
    private String category;
    private Date creationDate;
    private String description;

    public entry() {
        this.valor = 0;
        this.type = -1;
        this.category = "";
        this.creationDate = new Date();
        this.description = "Not described";
    }

    public entry(double valor,@Nonnull type type,@Nonnull String category, String description) {
        this.valor = valor;
        this.type = type.ordinal();
        this.category = category;
        this.creationDate = new Date();
        this.description = description==null ? "Not described" : description;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(@Nonnegative double valor) {
        this.valor = valor;
    }

    public type getType() {
        if(this.type==OUTGOING.ordinal())
            return OUTGOING;
        else
            return INCOME;
    }

    public void setType(@Nonnull type type) {
        this.type = type.ordinal();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(@Nonnull String category) {
        this.category = category;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description==null ? "Not described" : description;
    }
}
