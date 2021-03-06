package com.example.jorgegonzalezcabrera.outgoing.utilities;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;

import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.moneyController;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;

import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmResults;

public class localUtils {

    public interface OnEntriesChangeInterface {
        void addEntry(@NonNull entry newEntry);

        void removeEntry(@NonNull entry removedEntry);

        void editEntry(@NonNull entry nextVersion);
    }

    public interface OnCategoriesChangeInterface {
        void removeAndReplaceCategory(@NonNull category removedCategory, @NonNull String newSubcategory);

        void removeAndKeepCategory(@NonNull category removedCategory);

        void removeMoneyController(@NonNull moneyController removedCategory);
    }

    public interface changePeriodicEntriesInterface {
        void edit(periodicEntry periodicEntry, ConstraintLayout container, int requestCode);

        void remove(periodicEntry periodicEntry);
    }

    public static category.typeOfCategory getTypeFromOrdinal(int ordinal) {
        return (ordinal == category.typeOfCategory.OUTGOING.ordinal()) ? category.typeOfCategory.OUTGOING : category.typeOfCategory.INCOME;
    }

    public static Vector<String> getAllCategories() {
        Vector<String> categories = new Vector<>();
        categories.addAll(getAllOutgoingCategories());
        categories.addAll(getAllIncomeCategories());
        return categories;
    }

    public static Vector<String> getAllOutgoingCategories() {
        Realm database = Realm.getDefaultInstance();
        RealmResults<category> outgoingCategories = database.where(category.class).equalTo("type", category.typeOfCategory.OUTGOING.ordinal()).findAll();

        Vector<String> result = new Vector<>();
        for (int i = 0; i < outgoingCategories.size(); i++) {
            category outgoingCategory = outgoingCategories.get(i);
            if (outgoingCategory != null) {
                result.add(outgoingCategory.getName());
            }
        }
        return result;
    }

    public static Vector<String> getAllIncomeCategories() {
        Realm database = Realm.getDefaultInstance();
        RealmResults<category> incomeCategories = database.where(category.class).equalTo("type", category.typeOfCategory.INCOME.ordinal()).findAll();

        Vector<String> result = new Vector<>();
        for (int i = 0; i < incomeCategories.size(); i++) {
            category incomeCategory = incomeCategories.get(i);
            if (incomeCategory != null) {
                result.add(incomeCategory.getName());
            }
        }
        return result;
    }

    public static Vector<String> getFunctioningCategories() {
        Vector<String> categories = new Vector<>();
        categories.addAll(getFunctioningOutgoingCategories());
        categories.addAll(getFunctioningIncomeCategories());
        return categories;
    }

    public static Vector<String> getNonFunctioningCategories() {
        Vector<String> categories = new Vector<>();
        categories.addAll(getNonFunctioningIncomeCategories());
        categories.addAll(getNonFunctioningOutgoingCategories());
        return categories;
    }

    public static Vector<String> getFunctioningOutgoingCategories() {
        Realm database = Realm.getDefaultInstance();
        RealmResults<category> outgoingCategories;
        outgoingCategories = database.where(category.class).equalTo("type", category.typeOfCategory.OUTGOING.ordinal()).equalTo("operative", true).findAll();

        Vector<String> result = new Vector<>();
        for (int i = 0; i < outgoingCategories.size(); i++) {
            category outgoingCategory = outgoingCategories.get(i);
            if (outgoingCategory != null) {
                result.add(outgoingCategory.getName());
            }
        }
        return result;
    }

    public static Vector<String> getFunctioningIncomeCategories() {
        Realm database = Realm.getDefaultInstance();
        RealmResults<category> incomeCategories;
        incomeCategories = database.where(category.class).equalTo("type", category.typeOfCategory.INCOME.ordinal()).equalTo("operative", true).findAll();

        Vector<String> result = new Vector<>();
        for (int i = 0; i < incomeCategories.size(); i++) {
            category incomeCategory = incomeCategories.get(i);
            if (incomeCategory != null) {
                result.add(incomeCategory.getName());
            }
        }
        return result;
    }

    public static Vector<String> getNonFunctioningOutgoingCategories() {
        Realm database = Realm.getDefaultInstance();
        RealmResults<category> outgoingCategories;
        outgoingCategories = database.where(category.class).equalTo("type", category.typeOfCategory.OUTGOING.ordinal()).equalTo("operative", false).findAll();

        Vector<String> result = new Vector<>();
        for (int i = 0; i < outgoingCategories.size(); i++) {
            category outgoingCategory = outgoingCategories.get(i);
            if (outgoingCategory != null) {
                result.add(outgoingCategory.getName());
            }
        }
        return result;
    }

    public static Vector<String> getNonFunctioningIncomeCategories() {
        Realm database = Realm.getDefaultInstance();
        RealmResults<category> incomeCategories;
        incomeCategories = database.where(category.class).equalTo("type", category.typeOfCategory.INCOME.ordinal()).equalTo("operative", false).findAll();

        Vector<String> result = new Vector<>();
        for (int i = 0; i < incomeCategories.size(); i++) {
            category incomeCategory = incomeCategories.get(i);
            if (incomeCategory != null) {
                result.add(incomeCategory.getName());
            }
        }
        return result;
    }
}
