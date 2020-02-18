package com.capstone.aryoulearning.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.capstone.aryoulearning.db.model.Category;
import com.capstone.aryoulearning.db.model.CurrentCategory;
import com.capstone.aryoulearning.db.model.ModelInfo;
import com.capstone.aryoulearning.model.Model;
import com.capstone.aryoulearning.model.ModelResponse;
import com.capstone.aryoulearning.network.main.MainApi;
import com.capstone.aryoulearning.network.main.MainResource;

import java.util.ArrayList;
import java.util.List;
//import java.util.Observable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class MainViewModel extends ViewModel {
    public static final String TAG = "MainViewModel";
    private final MainRepository mainRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MainApi mainApi;

    private String currentCategory;

    private MediatorLiveData<MainResource<List<ModelResponse>>> modelResponsesData = new MediatorLiveData<>();

    private MutableLiveData<List<ModelInfo>> modelInfoLiveData = new MediatorLiveData<>();
    private MutableLiveData<List<Category>> catLiveData = new MutableLiveData<>();
    private MutableLiveData<String> curCatLiveData = new MutableLiveData<>();

    private MutableLiveData<List<Model>> convertModelLiveData = new MutableLiveData<>();

//    private Map<String,List<Model>> catMap = new HashMap<>();

    private List<ModelInfo> modelList = new ArrayList<>();

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
                        .map((Function<List<ModelResponse>, MainResource<List<ModelResponse>>>) modelResponses -> {

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
                                        modelList.add(new ModelInfo(
                                                modelResponses.get(i).getCategory(),
                                                modelResponses.get(i).getList().get(j).getName(),
                                                modelResponses.get(i).getList().get(j).getImage()
                                        ));
                                    }
                                }
                            }


                            return MainResource.success(modelResponses);
                        })


//                            .onErrorReturn(throwable -> {
//                                Log.e(TAG, "apply: ", throwable);
//                                ModelResponse modelResponse = new ModelResponse(null);
//                                modelResponse.setError(-1);
//
//                                ArrayList<ModelResponse> modelResponses = new ArrayList<>();
//                                modelResponses.add(modelResponse);
//                                return modelResponses;
//                            })

        );

        modelResponsesData.addSource(data, listResource -> {
//            Log.d(TAG, "observeModelResponses: reached" + listResource.data.size());
            mainRepository.insertAllModelInfos(modelList);

            for (int i = 0; i < modelList.size(); i++) {
                Log.d(TAG, "addsource: " + modelList.get(i).getName());
            }
            modelResponsesData.setValue(MainResource.finished(listResource.data));
            modelResponsesData.removeSource(data);

//
        });

        return modelResponsesData;
    }

//    public List<ModelResponse> getModelResponses() {
//        return modelResponsesData.getValue().data;
//    }

//    public List<ModelInfo> getModelInfoByCat(String cat){
//        return mainRepository.getModelInfoByCat(cat);
//    }

//    public void loadModelInfo() {
//        Disposable favoriteShowsDisposable = mainRepository.getAllModelInfo()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//
//                .subscribe(this::onModelsFetched, this::onError);
//        compositeDisposable.add(favoriteShowsDisposable);
//    }

    public void convertModelInfoToModels(List<ModelInfo> modelList) {

        Disposable modelDisposable = Observable.just(modelList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(info -> info)
                .flatMap(this::getModelObservable)
                .toList()
                .subscribe(this::onModelInfosConverted);
        compositeDisposable.add(modelDisposable);

    }

    public void loadModelInfoByCat(String cat) {
//        modelInfoLiveData.addSource(
//                mainRepository.getModelInfoByCat(cat), modelInfos -> {
//            modelInfoLiveData.setValue(modelInfos);
//        });
//        ArrayList<Model> returnModels = new ArrayList<>();
        Disposable modelInfoDisposable =
                mainRepository.getModelInfoByCat(cat)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
//                .toObservable()
//                .flatMapIterable(modelInfo -> modelInfo)
//                .flatMap(this::getModelObservable)
//                .toList()
                        .subscribe(this::onModelInfosFetched, this::onError);
        compositeDisposable.add(modelInfoDisposable);
    }

//    public void loadModelInfo(){
//        Disposable modelInfoDisposable = mainRepository.getAllModelInfo()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
////                .toObservable()
////                .flatMapIterable(modelInfo -> modelInfo)
////                .flatMap(this::getModelObservable)
////                .toList()
//                .subscribe(this::onModelInfosFetched, this::onError);
//        compositeDisposable.add(modelInfoDisposable);
//    }

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

    public MutableLiveData<List<ModelInfo>> getModelInfoLiveData() {
        return modelInfoLiveData;
    }

    public MutableLiveData<List<Model>> getConvertedModelInfoLiveData() {
        return convertModelLiveData;
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
//
//    public String getCurrentCategory() {
//        return this.currentCategory;
//    }

    public void clearEntireDatabase() {
        mainRepository.clearEntireDatabase();
    }

//    public void setCatMap(List<ModelResponse> modelResponses){
//
//        for (int i = 0; i < modelResponses.size(); i++) {
//
//            Log.d(TAG, "setCatMap: " + currentCategory);
//            Log.d(TAG, "setCatMap: " + modelResponses.get(i).getCategory());
//
//            Log.d(TAG, "setCatMap: " + modelResponses.get(i).getList().get(0).getName());
//
//            catMap.put(modelResponses.get(i).getCategory(), modelResponses.get(i).getList());
//        }
//
//    }

//    public List<Model> getListByCat(String cat){
//        return catMap.get(cat);
//    }

//    public MediatorLiveData<MainResource<List<ModelResponse>>> getModelResponsesData() {
//        return modelResponsesData;
//    }

    private void onError(Throwable throwable) {
        Log.d("MainViewModel", throwable.getMessage());
    }

    private void onModelInfosFetched(List<ModelInfo> modelInfos) {
        Log.d(TAG, "onModelsFetched: " + modelInfos.size());
        modelInfoLiveData.setValue(modelInfos);
    }

    private void onModelInfosConverted(List<Model> models) {
        Log.d(TAG, "onModelsFetched: " + models.size());
        convertModelLiveData.setValue(models);
    }

    private void onCatsFetched(List<Category> categories) {
        catLiveData.setValue(categories);
    }

    private void onCurCatsFetched(CurrentCategory category) {
        curCatLiveData.setValue(category.getCurrentCategory());
    }

    private Observable<Model> getModelObservable(ModelInfo modelInfo) {
        Model model = new Model(modelInfo.getName(), modelInfo.getImage());

        return Observable.just(model);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
//        clearEntireDatabase();
    }

}
