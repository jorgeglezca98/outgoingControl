package com.example.jorgegonzalezcabrera.outgoing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.jorgegonzalezcabrera.outgoing.models.appConfiguration;
import io.realm.Realm;

public class SplashActiviy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.darker_gray));

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
