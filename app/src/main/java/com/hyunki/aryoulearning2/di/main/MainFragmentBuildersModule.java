package com.hyunki.aryoulearning2.di.main;

import com.hyunki.aryoulearning2.ui.main.ar.ArHostFragment;
import com.hyunki.aryoulearning2.ui.main.hint.HintFragment;
import com.hyunki.aryoulearning2.ui.main.list.ListFragment;
import com.hyunki.aryoulearning2.ui.main.needsrefactor.ReplayFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ListFragment contributeListFragment();

    @ContributesAndroidInjector
    abstract HintFragment contributeHintFragment();

    @ContributesAndroidInjector
    abstract ArHostFragment contributeArHostFragment();

    @ContributesAndroidInjector
    abstract ReplayFragment contributeReplayFragment();
}
