package com.hyunki.aryoulearning2.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hyunki.aryoulearning2.model.Model;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

@Dao
public interface ModelDao {

//    @Query("SELECT name, image FROM models")
//    Single<List<Model>> getAllModels();

    @Query("SELECT name, image FROM models WHERE category = :category")
    Single<List<Model>> getModelsByCat(String category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Model model);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAll(List<Model> models);

    @Query("DELETE FROM models")
    void deleteAll();

}
