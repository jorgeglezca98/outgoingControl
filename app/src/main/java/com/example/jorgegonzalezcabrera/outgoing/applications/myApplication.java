package com.example.jorgegonzalezcabrera.outgoing.applications;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class myApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        getConfiguration();
        Realm.getDefaultInstance().beginTransaction();
        Realm.getDefaultInstance().deleteAll();
        Realm.getDefaultInstance().commitTransaction();
    }

    private void getConfiguration(){
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);
    }
}

//TODO: explore configuration possibilities