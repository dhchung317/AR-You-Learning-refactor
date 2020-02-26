package com.hyunki.aryoulearning2;

import android.app.Activity;
import android.app.Fragment;

import androidx.multidex.MultiDexApplication;

import com.hyunki.aryoulearning2.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasFragmentInjector;

public class BaseApplication extends MultiDexApplication implements HasActivityInjector, HasFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;


    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().application(this).build().inject(this);
    }

    // Dependency Injection
    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public DispatchingAndroidInjector<Fragment> fragmentInjector() {
        return fragmentInjector;
    }
}
