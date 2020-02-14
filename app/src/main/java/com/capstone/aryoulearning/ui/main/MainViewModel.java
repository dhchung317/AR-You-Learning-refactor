package com.capstone.aryoulearning.ui.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
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

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;



public class MainViewModel extends ViewModel {
    public static final String TAG = "MainViewModel";

    private final MainApi mainApi;

    private MediatorLiveData<MainResource<List<ModelResponse>>> modelResponses;

    @Inject
    public MainViewModel(MainApi mainApi) {
        this.mainApi = mainApi;
    }

    public LiveData<MainResource<List<ModelResponse>>> observeModelResponses(){
        if(modelResponses == null){
            modelResponses = new MediatorLiveData<>();
            modelResponses.setValue(MainResource.loading((List<ModelResponse>)null));

            final LiveData<MainResource<List<ModelResponse>>> source = LiveDataReactiveStreams.fromPublisher(
                    mainApi.getModels()

                            .onErrorReturn(new Function<Throwable, List<ModelResponse>>() {
                                @Override
                                public List<ModelResponse> apply(Throwable throwable) throws Exception {
                                    Log.e(TAG, "apply: ", throwable);
                                    ModelResponse modelResponse = new ModelResponse(null);
                                    modelResponse.setError(-1);

                                    ArrayList<ModelResponse> modelResponses = new ArrayList<>();
                                    modelResponses.add(modelResponse);
                                    return modelResponses;
                                }
                            })
                            .map(new Function<List<ModelResponse>, MainResource<List<ModelResponse>>>() {
                                @Override
                                public MainResource<List<ModelResponse>> apply(List<ModelResponse> modelResponses) throws Exception {

                                    if (modelResponses.size() > 0) {

                                        if (modelResponses.get(0).getError() == -1) {
                                            return MainResource.error("error", null);
                                        }
                                    }
                                    return MainResource.success(modelResponses);
                                }
                            })
                            .subscribeOn(Schedulers.io())
            );

            modelResponses.addSource(source, new Observer<MainResource<List<ModelResponse>>>() {
                @Override
                public void onChanged(MainResource<List<ModelResponse>> listResource) {
                    modelResponses.setValue(listResource);
                    modelResponses.removeSource(source);
                }
            });
        }

        return modelResponses;
    }
}
