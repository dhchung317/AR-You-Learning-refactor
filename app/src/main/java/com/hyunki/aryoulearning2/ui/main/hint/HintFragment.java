package com.hyunki.aryoulearning2.ui.main.hint;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunki.aryoulearning2.R;
import com.hyunki.aryoulearning2.model.Model;
import com.hyunki.aryoulearning2.ui.main.MainViewModel;
import com.hyunki.aryoulearning2.ui.main.controller.NavListener;
import com.hyunki.aryoulearning2.ui.main.hint.rv.HintAdapter;
import com.hyunki.aryoulearning2.viewmodel.ViewModelProviderFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;

public class HintFragment extends DaggerFragment {

    private RecyclerView hintRecyclerView;
    private ConstraintLayout constraintLayout;
    private NavListener listener;
    private Button startGameButton, tutorialButton;
    private FloatingActionButton backFAB;

    private MainViewModel mainViewModel;

//    private String currentCategory;

    private List<Model> modelList = new ArrayList<>();

    private ViewModelProviderFactory viewModelProviderFactory;

//    @Inject
//    PronunciationUtil pronunciationUtil;

    private HintAdapter hintAdapter;

    @Inject
    public HintFragment(ViewModelProviderFactory viewModelProviderFactory, HintAdapter hintAdapter) {
        this.viewModelProviderFactory = viewModelProviderFactory;
        this.hintAdapter = hintAdapter;
    }

    //    public static HintFragment newInstance() {
//
//        return new HintFragment();
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavListener) {
            listener = (NavListener) context;
        }
        AndroidSupportInjection.inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hint, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = ViewModelProviders.of(getActivity(), viewModelProviderFactory).get(MainViewModel.class);
        mainViewModel.loadCurrentCategoryName();

        mainViewModel.getCurCatLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                currentCategory = s;
                mainViewModel.loadModelsByCat(s);
            }
        });
        enableViews(constraintLayout);

//        textToSpeech = pronunciationUtil.getTTS(requireContext());
        initializeViews(view);

//        startBlinkText();
        hintRecyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false));

        hintRecyclerView.setAdapter(hintAdapter);

        mainViewModel.getModelLiveData().observe(getViewLifecycleOwner(), new Observer<List<Model>>() {
            @Override
            public void onChanged(List<Model> models) {
//                        modelList = models;
                for (int i = 0; i < models.size(); i++) {
                    Log.d("hint fragmetn", "onChanged: " + models.get(i).getName());
                }

                hintAdapter.setList(models);
            }
        });


//        Log.d("hint frag", "onViewCreated: " + modelList.get(0).getName());


//        setHintRV();
//        setArSwitch();
        viewClickListeners();

//        startButtonShimmering();
    }


    private void disableViews(View view) {
        if (view != null) {
            view.setClickable(false);
            if (view instanceof ViewGroup) {
                ViewGroup vg = ((ViewGroup) view);
                for (int i = 0; i < vg.getChildCount(); i++) {
                    disableViews(vg.getChildAt(i));
                }
            }
        }
    }

    private void enableViews(View view) {
        if (view != null) {
            view.setClickable(true);
            if (view instanceof ViewGroup) {
                ViewGroup vg = ((ViewGroup) view);
                for (int i = 0; i < vg.getChildCount(); i++) {
                    disableViews(vg.getChildAt(i));
                }
            }
        }
    }

    public void viewClickListeners() {

        startGameButton.setOnClickListener(v -> {
            disableViews(constraintLayout);
//            constraintLayout.addView(parentalSupervision);
//            okButton1.setOnClickListener(v1 -> {
//                constraintLayout.removeView(parentalSupervision);
//                constraintLayout.addView(stayAlert);
//                okButton2.setOnClickListener(v11 -> {
//                    constraintLayout.removeView(stayAlert);
            listener.moveToGameFragment();
//                });
//            });
        });

        tutorialButton.setOnClickListener(v -> listener.moveToTutorialScreen(modelList));
        backFAB.setOnClickListener(v -> getActivity().onBackPressed());
    }

    public void setHintRV() {

    }

    private void initializeViews(@NonNull final View view) {
        startGameButton = view.findViewById(R.id.hint_fragment_button);

        hintRecyclerView = view.findViewById(R.id.hint_recycler_view);
        tutorialButton = view.findViewById(R.id.hint_frag_tutorial_button);
        backFAB = view.findViewById(R.id.back_btn);

        constraintLayout = view.findViewById(R.id.hint_layout);

//        parentalSupervision = getLayoutInflater().inflate(R.layout.parental_supervision_card, constraintLayout, false);
//        stayAlert = getLayoutInflater().inflate(R.layout.stay_alert_card, constraintLayout, false);
//        okButton1 = parentalSupervision.findViewById(R.id.warning_button_ok_1);
//        okButton2 = stayAlert.findViewById(R.id.warning_button_ok_2);

    }

}
