package com.capstone.aryoulearning.ui.main.ar;

import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.capstone.aryoulearning.R;
import com.capstone.aryoulearning.controller.NavListenerX;
import com.capstone.aryoulearning.db.model.ModelInfo;
import com.capstone.aryoulearning.model.Model;
import com.capstone.aryoulearning.ui.main.MainViewModel;
import com.capstone.aryoulearning.ui.main.controller.NavListener;
import com.capstone.aryoulearning.util.audio.PronunciationUtil;
import com.capstone.aryoulearning.viewmodel.ViewModelProviderFactory;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;

public class ARFragment extends DaggerFragment {

    @Inject
    PronunciationUtil pronunciationUtil;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    private MainViewModel mainViewModel;

    private List<Model> modelList = new ArrayList<>();

    private static final int RC_PERMISSIONS = 0x123;
    public static final String MODEL_LIST = "MODEL_LIST";
    private NavListener listener;

    private GestureDetector gestureDetector;
    private ArFragment arFragment;

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
                mainViewModel.loadModelInfoByCat(s);
            }
        });
        mainViewModel.getModelInfoLiveData().observe(getViewLifecycleOwner(), new Observer<List<ModelInfo>>() {
            @Override
            public void onChanged(List<ModelInfo> modelInfos) {
                mainViewModel.convertModelInfoToModels(modelInfos);
            }
        });
        mainViewModel.getConvertedModelInfoLiveData().observe(getViewLifecycleOwner(), new Observer<List<Model>>() {
            @Override
            public void onChanged(List<Model> models) {
                modelList = models;
            }
        });

    }


        @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
