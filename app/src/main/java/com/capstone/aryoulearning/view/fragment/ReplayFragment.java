package com.capstone.aryoulearning.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.capstone.aryoulearning.R;
import com.capstone.aryoulearning.util.audio.PronunciationUtil;
import com.capstone.aryoulearning.controller.NavListenerX;
import com.capstone.aryoulearning.model.Model;

import java.util.ArrayList;
import java.util.List;


public class ReplayFragment extends Fragment {

    private static final String MODEL_LIST = "MODEL_LIST";
    private static final String PREVIOUS_GAME_STATUS = "GAME_STATUS";
    private NavListenerX listener;

    private CardView resultsButtonCard, homeButtonCard, playAgainButtonCard;

    private List<Model> modelList = new ArrayList<>();

    private TextToSpeech textToSpeech;
    private PronunciationUtil pronunciationUtil;

    private boolean previousGameTypeIsAR;


    public ReplayFragment() {
    }

    public static ReplayFragment newInstance(final List<Model> modelList, final boolean wasPreviousGameTypeAR) {
        ReplayFragment fragment = new ReplayFragment();
        Bundle args = new Bundle();
//        args.putParcelableArrayList(MODEL_LIST, (ArrayList<? extends Parcelable>) modelList);
        args.putBoolean(PREVIOUS_GAME_STATUS,wasPreviousGameTypeAR);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            modelList = getArguments().getParcelableArrayList(MODEL_LIST);
            previousGameTypeIsAR = getArguments().getBoolean(PREVIOUS_GAME_STATUS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_replay, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavListenerX) {
            listener = (NavListenerX) context;
        }
//        pronunciationUtil = new PronunciationUtil();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        viewClickListeners();
//        textToSpeech = pronunciationUtil.getTTS(requireContext());
    }

    private void initializeViews(final View view) {
        playAgainButtonCard = view.findViewById(R.id.cardView_playagain);
        homeButtonCard = view.findViewById(R.id.cardView_home);
        resultsButtonCard = view.findViewById(R.id.cardView_results);
    }

    public void viewClickListeners(){
        resultsButtonCard.setOnClickListener(v -> {
            pronunciationUtil.textToSpeechAnnouncer("Showing progress", textToSpeech);
            listener.moveToResultsFragment(modelList);
        });
        homeButtonCard.setOnClickListener(v -> {
            pronunciationUtil.textToSpeechAnnouncer("Lets go home", textToSpeech);
//            listener.moveToListFragment(MainActivityX.getAnimalModelList(),
//                    MainActivityX.getCategoryList(),
//                    MainActivityX.getBackgroundList());
        });
        playAgainButtonCard.setOnClickListener(v -> {
            pronunciationUtil.textToSpeechAnnouncer("Lets play again!", textToSpeech);
            listener.moveToGameOrARFragment(modelList,previousGameTypeIsAR);
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        textToSpeech.shutdown();
        pronunciationUtil = null;
        listener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getFragmentManager().findFragmentByTag("result_fragment") != null){
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag("result_fragment")).commit();
        }
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().clear().apply();
    }
}
