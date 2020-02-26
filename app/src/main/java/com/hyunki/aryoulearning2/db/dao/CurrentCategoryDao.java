package com.hyunki.aryoulearning2.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hyunki.aryoulearning2.db.model.CurrentCategory;

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
