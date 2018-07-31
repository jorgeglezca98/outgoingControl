package com.example.jorgegonzalezcabrera.outgoing.others;

import com.example.jorgegonzalezcabrera.outgoing.fragments.mainFragment;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry.periodicType;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils.getTypeFromOrdinal;

public class customizedTimerTask extends TimerTask {

    private mainFragment.OnNewEntryAddedInterface entryAddedInterface;

    public customizedTimerTask(mainFragment.OnNewEntryAddedInterface entryAddedInterface) {
        this.entryAddedInterface = entryAddedInterface;
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
                createEntry(periodicEntry,entryAddedInterface);
                break;
            }
            i++;
        }
    }

    private void monthlyActions(periodicEntry periodicEntry, GregorianCalendar currentDate) {
        int i=0;
        while(i<periodicEntry.getSelectedDates().size()){
            if(periodicEntry.getSelectedDates().get(i)==currentDate.get(Calendar.DAY_OF_MONTH)){
                createEntry(periodicEntry,entryAddedInterface);
                break;
            }
            i++;
        }
    }

    private void weeklyActions(periodicEntry periodicEntry, GregorianCalendar currentDate) {
        int i=0;
        while(i<periodicEntry.getSelectedDates().size()){
            if(periodicEntry.getSelectedDates().get(i)==currentDate.get(Calendar.DAY_OF_WEEK)){
                createEntry(periodicEntry,entryAddedInterface);
                break;
            }
            i++;
        }
    }

    public static void createEntry(final periodicEntry periodicEntry, mainFragment.OnNewEntryAddedInterface entryAddedInterface) {
        final entry newEntry = periodicEntry.getEntry();

        final Realm database = Realm.getDefaultInstance();
        final appConfiguration currentConfiguration = database.where(appConfiguration.class).findFirst();

        if (newEntry.getType() == entry.type.OUTGOING) {
            currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() - newEntry.getValor());
        } else {
            currentConfiguration.setCurrentMoney(currentConfiguration.getCurrentMoney() + newEntry.getValor());
        }

        final GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());
        currentDate.set(currentDate.get(Calendar.YEAR),currentDate.get(Calendar.MONTH),currentDate.get(Calendar.DAY_OF_MONTH),0,0,0);

        database.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                database.copyToRealmOrUpdate(currentConfiguration);
                database.copyToRealm(newEntry);
                periodicEntry.setLastChange(currentDate.getTime());
                database.copyToRealmOrUpdate(periodicEntry);
            }
        });

        entryAddedInterface.OnNewEntryAdded(newEntry);
    }

}
