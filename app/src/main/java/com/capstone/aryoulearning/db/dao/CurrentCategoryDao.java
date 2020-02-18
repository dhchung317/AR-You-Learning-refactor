package com.capstone.aryoulearning.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.capstone.aryoulearning.db.model.Category;
import com.capstone.aryoulearning.db.model.CurrentCategory;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface CurrentCategoryDao {
    @Query("SELECT * FROM current_category")
    Single<CurrentCategory> getCurrentCategory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CurrentCategory currentCategory);

    @Query("DELETE FROM current_category")
    void deleteAll();
}
