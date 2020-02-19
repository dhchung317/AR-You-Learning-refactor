package com.capstone.aryoulearning.di.main;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.capstone.aryoulearning.BaseApplication;
import com.capstone.aryoulearning.R;
import com.capstone.aryoulearning.ui.main.MainActivity;
import com.capstone.aryoulearning.ui.main.ar.ARHostFragmentX;
import com.capstone.aryoulearning.ui.main.hint.rv.HintAdapter;
import com.capstone.aryoulearning.ui.main.list.rv.ListAdapter;
import com.capstone.aryoulearning.network.main.MainApi;
import com.capstone.aryoulearning.util.audio.PronunciationUtil;
import com.google.ar.sceneform.ux.ArFragment;

import org.w3c.dom.Text;

import java.util.Locale;

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
    static ListAdapter provideCategoryAdapter(){
        return new ListAdapter();
    }

    @Provides
    static HintAdapter provideHintAdapter(PronunciationUtil pronunciationUtil){
        return new HintAdapter(pronunciationUtil);
    }



}
