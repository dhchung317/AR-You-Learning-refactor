package com.hyunki.aryoulearning2.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hyunki.aryoulearning2.db.model.ModelInfo;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ModelInfoDao {
    @Query("SELECT category, name, image FROM model_info")
    Single<List<ModelInfo>> getAllModelInfo();

    @Query("SELECT category, name, image FROM model_info WHERE category = :category")
    Single<List<ModelInfo>> getModelInfoByCat(String category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ModelInfo modelInfo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ModelInfo> modelInfos);

    @Query("DELETE FROM model_info")
    void deleteAll();
}
