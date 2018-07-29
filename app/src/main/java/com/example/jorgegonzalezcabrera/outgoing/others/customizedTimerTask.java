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
import io.realm.RealmResults;

public class customizedTimerTask extends TimerTask {

    private mainFragment mainFragment; //Really weird

    public customizedTimerTask(mainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    @Override
    public void run() {
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());
        Realm database = Realm.getDefaultInstance();
        database.beginTransaction();
        RealmResults<periodicEntry> periodicEntries = Realm.getDefaultInstance().where(periodicEntry.class).findAll();
        database.commitTransaction();
        for(int i=0;i<periodicEntries.size();i++){
            if (periodicEntries.get(i).getFrequency() == periodicType.WEEKLY) {
                weeklyActions(periodicEntries.get(i),currentDate);
            } else if (periodicEntries.get(i).getFrequency() == periodicType.MONTHLY) {
                monthlyActions(periodicEntries.get(i),currentDate);
            } else if (periodicEntries.get(i).getFrequency() == periodicType.ANNUAL) {
                annualActions(periodicEntries.get(i),currentDate);
            }
        }
    }

    private void annualActions(periodicEntry periodicEntry, GregorianCalendar currentDate) {
        int i=0;
        while(i<periodicEntry.getSelectedDates().size()){
            if(periodicEntry.getSelectedDates().get(i)==currentDate.get(Calendar.MONTH)){
                createEntry(periodicEntry,mainFragment);
                break;
            }
            i++;
        }
    }

    private void monthlyActions(periodicEntry periodicEntry, GregorianCalendar currentDate) {
        int i=0;
        while(i<periodicEntry.getSelectedDates().size()){
            if(periodicEntry.getSelectedDates().get(i)==currentDate.get(Calendar.DAY_OF_MONTH)){
                createEntry(periodicEntry,mainFragment);
                break;
            }
            i++;
        }
    }

    private void weeklyActions(periodicEntry periodicEntry, GregorianCalendar currentDate) {
        int i=0;
        while(i<periodicEntry.getSelectedDates().size()){
            if(periodicEntry.getSelectedDates().get(i)==currentDate.get(Calendar.DAY_OF_WEEK)){
                createEntry(periodicEntry,mainFragment);
                break;
            }
            i++;
        }
    }

    public static void createEntry(periodicEntry periodicEntry, mainFragment fragment) {
        entry newEntry = periodicEntry.getEntry();

        if (newEntry.getType() == entry.type.OUTGOING) {
            fragment.updateAfterOutgoing(newEntry);
        } else {
            fragment.updateAfterIncome(newEntry);
        }

        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());
        currentDate.set(currentDate.get(Calendar.YEAR),currentDate.get(Calendar.MONTH),currentDate.get(Calendar.DAY_OF_MONTH),0,0,0);

        Realm.getDefaultInstance().beginTransaction();
        periodicEntry.setLastChange(currentDate.getTime());
        Realm.getDefaultInstance().copyToRealmOrUpdate(periodicEntry);
        Realm.getDefaultInstance().commitTransaction();
    }
}
