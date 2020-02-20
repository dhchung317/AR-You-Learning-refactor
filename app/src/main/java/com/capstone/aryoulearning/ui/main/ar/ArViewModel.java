package com.capstone.aryoulearning.ui.main.ar;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.capstone.aryoulearning.db.model.CurrentCategory;
import com.capstone.aryoulearning.model.Model;
import com.capstone.aryoulearning.ui.main.MainRepository;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArViewModel extends ViewModel {
    public static final String TAG = "ArViewModel";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Application application;
    private final MainRepository mainRepository;

    private MutableLiveData<List<Model>> modelLiveData = new MediatorLiveData<>();
    private MutableLiveData<String> curCatLiveData = new MutableLiveData<>();

    private boolean hasFinishedLoadingModels;
    private boolean hasFinishedLoadingLetters;

    @Inject
    public ArViewModel(Application application, MainRepository mainRepository) {
        System.out.println("arviewmodel created");
        this.application = application;
        this.mainRepository = mainRepository;
    }

    private List<Model> modelList;

    private List<HashMap<String, CompletableFuture<ModelRenderable>>> futureModelMapList = new ArrayList<>();
    private HashMap<String, CompletableFuture<ModelRenderable>> futureLetterMap = new HashMap<>();
    private List<HashMap<String, ModelRenderable>> modelMapList = new ArrayList<>();
    private HashMap<String, ModelRenderable> letterMap = new HashMap<>();

    private void setListMapsOfFutureModels(List<Model> modelList) {
        for (int i = 0; i < modelList.size(); i++) {
            HashMap<String, CompletableFuture<ModelRenderable>> futureMap = new HashMap();
            futureMap.put(modelList.get(i).getName(),
                    ModelRenderable.builder().
                            setSource(application, Uri.parse(modelList.get(i).getName() + ".sfb")).build());
            futureModelMapList.add(futureMap);
        }
    }

    private void setMapOfFutureLetters(List<HashMap<String, CompletableFuture<ModelRenderable>>> futureMapList) {
        for (int i = 0; i < futureMapList.size(); i++) {
            String modelName = futureMapList.get(i).keySet().toString();
            for (int j = 0; j < modelName.length(); j++) {
                futureLetterMap.put(Character.toString(modelName.charAt(j)), ModelRenderable.builder().
                        setSource(application, Uri.parse(modelName.charAt(j) + ".sfb")).build());
            }
        }
    }

    private void setModelRenderables(List<HashMap<String, CompletableFuture<ModelRenderable>>> futureModelMapList) {

        for (int i = 0; i < futureModelMapList.size(); i++) {

            for (Map.Entry<String, CompletableFuture<ModelRenderable>> e : futureModelMapList.get(i).entrySet()) {

                HashMap<String, ModelRenderable> modelMap = new HashMap<>();

                CompletableFuture.allOf(e.getValue())
                        .handle(
                                (notUsed, throwable) -> {
                                    // When you build a Renderable, Sceneform loads its resources in the background while
                                    // returning a CompletableFuture. Call handle(), thenAccept(), or check isDone()
                                    // before calling get().
                                    if (throwable != null) {
                                        return null;
                                    }
                                    try {
                                        modelMap.put(e.getKey(), e.getValue().get());
                                    } catch (InterruptedException | ExecutionException ex) {
                                    }
                                    return null;
                                });
                modelMapList.add(modelMap);
            }
        }
        hasFinishedLoadingModels = true;
    }

    private void setLetterRenderables(HashMap<String, CompletableFuture<ModelRenderable>> futureLetterMap) {
        for (Map.Entry<String, CompletableFuture<ModelRenderable>> e : futureLetterMap.entrySet()) {

            CompletableFuture.allOf(e.getValue())
                    .handle(
                            (notUsed, throwable) -> {
                                // When you build a Renderable, Sceneform loads its resources in the background while
                                // returning a CompletableFuture. Call handle(), thenAccept(), or check isDone()
                                // before calling get().
                                if (throwable != null) {
                                    return null;
                                }
                                try {
                                    letterMap.put(e.getKey(), e.getValue().get());
                                } catch (InterruptedException | ExecutionException ex) {
                                }
                                return null;
                            });
        }
        hasFinishedLoadingLetters = true;
    }

    public void loadModelsByCat(String cat) {
        Disposable modelDisposable =
                mainRepository.getModelsByCat(cat)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onModelsFetched, this::onError);
        compositeDisposable.add(modelDisposable);
    }

    public void loadCurrentCategoryName() {
        Disposable curCatDisposable = mainRepository.getCurrentCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onCurCatsFetched, this::onError);
        compositeDisposable.add(curCatDisposable);
    }

    private void onCurCatsFetched(CurrentCategory category) {
        curCatLiveData.setValue(category.getCurrentCategory());
    }

    private void onModelsFetched(List<Model> models) {
        Log.d(TAG, "onModelsFetched: " + models.size());

        modelList = models;

        setListMapsOfFutureModels(modelList);
        setMapOfFutureLetters(futureModelMapList);

        setModelRenderables(futureModelMapList);
        setLetterRenderables(futureLetterMap);

        modelLiveData.setValue(models);
    }

    public MutableLiveData<List<Model>> getModelLiveData() {
        return modelLiveData;
    }

    public MutableLiveData<String> getCurCatLiveData() {
        return curCatLiveData;
    }

    private void onError(Throwable throwable) {
        Log.d("MainViewModel", throwable.getMessage());
    }

    public List<Model> getModelList() {
        return modelList;
    }

    public List<HashMap<String, CompletableFuture<ModelRenderable>>> getFutureModelMapList() {
        return futureModelMapList;
    }

    public HashMap<String, CompletableFuture<ModelRenderable>> getFutureLetterMap() {
        return futureLetterMap;
    }

    public List<HashMap<String, ModelRenderable>> getModelMapList() {
        return modelMapList;
    }

    public HashMap<String, ModelRenderable> getLetterMap() {
        return letterMap;
    }
}
