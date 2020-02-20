package com.capstone.aryoulearning.ui.main.ar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.capstone.aryoulearning.R;
import com.capstone.aryoulearning.model.Model;
import com.capstone.aryoulearning.ui.main.MainViewModel;
import com.capstone.aryoulearning.ui.main.controller.NavListener;
import com.capstone.aryoulearning.util.audio.PronunciationUtil;
import com.capstone.aryoulearning.viewmodel.ViewModelProviderFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;

public class ArHostFragment extends DaggerFragment {

    @Inject
    PronunciationUtil pronunciationUtil;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private MainViewModel mainViewModel;
    private ArViewModel arViewModel;

    private static final int RC_PERMISSIONS = 0x123;
    public static final String MODEL_LIST = "MODEL_LIST";

    private NavListener listener;

    private GestureDetector gestureDetector;
    private ArFragment arFragment;

    private List<Model> modelList = new ArrayList<>();
    private List<HashMap<String, CompletableFuture<ModelRenderable>>> futureModelMapList = new ArrayList<>();
    private HashMap<String, CompletableFuture<ModelRenderable>> futureLetterMap = new HashMap<>();
    private List<HashMap<String, ModelRenderable>> modelMapList = new ArrayList<>();
    private HashMap<String, ModelRenderable> letterMap = new HashMap<>();

//    @Inject
//    public ArHostFragment(PronunciationUtil pronunciationUtil){
//        this.pronunciationUtil = pronunciationUtil;
//        this.textToSpeech = pronunciationUtil.textToSpeech;
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavListener) {
            listener = (NavListener) context;
        }
        AndroidSupportInjection.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_arfragment_host, container, false);
        arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel.class);
        mainViewModel.loadCurrentCategoryName();
        mainViewModel.getCurCatLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mainViewModel.loadModelsByCat(s);
            }
        });

        mainViewModel.getModelLiveData().observe(getViewLifecycleOwner(), new Observer<List<Model>>() {
            @Override
            public void onChanged(List<Model> models) {
                modelList = models;

//                setListMapsOfFutureModels(modelList);
//                setMapOfFutureLetters(futureModelMapList);
//
//                setModelRenderables(futureModelMapList);
//                setLetterRenderables(futureLetterMap);

                futureLetterMap = arViewModel.getFutureLetterMap();
                futureModelMapList = arViewModel.getFutureModelMapList();
                modelMapList = arViewModel.getModelMapList();
                letterMap = arViewModel.getLetterMap();
            }
        });

        gestureDetector =
                new GestureDetector(
                        getActivity(),
                        new GestureDetector.SimpleOnGestureListener() {
                            @Override
                            public boolean onSingleTapUp(MotionEvent e) {
//                                onSingleTap(e);
                                return true;
                            }

                            @Override
                            public boolean onDown(MotionEvent e) {
                                return true;
                            }
                        });

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void setListMapsOfFutureModels(List<Model> modelList) {

        for (int i = 0; i < modelList.size(); i++) {
            HashMap<String, CompletableFuture<ModelRenderable>> futureMap = new HashMap();
            futureMap.put(modelList.get(i).getName(),
                    ModelRenderable.builder().
                            setSource(getActivity(), Uri.parse(modelList.get(i).getName() + ".sfb")).build());
            futureModelMapList.add(futureMap);
        }
    }

    private void setMapOfFutureLetters(List<HashMap<String, CompletableFuture<ModelRenderable>>> futureMapList) {
        for (int i = 0; i < futureMapList.size(); i++) {
            String modelName = futureMapList.get(i).keySet().toString();
            for (int j = 0; j < modelName.length(); j++) {
                futureLetterMap.put(Character.toString(modelName.charAt(j)), ModelRenderable.builder().
                        setSource(getActivity(), Uri.parse(modelName.charAt(j) + ".sfb")).build());
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
//        hasFinishedLoadingModels = true;
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
//        hasFinishedLoadingLetters = true;
    }

    public static void requestCameraPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(
                activity, new String[]{Manifest.permission.CAMERA}, requestCode);
    }

//    private void onSingleTap(MotionEvent tap) {
//        if (!hasFinishedLoadingModels || !hasFinishedLoadingLetters) {
//            // We can't do anything yet.
//            return;
//        }
//
//        Frame frame = arFragment.getArSceneView().getArFrame();
//        if (frame != null) {
//            if (!hasPlacedGame && tryPlaceGame(tap, frame)) {
//                hasPlacedGame = true;
//                f.removeView(tapAnimation);
//            }
//        }
//    }
}
