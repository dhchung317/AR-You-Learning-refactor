package com.capstone.aryoulearning.di;

import android.app.Application;
import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.capstone.aryoulearning.BaseApplication;
import com.capstone.aryoulearning.R;
import com.capstone.aryoulearning.ui.main.MainActivity;
import com.capstone.aryoulearning.ui.main.ar.ARHostFragmentX;
import com.capstone.aryoulearning.util.audio.PronunciationUtil;
import com.capstone.aryoulearning.util.Constants;
import com.google.ar.sceneform.ux.ArFragment;


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
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions){
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }

    @Provides
    static Context provideApplicationContext(Application application) {
        return application.getBaseContext();
    }

//    @Provides
//    static Application provideApplication(Application application) {
//        return application;
//    }

    @Provides
    static PronunciationUtil providePronunciationUtil(Application application){
        return new PronunciationUtil(application.getBaseContext());
    }

//    @Provides
//    static ArFragment provideArFragment(MainActivity activity){
//        return (ArFragment)activity.getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
//    }

//    @Provides
//    static Drawable provideAppDrawable(Application application) {
//        return ContextCompat.getDrawable(application,R.drawable.arulearning);
//    }

}

