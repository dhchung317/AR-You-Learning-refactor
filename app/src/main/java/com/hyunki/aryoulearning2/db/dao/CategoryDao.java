package com.hyunki.aryoulearning2.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hyunki.aryoulearning2.db.model.Category;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category")
    Single<List<Category>> getAllCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Query("DELETE FROM category")
    void deleteAll();
}
