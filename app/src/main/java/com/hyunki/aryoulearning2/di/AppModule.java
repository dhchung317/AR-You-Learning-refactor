package com.hyunki.aryoulearning2.di;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.hyunki.aryoulearning2.R;
import com.hyunki.aryoulearning2.util.Constants;
import com.hyunki.aryoulearning2.util.audio.PronunciationUtil;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Provides
    static Retrofit provideRetrofitInstance() {
        return new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    @Provides
    static RequestOptions provideRequestOptions() {
        return RequestOptions.placeholderOf(R.drawable.error)
                .error(R.drawable.error);
    }

    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions) {
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }

    @Provides
    static Context provideApplicationContext(Application application) {
        return application.getBaseContext();
    }

    @Provides
    static PronunciationUtil providePronunciationUtil(Application application) {
        return new PronunciationUtil(application.getBaseContext());
    }

}

