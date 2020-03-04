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
import com.hyunki.aryoulearning2.ui.main.ar.State;

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


    public MutableLiveData<State> getModelResponsesData() {
        return modelResponsesData;
    }

    private MutableLiveData<State> modelResponsesData = new MutableLiveData<>();
    private MutableLiveData<State> modelLiveData = new MutableLiveData<>();
    private MutableLiveData<State> catLiveData = new MutableLiveData<>();
    private MutableLiveData<State> curCatLiveData = new MutableLiveData<>();

    @Inject
    public MainViewModel(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    public void loadModelResponses() {

        modelResponsesData.setValue(State.Loading.INSTANCE);

        compositeDisposable.add(
                mainRepository.getModelResponses()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(modelResponses -> {
                            if (modelResponses.size() > 0) {
                                saveModelResponseData(modelResponses);
                                modelResponsesData.setValue(
                                        new State.Success.OnModelResponsesLoaded(modelResponses));
                            }

                        },throwable -> modelResponsesData.setValue(State.Error.INSTANCE))
        );
    }

    public void saveModelResponseData(ArrayList<ModelResponse> modelResponses) {
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

//        LiveData<MainResource<List<ModelResponse>>> data = LiveDataReactiveStreams.fromPublisher(
//                mainApi.getModels()
//                        .subscribeOn(Schedulers.io())
//                        .map(modelResponses -> {
//                            if (modelResponses.size() > 0) {
//                                if (modelResponses.get(0).getError() == -1) {
//                                    return MainResource.error("error", null);
//                                }
//                                for (int i = 0; i < modelResponses.size(); i++) {
//                                    mainRepository.insertCat(new Category(
//                                            modelResponses.get(i).getCategory(),
//                                            modelResponses.get(i).getBackground()
//                                    ));
//                                    for (int j = 0; j < modelResponses.get(i).getList().size(); j++) {
//                                        Log.d(TAG, "observeModelResponses: " + modelResponses.get(i).getList().get(j).getName());
//                                        mainRepository.insertModel(new Model(
//                                                modelResponses.get(i).getCategory(),
//                                                modelResponses.get(i).getList().get(j).getName(),
//                                                modelResponses.get(i).getList().get(j).getImage()
//                                        ));
//                                    }
//                                }
//                            }
//                            return MainResource.success(modelResponses);
//                        })
//        );
//        modelResponsesData.addSource(data, listResource -> {
//            modelResponsesData.setValue(MainResource.finished(listResource.data));
//            modelResponsesData.removeSource(data);
//        });
//        return modelResponsesData;


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

    public LiveData<State> getModelLiveData() {
        return modelLiveData;
    }

    public LiveData<State> getCatLiveData() {
        return catLiveData;
    }

    public LiveData<State> getCurCatLiveData() {
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

    private void onModelsFetched(ArrayList<Model> models) {
        Log.d(TAG, "onModelsFetched: " + models.size());
        modelLiveData.setValue(new State.Success.OnModelsLoaded(models));
    }

    private void onCatsFetched(ArrayList<Category> categories) {
        catLiveData.setValue(new State.Success.OnCategoriesLoaded(categories));
    }

    private void onCurCatsFetched(CurrentCategory category) {
        curCatLiveData.setValue(
                new State.Success.OnCurrentCategoryStringLoaded(category.getCurrentCategory()));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        clearEntireDatabase();
    }

}
