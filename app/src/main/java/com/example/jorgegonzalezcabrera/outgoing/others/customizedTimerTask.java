package com.example.jorgegonzalezcabrera.outgoing.others;

import android.support.annotation.NonNull;

import com.example.jorgegonzalezcabrera.outgoing.fragments.mainFragment.OnNewEntryAddedInterface;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry.periodicType;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;

import javax.annotation.Nonnull;

import io.realm.Realm;
import io.realm.RealmResults;

public class customizedTimerTask extends TimerTask {

    private OnNewEntryAddedInterface entryAddedInterface;

    public customizedTimerTask(@Nonnull OnNewEntryAddedInterface entryAddedInterface) {
        this.entryAddedInterface = entryAddedInterface;
    }

    @Override
    public void run() {
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());

        RealmResults<periodicEntry> periodicEntries = Realm.getDefaultInstance().where(periodicEntry.class).findAll();
        periodicEntry periodicEntry;
        for (int i = 0; i < periodicEntries.size(); i++) {
            periodicEntry = periodicEntries.get(i);
            if (periodicEntry != null) {
                if (periodicEntry.getFrequency() == periodicType.WEEKLY) {
                    weeklyActions(periodicEntry, currentDate);
                } else if (periodicEntry.getFrequency() == periodicType.MONTHLY) {
                    monthlyActions(periodicEntry, currentDate);
                } else if (periodicEntry.getFrequency() == periodicType.ANNUAL) {
                    annualActions(periodicEntry, currentDate);
                }
            }
        }
    }

    private void annualActions(periodicEntry periodicEntry, GregorianCalendar currentDate) {
        int i = 0;
        while (i < periodicEntry.getSelectedDates().size()) {
            if (periodicEntry.getSelectedDates().get(i) == currentDate.get(Calendar.MONTH)) {
                createEntry(periodicEntry, entryAddedInterface, currentDate);
                break;
            }
            i++;
        }
    }

    private void monthlyActions(periodicEntry periodicEntry, GregorianCalendar currentDate) {
        int i = 0;
        while (i < periodicEntry.getSelectedDates().size()) {
            if (periodicEntry.getSelectedDates().get(i) == currentDate.get(Calendar.DAY_OF_MONTH)) {
                createEntry(periodicEntry, entryAddedInterface, currentDate);
                break;
            }
            i++;
        }
    }

    private void weeklyActions(periodicEntry periodicEntry, GregorianCalendar currentDate) {
        int i = 0;
        while (i < periodicEntry.getSelectedDates().size()) {
            if (periodicEntry.getSelectedDates().get(i) == currentDate.get(Calendar.DAY_OF_WEEK)) {
                createEntry(periodicEntry, entryAddedInterface, currentDate);
                break;
            }
            i++;
        }
    }

    public static void createEntry(final periodicEntry periodicEntry, OnNewEntryAddedInterface entryAddedInterface, final GregorianCalendar currentDate) {
        final entry newEntry = periodicEntry.getEntry();

        final Realm database = Realm.getDefaultInstance();
        final appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();

        if (newEntry.getType() == entry.type.OUTGOING) {
            currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() - newEntry.getValor());
        } else {
            currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() + newEntry.getValor());
        }

        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                database.copyToRealmOrUpdate(currentConfiguration);
                database.copyToRealm(newEntry);
                periodicEntry.setLastChange(currentDate.getTime());
                database.copyToRealmOrUpdate(periodicEntry);
            }
        });

        entryAddedInterface.OnNewEntryAdded(newEntry);
    }

}
