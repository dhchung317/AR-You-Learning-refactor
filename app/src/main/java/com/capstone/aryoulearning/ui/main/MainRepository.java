package com.capstone.aryoulearning.ui.main;

import com.capstone.aryoulearning.db.dao.CategoryDao;
import com.capstone.aryoulearning.db.dao.CurrentCategoryDao;
import com.capstone.aryoulearning.db.dao.ModelDao;
import com.capstone.aryoulearning.db.model.Category;
import com.capstone.aryoulearning.db.model.CurrentCategory;
import com.capstone.aryoulearning.model.Model;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class MainRepository {
    private final ModelDao modelDao;
    private final CategoryDao categoryDao;
    private final CurrentCategoryDao currentCategoryDao;

    @Inject
    public MainRepository(ModelDao modelDao, CategoryDao categoryDao, CurrentCategoryDao currentCategoryDao) {
        this.modelDao = modelDao;
        this.categoryDao = categoryDao;
        this.currentCategoryDao = currentCategoryDao;
    }

    public Single<List<Model>> getAllModels() {
        return modelDao.getAllModels();
    }

    public Single<List<Model>> getModelsByCat(String cat) {
        return modelDao.getModelsByCat(cat);
    }

    void insertModel(Model model) {
        modelDao.insert(model);
    }
//
    void insertAllModels(List<Model> models) {
        modelDao.insertAll(models);
    }

    Single<List<Category>> getAllCats() {
        return categoryDao.getAllCategories();
    }

    void insertCat(Category category) {
        categoryDao.insert(category);
    }

    public Single<CurrentCategory> getCurrentCategory() {
        return currentCategoryDao.getCurrentCategory();
    }

    void setCurrentCategory(CurrentCategory category) {
        currentCategoryDao.insert(category);
    }

    void clearEntireDatabase() {
        modelDao.deleteAll();
        categoryDao.deleteAll();
        currentCategoryDao.deleteAll();
    }

}
