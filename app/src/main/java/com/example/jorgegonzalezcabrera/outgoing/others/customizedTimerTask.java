package com.example.jorgegonzalezcabrera.outgoing.others;

import com.example.jorgegonzalezcabrera.outgoing.fragments.mainFragment;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry.periodicType;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;

import io.realm.Realm;

public class customizedTimerTask extends TimerTask {

    private long periodicEntryId;
    private mainFragment mainFragment;

    public customizedTimerTask(long periodicEntryId, mainFragment mainFragment) {
        this.periodicEntryId = periodicEntryId;
        this.mainFragment = mainFragment;
    }

    @Override
    public void run() {
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());
        Realm database = Realm.getDefaultInstance();
        database.beginTransaction();
        periodicEntry periodicEntry = Realm.getDefaultInstance().where(periodicEntry.class).equalTo("id", periodicEntryId).findFirst();
        database.commitTransaction();
        if (periodicEntry != null) {
            if (periodicEntry.getFrequency() == periodicType.WEEKLY) {
                weeklyActions(periodicEntry,currentDate);
            } else if (periodicEntry.getFrequency() == periodicType.MONTHLY) {
                monthlyActions(periodicEntry,currentDate);
            } else if (periodicEntry.getFrequency() == periodicType.ANNUAL) {
                annualActions(periodicEntry,currentDate);
            }
        } else {
            cancel();
        }
    }

    private void annualActions(periodicEntry periodicEntry, GregorianCalendar currentDate) {
        int i=0;
        while(i<periodicEntry.getSelectedDates().size()){
            if(periodicEntry.getSelectedDates().get(i)==currentDate.get(Calendar.MONTH)){
                createEntry(periodicEntry);
                break;
            }
            i++;
        }
    }

    private void monthlyActions(periodicEntry periodicEntry, GregorianCalendar currentDate) {
        int i=0;
        while(i<periodicEntry.getSelectedDates().size()){
            if(periodicEntry.getSelectedDates().get(i)==currentDate.get(Calendar.DAY_OF_MONTH)){
                createEntry(periodicEntry);
                break;
            }
            i++;
        }
    }

    private void weeklyActions(periodicEntry periodicEntry, GregorianCalendar currentDate) {
        int i=0;
        while(i<periodicEntry.getSelectedDates().size()){
            if(periodicEntry.getSelectedDates().get(i)==currentDate.get(Calendar.DAY_OF_WEEK)){
                createEntry(periodicEntry);
                break;
            }
            i++;
        }
    }

    private void createEntry(periodicEntry periodicEntry) {
        entry newEntry = periodicEntry.getEntry();

        if (newEntry.getType() == entry.type.OUTGOING.ordinal()) {
            mainFragment.updateAfterOutgoing(newEntry);
        } else {
            mainFragment.updateAfterIncome(newEntry);
        }

        Realm.getDefaultInstance().beginTransaction();
        periodicEntry.setLastChange(new Date());
        Realm.getDefaultInstance().copyToRealmOrUpdate(periodicEntry);
        Realm.getDefaultInstance().commitTransaction();
    }
}
