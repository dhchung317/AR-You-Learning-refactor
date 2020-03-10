package com.hyunki.aryoulearning2.di;

import android.app.Application;

import com.hyunki.aryoulearning2.BaseApplication;
import com.hyunki.aryoulearning2.db.ModelDatabaseModule;
import com.hyunki.aryoulearning2.di.main.MainFragmentBuildersModule;
import com.hyunki.aryoulearning2.di.main.MainModule;
import com.hyunki.aryoulearning2.di.main.MainViewModelsModule;
import com.hyunki.aryoulearning2.network.main.MainApi;
import com.hyunki.aryoulearning2.ui.main.MainActivity;
import com.hyunki.aryoulearning2.ui.main.MainRepository;
import com.hyunki.aryoulearning2.ui.main.ar.ArHostFragment;
import com.hyunki.aryoulearning2.ui.main.hint.HintFragment;
import com.hyunki.aryoulearning2.ui.main.list.ListFragment;
import com.hyunki.aryoulearning2.ui.main.replay.ReplayFragment;
import com.hyunki.aryoulearning2.ui.main.results.ResultsFragment;
import com.hyunki.aryoulearning2.ui.main.tutorial.TutorialFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import retrofit2.Retrofit;

@Component(
        modules = {
                AppModule.class,
                ViewModelFactoryModule.class,
                ModelDatabaseModule.class,
                MainModule.class,
                MainViewModelsModule.class,
        })
@Singleton
public interface AppComponent{

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    Retrofit retrofit();
    MainApi mainApi();
    MainRepository mainRepository();

    void inject(BaseApplication baseApplication);

    void inject(MainActivity mainActivity);

    void inject(ListFragment listFragment);
    void inject(ArHostFragment arHostFragment);
    void inject(ReplayFragment replayFragment);
    void inject(ResultsFragment resultsFragment);
    void inject(HintFragment hintFragment);
    void inject(TutorialFragment tutorialFragment);
}

//    @ContributesAndroidInjector
//    abstract ListFragment contributeListFragment();
//
//    @ContributesAndroidInjector
//    abstract HintFragment contributeHintFragment();
//
//    @ContributesAndroidInjector
//    abstract ArHostFragment contributeArHostFragment();
//
//    @ContributesAndroidInjector
//    abstract ReplayFragment contributeReplayFragment();
//
//    @ContributesAndroidInjector
//    abstract ResultsFragment contributeResultsFragment();
//
//    @ContributesAndroidInjector
//    abstract TutorialFragment contributeTutorialFragment();

//    abstract MainActivity contributeMainActivity();
