package com.example.jorgegonzalezcabrera.outgoing.applications;

import android.app.Application;

import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class myApplication extends Application {

    public static AtomicInteger periodicEntryId;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

        Number higherId = Realm.getDefaultInstance().where(periodicEntry.class).max("id");
        periodicEntryId = new AtomicInteger((higherId == null) ? 0 : higherId.intValue());
    }
}
//TODO: explore configuration possibilities