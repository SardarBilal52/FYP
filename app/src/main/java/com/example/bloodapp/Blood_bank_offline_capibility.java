package com.example.bloodapp;


import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Blood_bank_offline_capibility extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
