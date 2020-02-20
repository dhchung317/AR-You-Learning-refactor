package com.capstone.aryoulearning.ui.main.ar;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.airbnb.lottie.L;
import com.capstone.aryoulearning.db.model.CurrentCategory;
import com.capstone.aryoulearning.model.Model;
import com.capstone.aryoulearning.ui.main.MainRepository;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArViewModel extends ViewModel {
    public static final String TAG = "ArViewModel";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Application application;
    private final MainRepository mainRepository;

    private MutableLiveData<List<Model>> modelLiveData = new MutableLiveData<>();

    private MutableLiveData<List<HashMap<String, CompletableFuture<ModelRenderable>>>> futureModelMapList = new MutableLiveData<>();
    private MutableLiveData<HashMap<String, CompletableFuture<ModelRenderable>>> futureLetterMap = new MutableLiveData<>();

    private MutableLiveData<List<HashMap<String, ModelRenderable>>> modelMapList = new MutableLiveData<>();
    private MutableLiveData<HashMap<String, ModelRenderable>> letterMap = new MutableLiveData<>();

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
                            Disposable modelDisposable =
                                    mainRepository.getModelsByCat(currentCategory.getCurrentCategory())
                                    .subscribe(this::onModelsFetched, this::onError);
                            compositeDisposable.add(modelDisposable);
                        });
        compositeDisposable.add(catDisposable);

    }

    public void setListMapsOfFutureModels(List<Model> modelList) {

        List<HashMap<String,CompletableFuture<ModelRenderable>>> returnFutureModelMapList = new ArrayList<>();

        for (int i = 0; i < modelList.size(); i++) {
            HashMap<String, CompletableFuture<ModelRenderable>> futureMap = new HashMap();
            futureMap.put(modelList.get(i).getName(),
                    ModelRenderable.builder().
                            setSource(application, Uri.parse(modelList.get(i).getName() + ".sfb")).build());
            returnFutureModelMapList.add(futureMap);
        }

        futureModelMapList.setValue(returnFutureModelMapList);
    }

    public void setMapOfFutureLetters(List<HashMap<String, CompletableFuture<ModelRenderable>>> futureMapList) {

        HashMap<String,CompletableFuture<ModelRenderable>> returnMap = new HashMap<>();

        for (int i = 0; i < futureMapList.size(); i++) {

            String modelName = futureMapList.get(i).keySet().toString();
            for (int j = 0; j < modelName.length(); j++) {
                returnMap.put(Character.toString(modelName.charAt(j)), ModelRenderable.builder().
                        setSource(application, Uri.parse(modelName.charAt(j) + ".sfb")).build());
            }
        }
        futureLetterMap.setValue(returnMap);
    }

    public void setLetterRenderables(HashMap<String, CompletableFuture<ModelRenderable>> futureLetterMap) {
        HashMap<String,ModelRenderable> returnMap = new HashMap<>();

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
                                    returnMap.put(e.getKey(), e.getValue().get());
                                } catch (InterruptedException | ExecutionException ex) {
                                }
                                return null;
                            });
        }
        letterMap.setValue(returnMap);
    }

    public void setModelRenderables(List<HashMap<String, CompletableFuture<ModelRenderable>>> futureModelMapList) {
        List<HashMap<String, ModelRenderable>> returnList = new ArrayList<>();

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
               returnList.add(modelMap);
            }
        }
        modelMapList.setValue(returnList);
    }

    public MutableLiveData<List<HashMap<String, CompletableFuture<ModelRenderable>>>> getFutureModelMapList() {
        return futureModelMapList;
    }

    public MutableLiveData<HashMap<String, CompletableFuture<ModelRenderable>>> getFutureLetterMap() {
        return futureLetterMap;
    }

    public MutableLiveData<List<HashMap<String, ModelRenderable>>> getModelMapList() {
        return modelMapList;
    }

    public MutableLiveData<HashMap<String, ModelRenderable>> getLetterMap() {
        return letterMap;
    }

    private void onError(Throwable throwable) {
        Log.d("MainViewModel", throwable.getMessage());
    }
}
