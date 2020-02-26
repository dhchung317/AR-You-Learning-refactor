package com.hyunki.aryoulearning2.di;

import androidx.lifecycle.ViewModelProvider;

import com.hyunki.aryoulearning2.viewmodel.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelProviderFactory(ViewModelProviderFactory viewModelProviderFactory);

}
