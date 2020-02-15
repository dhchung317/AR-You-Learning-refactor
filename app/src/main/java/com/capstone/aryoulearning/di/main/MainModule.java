package com.capstone.aryoulearning.di.main;

import com.capstone.aryoulearning.R;
import com.capstone.aryoulearning.controller.CategoryAdapter;
import com.capstone.aryoulearning.network.main.MainApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

    @Provides
    static int provideThemeID() {
        return R.style.AppTheme;
    }

    @Provides
    static MainApi provideMainApi(Retrofit retrofit){
        return retrofit.create(MainApi.class);
    }

    @Provides
    static CategoryAdapter provideCategoryAdapter(){
        return new CategoryAdapter();
    }
}
