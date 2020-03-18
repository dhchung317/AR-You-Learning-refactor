package com.hyunki.aryoulearning2.db;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.hyunki.aryoulearning2.db.dao.CategoryDao;
import com.hyunki.aryoulearning2.db.dao.CurrentCategoryDao;
import com.hyunki.aryoulearning2.db.dao.ModelDao;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelDatabaseModule {

    @Provides
    static ModelDatabase provideModelDatabase(Application application) {
        return Room.databaseBuilder(
                application.getApplicationContext(),
                ModelDatabase.class, ModelDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    static ModelDao provideModelDao(ModelDatabase modelDatabase) {
        return modelDatabase.modelDao();
    }

    @Provides
    static CategoryDao provideCatDao(ModelDatabase modelDatabase) {
        return modelDatabase.catDao();
    }

    @Provides
    static CurrentCategoryDao provideCurCatDao(ModelDatabase modelDatabase) {
        return modelDatabase.curCatDao();
    }
}