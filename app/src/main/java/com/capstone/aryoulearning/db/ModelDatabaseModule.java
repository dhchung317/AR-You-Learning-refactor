package com.capstone.aryoulearning.db;

import android.content.Context;

import androidx.room.Room;

import com.capstone.aryoulearning.db.dao.CategoryDao;
import com.capstone.aryoulearning.db.dao.CurrentCategoryDao;
import com.capstone.aryoulearning.db.dao.ModelDao;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelDatabaseModule {

    @Provides
    static ModelDatabase provideModelDatabase(Context context){
        return Room.databaseBuilder(
                context,
                ModelDatabase.class, ModelDatabase.DATABASE_NAME
        )
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
