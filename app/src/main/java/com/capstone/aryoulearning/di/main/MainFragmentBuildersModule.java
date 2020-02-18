package com.capstone.aryoulearning.di.main;

import com.capstone.aryoulearning.ui.main.ar.ARFragment;
import com.capstone.aryoulearning.ui.main.ar.ARHostFragmentX;
import com.capstone.aryoulearning.ui.main.hint.HintFragment;
import com.capstone.aryoulearning.ui.main.list.ListFragment;
import com.capstone.aryoulearning.util.audio.PronunciationUtil;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ListFragment contributeListFragment();

    @ContributesAndroidInjector
    abstract HintFragment contributeHintFragment();

    @ContributesAndroidInjector
    abstract ARFragment contributeARFragment();

    @ContributesAndroidInjector
    abstract ARHostFragmentX contributeAXFragment();
}
