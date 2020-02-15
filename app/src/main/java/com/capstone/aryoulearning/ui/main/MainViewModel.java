package com.capstone.aryoulearning.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.load.engine.Resource;
import com.capstone.aryoulearning.model.Model;
import com.capstone.aryoulearning.model.ModelResponse;
import com.capstone.aryoulearning.network.main.MainApi;
import com.capstone.aryoulearning.network.main.MainResource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;



public class MainViewModel extends ViewModel {
    public static final String TAG = "MainViewModel";

    private final MainApi mainApi;

    private String currentCategory;

    public MediatorLiveData<MainResource<List<ModelResponse>>> modelResponsesData = new MediatorLiveData<>();

    @Inject
    public MainViewModel(MainApi mainApi) {
        this.mainApi = mainApi;
    }

    public LiveData<MainResource<List<ModelResponse>>> observeModelResponses(){

            modelResponsesData.setValue(MainResource.loading(new ArrayList<>()));


                    LiveData<MainResource<List<ModelResponse>>> data = LiveDataReactiveStreams.fromPublisher(
                            mainApi.getModels()

                            .onErrorReturn(throwable -> {
                                Log.e(TAG, "apply: ", throwable);
                                ModelResponse modelResponse = new ModelResponse(null);
                                modelResponse.setError(-1);

                                ArrayList<ModelResponse> modelResponses = new ArrayList<>();
                                modelResponses.add(modelResponse);
                                return modelResponses;
                            })
                            .map((Function<List<ModelResponse>, MainResource<List<ModelResponse>>>) modelResponses -> {

                                if (modelResponses.size() > 0) {

                                    if (modelResponses.get(0).getError() == -1) {
                                        return MainResource.error("error", null);
                                    }
                                }

                                return MainResource.success(modelResponses);
                            })
                            .subscribeOn(Schedulers.io())
                    );

            modelResponsesData.addSource(data, listResource -> {
            Log.d(TAG, "observeModelResponses: reached" + listResource.data.size());
            modelResponsesData.setValue(MainResource.finished(listResource.data));
            modelResponsesData.removeSource(data);

//
        });

        return modelResponsesData;
    }

    public List<ModelResponse> getModelResponses() {

        return modelResponsesData.getValue().data;
    }

    public String getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }
}
