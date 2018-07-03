package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;

import io.realm.Realm;

public class SplashActiviy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Realm database = Realm.getDefaultInstance();
        final Intent intent;

        database.beginTransaction();
        if (database.where(appConfiguration.class).findAll().size() == 0)
            intent = new Intent(SplashActiviy.this, initialConfigurationActivity.class);
        else
            intent = new Intent(SplashActiviy.this, MainActivity.class);
        database.commitTransaction();

        startActivity(intent);
        finish();
    }
}
