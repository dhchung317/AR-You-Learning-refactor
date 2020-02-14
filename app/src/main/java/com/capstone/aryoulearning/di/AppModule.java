package com.capstone.aryoulearning.di;

import android.app.Application;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.capstone.aryoulearning.R;
import com.capstone.aryoulearning.util.Constants;


import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Provides
    static Retrofit provideRetrofitInstance(){
        return new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    @Provides
    static RequestOptions provideRequestOptions(){
        return RequestOptions.placeholderOf(R.drawable.error)
                .error(R.drawable.error);
    }
    @Provides
    static RequestManager provideGlideInstance(Application applicaiton, RequestOptions requestOptions){
        return Glide.with(applicaiton)
                .setDefaultRequestOptions(requestOptions);
    }

//    @Provides
//    static Drawable provideAppDrawable(Application application) {
//        return ContextCompat.getDrawable(application,R.drawable.arulearning);
//    }

}

