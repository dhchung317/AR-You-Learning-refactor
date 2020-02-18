package com.capstone.aryoulearning.di;

import android.app.Application;

import androidx.fragment.app.Fragment;

import com.capstone.aryoulearning.BaseApplication;
import com.capstone.aryoulearning.db.ModelDatabaseModule;
import com.capstone.aryoulearning.di.main.MainFragmentBuildersModule;

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
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(BaseApplication baseApplication);
}
