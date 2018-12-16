package com.example.jorgegonzalezcabrera.outgoing.models;

import com.example.jorgegonzalezcabrera.outgoing.applications.myApplication;

import java.util.Date;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getTypeFromOrdinal;

public class entry extends RealmObject {

    @PrimaryKey
    private long id;
    private double valor;
    private int type;
    private String categoryName;
    private Date creationDate;
    private String description;

    public entry() {
        this.id = -1;
        this.valor = 0;
        this.type = -1;
        this.categoryName = "";
        this.creationDate = new Date();
        this.description = "Not described";
    }

    public entry(double valor, @Nonnull category.typeOfCategory type, @Nonnull String categoryName, String description) {
        this.id = myApplication.entryId.incrementAndGet();
        this.valor = valor;
        this.type = type.ordinal();
        this.categoryName = categoryName;
        this.creationDate = new Date();
        this.description = description == null ? "Not described" : description;
    }

    public entry(double valor, @Nonnull category.typeOfCategory type, @Nonnull String categoryName, String description, Date creationDate) {
        this.id = myApplication.entryId.incrementAndGet();
        this.valor = valor;
        this.type = type.ordinal();
        this.categoryName = categoryName;
        this.creationDate = creationDate;
        this.description = description == null ? "Not described" : description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(@Nonnegative double valor) {
        this.valor = valor;
    }

    public category.typeOfCategory getType() {
        return getTypeFromOrdinal(type);
    }

    public void setType(@Nonnull category.typeOfCategory type) {
        this.type = type.ordinal();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(@Nonnull String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "Not described" : description;
    }
}
