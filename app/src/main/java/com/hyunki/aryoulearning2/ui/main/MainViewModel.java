package com.hyunki.aryoulearning2.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyunki.aryoulearning2.db.model.Category;
import com.hyunki.aryoulearning2.db.model.CurrentCategory;
import com.hyunki.aryoulearning2.model.Model;
import com.hyunki.aryoulearning2.model.ModelResponse;
import com.hyunki.aryoulearning2.network.main.MainApi;
import com.hyunki.aryoulearning2.network.main.MainResource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//import java.util.Observable;


public class MainViewModel extends ViewModel {
    public static final String TAG = "MainViewModel";

    private final MainRepository mainRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MainApi mainApi;

    private MediatorLiveData<MainResource<List<ModelResponse>>> modelResponsesData = new MediatorLiveData<>();

    private MutableLiveData<List<Model>> modelLiveData = new MediatorLiveData<>();
    private MutableLiveData<List<Category>> catLiveData = new MutableLiveData<>();
    private MutableLiveData<String> curCatLiveData = new MutableLiveData<>();

    @Inject
    public MainViewModel(MainApi mainApi, MainRepository mainRepository) {
        this.mainApi = mainApi;
        this.mainRepository = mainRepository;
    }

    public LiveData<MainResource<List<ModelResponse>>> observeModelResponses() {

        modelResponsesData.setValue(MainResource.loading(new ArrayList<>()));

        LiveData<MainResource<List<ModelResponse>>> data = LiveDataReactiveStreams.fromPublisher(
                mainApi.getModels()
                        .subscribeOn(Schedulers.io())
                        .map(modelResponses -> {
                            if (modelResponses.size() > 0) {
                                if (modelResponses.get(0).getError() == -1) {
                                    return MainResource.error("error", null);
                                }
                                for (int i = 0; i < modelResponses.size(); i++) {
                                    mainRepository.insertCat(new Category(
                                            modelResponses.get(i).getCategory(),
                                            modelResponses.get(i).getBackground()
                                    ));
                                    for (int j = 0; j < modelResponses.get(i).getList().size(); j++) {
                                        Log.d(TAG, "observeModelResponses: " + modelResponses.get(i).getList().get(j).getName());
                                        mainRepository.insertModel(new Model(
                                                modelResponses.get(i).getCategory(),
                                                modelResponses.get(i).getList().get(j).getName(),
                                                modelResponses.get(i).getList().get(j).getImage()
                                        ));
                                    }
                                }
                            }
                            return MainResource.success(modelResponses);
                        })
        );
        modelResponsesData.addSource(data, listResource -> {
            modelResponsesData.setValue(MainResource.finished(listResource.data));
            modelResponsesData.removeSource(data);
        });
        return modelResponsesData;
    }

    public void loadModelsByCat(String cat) {
        Disposable modelDisposable =
                mainRepository.getModelsByCat(cat)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onModelsFetched, this::onError);
        compositeDisposable.add(modelDisposable);
    }

    public void loadCategories() {
        Disposable catDisposable = mainRepository.getAllCats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCatsFetched, this::onError);
        compositeDisposable.add(catDisposable);
    }

    public void loadCurrentCategoryName() {
        Disposable curCatDisposable = mainRepository.getCurrentCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCurCatsFetched, this::onError);
        compositeDisposable.add(curCatDisposable);
    }

    public MutableLiveData<List<Model>> getModelLiveData() {
        return modelLiveData;
    }

    public MutableLiveData<List<Category>> getCatLiveData() {
        return catLiveData;
    }

    public MutableLiveData<String> getCurCatLiveData() {
        return curCatLiveData;
    }

    public void setCurrentCategory(String currentCategory) {
        mainRepository.setCurrentCategory(new CurrentCategory(currentCategory));
    }

    public void clearEntireDatabase() {
        mainRepository.clearEntireDatabase();
    }

    private void onError(Throwable throwable) {
        Log.d("MainViewModel", throwable.getMessage());
    }

    private void onModelsFetched(List<Model> models) {
        Log.d(TAG, "onModelsFetched: " + models.size());
        modelLiveData.setValue(models);
    }

    private void onCatsFetched(List<Category> categories) {
        catLiveData.setValue(categories);
    }

    private void onCurCatsFetched(CurrentCategory category) {
        curCatLiveData.setValue(category.getCurrentCategory());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        clearEntireDatabase();
    }

}
