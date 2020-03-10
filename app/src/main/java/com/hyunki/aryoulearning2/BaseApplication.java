package com.hyunki.aryoulearning2;

import android.app.Application;

import com.hyunki.aryoulearning2.di.AppComponent;
import com.hyunki.aryoulearning2.di.DaggerAppComponent;

public class BaseApplication extends Application {
    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().application(this).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
