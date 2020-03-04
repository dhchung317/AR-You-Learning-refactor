package com.hyunki.aryoulearning2.network;

import com.hyunki.aryoulearning2.network.main.MainApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitSingleton {
    private static final String BASEURL = "https://gist.githubusercontent.com/";
    private static Retrofit instance;


    private static Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return instance;
    }

    private RetrofitSingleton() {
    }

    public static MainApi getService() {
        return getInstance().create(MainApi.class);
    }
}
