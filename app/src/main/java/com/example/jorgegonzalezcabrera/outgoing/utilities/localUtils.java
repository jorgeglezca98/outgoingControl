package com.example.jorgegonzalezcabrera.outgoing.utilities;

import android.support.annotation.NonNull;

import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.entry.type;
import com.example.jorgegonzalezcabrera.outgoing.models.incomeCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;
import com.example.jorgegonzalezcabrera.outgoing.models.subcategory;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class localUtils {

    public interface OnEntriesChangeInterface {
        void addEntry(@NonNull entry newEntry);

        void removeEntry(@NonNull entry removedEntry);

        void editEntry(@NonNull entry nextVersion);
    }


    public static type getTypeFromOrdinal(int ordinal) {
        return (ordinal == type.OUTGOING.ordinal()) ? type.OUTGOING : type.INCOME;
    }

    public static List<String> getCategories() {
        Realm database = Realm.getDefaultInstance();
        RealmResults<outgoingCategory> outgoingCategories = database.where(outgoingCategory.class).findAll();
        RealmResults<incomeCategory> incomeCategories = database.where(incomeCategory.class).findAll();

        List<String> categories = new ArrayList<>();
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
        for (int i = 0; i < incomeCategories.size(); i++) {
            incomeCategory incomeCategory = incomeCategories.get(i);
            if (incomeCategory != null) {
                categories.add(incomeCategory.getName());
            }
        }

        return categories;
    }
}
