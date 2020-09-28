package com.example.newoneday.bomb.model;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by yongg on 2019/1/14.
 */

public class ClientApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}