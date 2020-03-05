package com.hyunki.aryoulearning2.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hyunki.aryoulearning2.db.dao.CategoryDao;
import com.hyunki.aryoulearning2.db.dao.CurrentCategoryDao;
import com.hyunki.aryoulearning2.db.dao.ModelDao;
import com.hyunki.aryoulearning2.db.model.Category;
import com.hyunki.aryoulearning2.db.model.CurrentCategory;
import com.hyunki.aryoulearning2.model.Model;

@Database(entities = {Model.class, Category.class, CurrentCategory.class}, version = 3, exportSchema = false)
abstract class ModelDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "data.db";

    public abstract ModelDao modelDao();

    public abstract CategoryDao catDao();

    public abstract CurrentCategoryDao curCatDao();

}