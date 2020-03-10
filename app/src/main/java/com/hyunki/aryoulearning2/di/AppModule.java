package com.hyunki.aryoulearning2.di;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.hyunki.aryoulearning2.R;
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

    //    @Provides
//    @Singleton
//    static Retrofit provideRetrofitInstance() {
//        return new Retrofit.Builder().baseUrl(Constants.BASE_URL)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create()).build();
//    }
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

    @Provides
    @Singleton
    static Context provideApplicationContext(Application application) {
        return application.getBaseContext();
    }

    @Provides
    @Singleton
    static PronunciationUtil providePronunciationUtil(Application application) {
        return new PronunciationUtil(application.getBaseContext());
    }


}

