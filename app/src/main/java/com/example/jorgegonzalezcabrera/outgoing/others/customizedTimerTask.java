package com.example.jorgegonzalezcabrera.outgoing.others;

import com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.OnNewEntryAddedInterface;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry.periodicType;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;

import javax.annotation.Nonnull;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry.createEntry;

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
                    if (periodicEntry.getSelectedDate() == currentDate.get(Calendar.DAY_OF_WEEK)) {
                        createEntry(periodicEntry, entryAddedInterface, currentDate);
                    }
                } else if (periodicEntry.getFrequency() == periodicType.MONTHLY) {
                    if (periodicEntry.getSelectedDate() == currentDate.get(Calendar.DAY_OF_MONTH)) {
                        createEntry(periodicEntry, entryAddedInterface, currentDate);
                    }
                } else if (periodicEntry.getFrequency() == periodicType.ANNUAL) {
                    if (periodicEntry.getSelectedDate() == currentDate.get(Calendar.MONTH)) {
                        createEntry(periodicEntry, entryAddedInterface, currentDate);
                    }
                }
            }
        }
    }

}
