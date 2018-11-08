package com.example.jorgegonzalezcabrera.outgoing.utilities;

import android.support.annotation.NonNull;

import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.entry.type;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;

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

        void removeMoneyController(@NonNull outgoingCategory removedCategory);
    }

    public static type getTypeFromOrdinal(int ordinal) {
        return (ordinal == type.OUTGOING.ordinal()) ? type.OUTGOING : type.INCOME;
    }

    public static Vector<String> getAllCategories() {
        Vector<String> categories = new Vector<>();
        categories.addAll(getAllOutgoingCategories());
        categories.addAll(getAllIncomeCategories());
        return categories;
    }

    public static Vector<String> getAllOutgoingCategories() {
        Realm database = Realm.getDefaultInstance();
        RealmResults<category> outgoingCategories = database.where(category.class).equalTo("type", category.OUTGOING).findAll();

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
        RealmResults<category> incomeCategories = database.where(category.class).equalTo("type", category.INCOME).findAll();

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
        outgoingCategories = database.where(category.class).equalTo("type", category.OUTGOING).equalTo("operative", true).findAll();

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
        incomeCategories = database.where(category.class).equalTo("type", category.INCOME).equalTo("operative", true).findAll();

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
        outgoingCategories = database.where(category.class).equalTo("type", category.OUTGOING).equalTo("operative", false).findAll();

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
        incomeCategories = database.where(category.class).equalTo("type", category.INCOME).equalTo("operative", false).findAll();

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
