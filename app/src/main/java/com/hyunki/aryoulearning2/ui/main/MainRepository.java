package com.hyunki.aryoulearning2.ui.main;

import com.hyunki.aryoulearning2.db.dao.CategoryDao;
import com.hyunki.aryoulearning2.db.dao.CurrentCategoryDao;
import com.hyunki.aryoulearning2.db.dao.ModelDao;
import com.hyunki.aryoulearning2.db.model.Category;
import com.hyunki.aryoulearning2.db.model.CurrentCategory;
import com.hyunki.aryoulearning2.model.Model;
import com.hyunki.aryoulearning2.model.ModelResponse;
import com.hyunki.aryoulearning2.network.RetrofitSingleton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;

@Singleton
public class MainRepository {
    private final ModelDao modelDao;
    private final CategoryDao categoryDao;
    private final CurrentCategoryDao currentCategoryDao;

    @Inject
    MainRepository(ModelDao modelDao, CategoryDao categoryDao, CurrentCategoryDao currentCategoryDao) {
        this.modelDao = modelDao;
        this.categoryDao = categoryDao;
        this.currentCategoryDao = currentCategoryDao;
    }

    public Single<List<Model>> getModelsByCat(String cat) {
        return modelDao.getModelsByCat(cat);
    }

    void insertModel(Model model) {
        modelDao.insert(model);
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

    public Observable<ArrayList<ModelResponse>> getModelResponses() {
        return RetrofitSingleton.getService().getModels();
    }

    void clearEntireDatabase() {
        modelDao.deleteAll();
        categoryDao.deleteAll();
        currentCategoryDao.deleteAll();
    }
}
