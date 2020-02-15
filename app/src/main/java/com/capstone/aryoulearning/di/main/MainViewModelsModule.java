package com.capstone.aryoulearning.di.main;

import androidx.lifecycle.ViewModel;

import com.capstone.aryoulearning.di.ViewModelKey;
import com.capstone.aryoulearning.ui.main.MainViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel viewModel);
}
