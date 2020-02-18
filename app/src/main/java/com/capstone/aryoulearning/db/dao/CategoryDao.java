package com.capstone.aryoulearning.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.capstone.aryoulearning.db.model.Category;
import com.capstone.aryoulearning.db.model.ModelInfo;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category")
    Single<List<Category>> getAllCategories();

//    @Query("SELECT * FROM model_info WHERE category = :cat")
//    Single<List<ModelInfo>> getModelInfoByCat(String cat);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Query("DELETE FROM category")
    void deleteAll();
}
