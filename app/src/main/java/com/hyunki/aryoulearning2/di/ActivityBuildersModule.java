package com.hyunki.aryoulearning2.di;

import com.hyunki.aryoulearning2.di.main.MainFragmentBuildersModule;
import com.hyunki.aryoulearning2.di.main.MainModule;
import com.hyunki.aryoulearning2.di.main.MainViewModelsModule;
import com.hyunki.aryoulearning2.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
            modules = {
                    MainModule.class,
                    MainViewModelsModule.class,
                    MainFragmentBuildersModule.class
            }
    )
    abstract MainActivity contributeMainActivity();
}
