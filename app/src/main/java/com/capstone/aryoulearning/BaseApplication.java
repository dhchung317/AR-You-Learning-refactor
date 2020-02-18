package com.capstone.aryoulearning;

import android.app.Activity;
import android.app.Fragment;

import androidx.multidex.MultiDexApplication;

import com.capstone.aryoulearning.di.AppComponent;
import com.capstone.aryoulearning.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasFragmentInjector;
import dagger.android.support.AndroidSupportInjection;

public class BaseApplication extends MultiDexApplication implements HasActivityInjector, HasFragmentInjector {

//    @Override
//    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
//        return DaggerAppComponent.builder().application(this).build();
//    }

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;
    @Inject DispatchingAndroidInjector<Fragment> fragmentInjector;


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
