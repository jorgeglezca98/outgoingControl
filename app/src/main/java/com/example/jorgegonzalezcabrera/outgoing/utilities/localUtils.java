package com.example.jorgegonzalezcabrera.outgoing.utilities;

import android.support.annotation.NonNull;

import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.entry.type;
import com.example.jorgegonzalezcabrera.outgoing.models.incomeCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.subcategory;

import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmList;

public class localUtils {

    public interface OnEntriesChangeInterface {
        void addEntry(@NonNull entry newEntry);

        void removeEntry(@NonNull entry removedEntry);

        void editEntry(@NonNull entry nextVersion);
    }

    public interface OnCategoriesChangeInterface {
        void removeAndReplaceCategory(@NonNull outgoingCategory removedOutgoingCategory, @NonNull String newCategory);

        void removeAndKeepCategory(@NonNull outgoingCategory removedOutgoingCategory);

        void removeAndReplaceCategory(@NonNull incomeCategory removedIncomeCategory, @NonNull String newCategory);

        void removeAndKeepCategory(@NonNull incomeCategory removedIncomeCategory);

        void addedCategory(@NonNull outgoingCategory newOutgoingCategory);

        void addedCategory(@NonNull incomeCategory newIncomeCategory);
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
        Vector<String> categories = new Vector<>();
        categories.addAll(getFunctioningOutgoingCategories());
        categories.addAll(getNonFunctioningOutgoingCategories());
        return categories;
    }

    public static Vector<String> getAllIncomeCategories() {
        Vector<String> categories = new Vector<>();
        categories.addAll(getFunctioningIncomeCategories());
        categories.addAll(getNonFunctioningIncomeCategories());
        return categories;
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
        appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();
        RealmList<outgoingCategory> outgoingCategories = currentConfiguration.getOutgoingCategories();

        Vector<String> categories = new Vector<>();
        for (int i = 0; i < outgoingCategories.size(); i++) {
            outgoingCategory outgoingCategory = outgoingCategories.get(i);
            if (outgoingCategory != null) {
                for (int j = 0; j < outgoingCategory.getSubcategories().size(); j++) {
                    subcategory subcategory = outgoingCategory.getSubcategories().get(j);
                    if (subcategory != null) {
                        categories.add(subcategory.getName());
                    }
                }
            }
        }
        return categories;
    }

    public static Vector<String> getFunctioningIncomeCategories() {
        Realm database = Realm.getDefaultInstance();
        appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();
        RealmList<incomeCategory> incomeCategories = currentConfiguration.getIncomeCategories();

        Vector<String> categories = new Vector<>();
        for (int i = 0; i < incomeCategories.size(); i++) {
            incomeCategory incomeCategory = incomeCategories.get(i);
            if (incomeCategory != null) {
                categories.add(incomeCategory.getName());
            }
        }

        return categories;
    }

    public static Vector<String> getNonFunctioningOutgoingCategories() {
        Realm database = Realm.getDefaultInstance();
        appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();
        RealmList<outgoingCategory> outgoingCategories = currentConfiguration.getRemovedOutgoingCategories();

        Vector<String> categories = new Vector<>();
        for (int i = 0; i < outgoingCategories.size(); i++) {
            outgoingCategory outgoingCategory = outgoingCategories.get(i);
            if (outgoingCategory != null) {
                for (int j = 0; j < outgoingCategory.getSubcategories().size(); j++) {
                    subcategory subcategory = outgoingCategory.getSubcategories().get(j);
                    if (subcategory != null) {
                        categories.add(subcategory.getName());
                    }
                }
            }
        }
        return categories;
    }

    public static Vector<String> getNonFunctioningIncomeCategories() {
        Realm database = Realm.getDefaultInstance();
        appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();
        RealmList<incomeCategory> incomeCategories = currentConfiguration.getRemovedIncomeCategories();

        Vector<String> categories = new Vector<>();
        for (int i = 0; i < incomeCategories.size(); i++) {
            incomeCategory incomeCategory = incomeCategories.get(i);
            if (incomeCategory != null) {
                categories.add(incomeCategory.getName());
            }
        }

        return categories;
    }
}
