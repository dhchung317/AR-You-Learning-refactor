package com.hyunki.aryoulearning2.di.main;

import com.hyunki.aryoulearning2.R;
import com.hyunki.aryoulearning2.animation.LottieHelper;
import com.hyunki.aryoulearning2.network.main.MainApi;
import com.hyunki.aryoulearning2.ui.main.hint.rv.HintAdapter;
import com.hyunki.aryoulearning2.ui.main.list.rv.ListAdapter;
import com.hyunki.aryoulearning2.util.audio.PronunciationUtil;

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
    static ListAdapter provideCategoryAdapter() {
        return new ListAdapter();
    }

    @Provides
    static HintAdapter provideHintAdapter(PronunciationUtil pronunciationUtil) {
        return new HintAdapter(pronunciationUtil);
    }

    @Provides
    static LottieHelper provideLottieHelper() {
        return new LottieHelper();
    }
}
