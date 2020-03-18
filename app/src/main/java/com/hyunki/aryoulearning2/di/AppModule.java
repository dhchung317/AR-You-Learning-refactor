package com.hyunki.aryoulearning2.di;

import android.app.Application;
import com.hyunki.aryoulearning2.network.main.MainApi;
import com.hyunki.aryoulearning2.util.Constants;
import com.hyunki.aryoulearning2.util.audio.PronunciationUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Provides
    @Singleton
    static Retrofit provideRetrofit() {
        return new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    @Provides
    @Singleton
    static MainApi provideMainApi(Retrofit retrofit) {
        return retrofit.create(MainApi.class);
    }

//    @Provides
//    @Singleton
//    static Application provideApplicationContext(Application application) {
//        return application;
//    }

    @Provides
    @Singleton
    static PronunciationUtil providePronunciationUtil(Application application) {
        return new PronunciationUtil(application.getBaseContext());
    }


}

