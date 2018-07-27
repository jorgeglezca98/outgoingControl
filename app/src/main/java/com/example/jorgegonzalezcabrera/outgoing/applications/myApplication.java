package com.example.jorgegonzalezcabrera.outgoing.applications;

import android.app.Application;

import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class myApplication extends Application{

    public static AtomicInteger periodicEntryId;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        getConfiguration();

        if(Realm.getDefaultInstance().where(periodicEntry.class).findAll().size()!=0){
            periodicEntryId = new AtomicInteger(Realm.getDefaultInstance().where(periodicEntry.class).max("id").intValue());
        } else{
            periodicEntryId.set(0);
        }
        //TODO: this condition can result in the malfunction of the app, it is necessary to avoid id repetitions after the removal of perodicEntry
    }

    private void getConfiguration(){
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }
}

//TODO: explore configuration possibilities