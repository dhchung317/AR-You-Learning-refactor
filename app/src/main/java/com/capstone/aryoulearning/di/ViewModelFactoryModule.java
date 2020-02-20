package com.capstone.aryoulearning.di;

import androidx.lifecycle.ViewModelProvider;

import com.capstone.aryoulearning.viewmodel.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelProviderFactory(ViewModelProviderFactory viewModelProviderFactory);

}
