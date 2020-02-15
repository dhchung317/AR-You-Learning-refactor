package com.capstone.aryoulearning.di;

import com.capstone.aryoulearning.di.main.MainFragmentBuildersModule;
import com.capstone.aryoulearning.di.main.MainModule;
import com.capstone.aryoulearning.di.main.MainViewModelsModule;
import com.capstone.aryoulearning.ui.main.MainActivity;

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
