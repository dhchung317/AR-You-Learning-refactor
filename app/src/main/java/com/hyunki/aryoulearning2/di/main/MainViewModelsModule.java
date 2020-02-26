package com.hyunki.aryoulearning2.di.main;

import androidx.lifecycle.ViewModel;

import com.hyunki.aryoulearning2.di.ViewModelKey;
import com.hyunki.aryoulearning2.ui.main.MainViewModel;
import com.hyunki.aryoulearning2.ui.main.ar.ArViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ArViewModel.class)
    public abstract ViewModel bindArViewModel(ArViewModel viewModel);
}
