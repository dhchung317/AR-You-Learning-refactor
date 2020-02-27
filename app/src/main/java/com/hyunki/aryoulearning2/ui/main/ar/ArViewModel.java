package com.hyunki.aryoulearning2.ui.main.ar;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyunki.aryoulearning2.model.Model;
import com.hyunki.aryoulearning2.ui.main.MainRepository;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.hyunki.aryoulearning2.ui.main.ar.util.GameCommandListener;
import com.hyunki.aryoulearning2.ui.main.ar.util.GameManager;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class ArViewModel extends ViewModel {
    public static final String TAG = "ArViewModel";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Application application;
    private final MainRepository mainRepository;

    private MutableLiveData<List<Model>> modelLiveData = new MutableLiveData<>();

    private MutableLiveData<HashMap<String, CompletableFuture<ModelRenderable>>> futureModelMapLiveData = new MutableLiveData<>();
    private MutableLiveData<HashMap<String, CompletableFuture<ModelRenderable>>> futureLetterMapLiveData = new MutableLiveData<>();

    private MutableLiveData<List<HashMap<String, ModelRenderable>>> modelMapLiveData = new MutableLiveData<>();
    private MutableLiveData<HashMap<String, ModelRenderable>> letterMapLiveData = new MutableLiveData<>();

    @Inject
    public ArViewModel(Application application, MainRepository mainRepository) {
        System.out.println("arviewmodel created");
        this.application = application;
        this.mainRepository = mainRepository;
    }

    private void onModelsFetched(List<Model> models) {
        Log.d(TAG, "onModelsFetched: " + models.size());
        modelLiveData.setValue(models);
    }

    public MutableLiveData<List<Model>> getModelLiveData() {
        return modelLiveData;
    }

    public void loadModels() {
        Disposable catDisposable =
                mainRepository.getCurrentCategory().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(currentCategory -> {
                            Log.d(TAG, "loadModels: " + currentCategory.getCurrentCategory());
                            Disposable modelDisposable =
                                    mainRepository.getModelsByCat(currentCategory.getCurrentCategory())
                                            .subscribe(this::onModelsFetched, this::onError);
                            compositeDisposable.add(modelDisposable);
                        });
        compositeDisposable.add(catDisposable);
    }

    public void setMapOfFutureModels(List<Model> modelList) {
//        List<HashMap<String, CompletableFuture<ModelRenderable>>> returnFutureModelMapList = new ArrayList<>();
        HashMap<String, CompletableFuture<ModelRenderable>> returnMap = new HashMap<>();

        Log.d(TAG, "setMapOfFutureModels: " + modelList.size());
        for (int i = 0; i < modelList.size(); i++) {
            returnMap.put(modelList.get(i).getName(),
                    ModelRenderable.builder().
                            setSource(application, Uri.parse(modelList.get(i).getName() + ".sfb")).build());
        }

        futureModelMapLiveData.setValue(returnMap);
    }

    public void setMapOfFutureLetters(HashMap<String, CompletableFuture<ModelRenderable>> futureMap) {
        HashMap<String, CompletableFuture<ModelRenderable>> returnMap = new HashMap<>();
        for (Map.Entry<String, CompletableFuture<ModelRenderable>> e : futureMap.entrySet()) {

            String modelName = e.getKey();
            Log.d(TAG, "setMapOfFutureLetters: " + e.getKey());
            for (int j = 0; j < modelName.length(); j++) {
                returnMap.put(Character.toString(modelName.charAt(j)), ModelRenderable.builder().
                        setSource(application, Uri.parse(modelName.charAt(j) + ".sfb")).build());
            }
        }

        futureLetterMapLiveData.setValue(returnMap);
    }

    public void setLetterRenderables(HashMap<String, CompletableFuture<ModelRenderable>> futureLetterMap) {
        HashMap<String, ModelRenderable> returnMap = new HashMap<>();
        Log.d(TAG, "setLetterRenderables: " + futureLetterMap.entrySet().size());

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
                                    Log.d(TAG, "setLetterRenderables trying: " + e.getKey());
                                    returnMap.put(e.getKey(), e.getValue().get());
                                } catch (InterruptedException | ExecutionException ex) {
                                }
                                return null;
                            });
        }

        Log.d(TAG, "setLetterRenderables: " + returnMap.size());
        letterMapLiveData.setValue(returnMap);
    }

    public void setModelRenderables(HashMap<String, CompletableFuture<ModelRenderable>> futureModelMap) {
//        List<HashMap<String, ModelRenderable>> mapList = new ArrayList<>();
        Log.d(TAG, "setModelRenderables: " + futureModelMap.entrySet().size());


        Disposable mapDisposable =
                Observable.just(futureModelMap.entrySet())
                        .flatMap(entries -> {
                            HashMap<String, ModelRenderable> map = new HashMap<>();
                            for(Map.Entry<String,CompletableFuture<ModelRenderable>> e : entries){

                                CompletableFuture.allOf(e.getValue())
                                        .handle(
                                                (notUsed, throwable) -> {
                                                    if (throwable != null) {
                                                        return null;
                                                    }
                                                    try {
                                                        Log.d(TAG, "setModelRenderables: key from try:" + e.getKey());
                                                        map.put(e.getKey(), e.getValue().get());
                                                    } catch (ExecutionException ex) {
                                                        ex.printStackTrace();
                                                    } catch (InterruptedException ex) {
                                                        ex.printStackTrace();
                                                    }
                                                    return null;
                                                });
                            }
                            return Observable.just(map);
                        }).toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(hashMaps -> {
                            modelMapLiveData.setValue(hashMaps);
                        });

        compositeDisposable.add(mapDisposable);

    }

    public LiveData<HashMap<String, CompletableFuture<ModelRenderable>>> getFutureModelMap() {
        return futureModelMapLiveData;
    }

    public LiveData<HashMap<String, CompletableFuture<ModelRenderable>>> getFutureLetterMap() {
        return futureLetterMapLiveData;
    }

    public LiveData<List<HashMap<String, ModelRenderable>>> getModelMap() {
//        Log.d(TAG, "getModelMapList: " + modelMap.getValue().size());
        return modelMapLiveData;
    }

    public LiveData<HashMap<String, ModelRenderable>> getLetterMap() {
        return letterMapLiveData;
    }

    private void onError(Throwable throwable) {
        Log.d("MainViewModel", throwable.getMessage());
    }
}
