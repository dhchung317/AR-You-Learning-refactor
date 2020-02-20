package com.capstone.aryoulearning.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.capstone.aryoulearning.db.dao.CategoryDao;
import com.capstone.aryoulearning.db.dao.CurrentCategoryDao;
import com.capstone.aryoulearning.db.dao.ModelDao;
import com.capstone.aryoulearning.db.model.Category;
import com.capstone.aryoulearning.db.model.CurrentCategory;
import com.capstone.aryoulearning.model.Model;

@Database(entities = {Model.class, Category.class, CurrentCategory.class}, version = 1, exportSchema = false)
abstract class ModelDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "data.db";

    public abstract ModelDao modelDao();
    public abstract CategoryDao catDao();
    public abstract CurrentCategoryDao curCatDao();

}