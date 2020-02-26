package com.hyunki.aryoulearning2.di;

import android.app.Application;

import com.hyunki.aryoulearning2.BaseApplication;
import com.hyunki.aryoulearning2.db.ModelDatabaseModule;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ActivityBuildersModule.class,
                AppModule.class,
                ViewModelFactoryModule.class,
                ModelDatabaseModule.class

        })
public interface AppComponent extends AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(BaseApplication baseApplication);
}
