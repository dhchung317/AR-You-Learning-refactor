package com.capstone.aryoulearning.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.capstone.aryoulearning.db.dao.CategoryDao;
import com.capstone.aryoulearning.db.dao.CurrentCategoryDao;
import com.capstone.aryoulearning.db.model.Category;
import com.capstone.aryoulearning.db.model.CurrentCategory;
import com.capstone.aryoulearning.db.model.ModelInfo;
import com.capstone.aryoulearning.db.dao.ModelInfoDao;
import com.capstone.aryoulearning.model.Model;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class MainRepository {
    private final ModelInfoDao modelInfoDao;
    private final CategoryDao categoryDao;
    private final CurrentCategoryDao currentCategoryDao;

    @Inject
    public MainRepository(ModelInfoDao modelInfoDao, CategoryDao categoryDao, CurrentCategoryDao currentCategoryDao) {
        this.modelInfoDao = modelInfoDao;
        this.categoryDao = categoryDao;
        this.currentCategoryDao = currentCategoryDao;
    }

    public Single<List<ModelInfo>> getAllModelInfo() {
        return modelInfoDao.getAllModelInfo();
    }

    Single<List<ModelInfo>> getModelInfoByCat(String cat) {
//        final MutableLiveData<List<ModelInfo>> data = new MutableLiveData<>();
//
//        data.setValue(modelInfoDao.getModelInfoByCat(cat));
        return modelInfoDao.getModelInfoByCat(cat);
    }

    void insertModelInfo(ModelInfo modelInfo) {
        modelInfoDao.insert(modelInfo);
    }

    void insertAllModelInfos(List<ModelInfo> modelInfos) {
        modelInfoDao.insertAll(modelInfos);
    }

    Single<List<Category>> getAllCats() {
//        Single<List<Category>> list = categoryDao.getAllCategories();

        return categoryDao.getAllCategories();
    }

    void insertCat(Category category) {
        categoryDao.insert(category);
    }

    Single<CurrentCategory> getCurrentCategory() {
        return currentCategoryDao.getCurrentCategory();
    }

    void setCurrentCategory(CurrentCategory category) {
        currentCategoryDao.insert(category);
    }

    void clearEntireDatabase() {
        modelInfoDao.deleteAll();
        categoryDao.deleteAll();
        currentCategoryDao.deleteAll();
    }

}
