package com.hyunki.aryoulearning2.ui.main.replay;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.hyunki.aryoulearning2.BaseApplication;
import com.hyunki.aryoulearning2.R;
import com.hyunki.aryoulearning2.model.Model;
import com.hyunki.aryoulearning2.ui.main.controller.NavListener;
import com.hyunki.aryoulearning2.util.audio.PronunciationUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ReplayFragment extends Fragment {
    private NavListener listener;

    private CardView resultsButtonCard, homeButtonCard, playAgainButtonCard;

    private List<Model> modelList = new ArrayList<>();

    private TextToSpeech textToSpeech;
    private PronunciationUtil pronunciationUtil;

    @Inject
    public ReplayFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_replay, container, false);
    }

    @Override
    public void onAttach(Context context) {
        ((BaseApplication) getActivity().getApplication()).getAppComponent().inject(this);
        super.onAttach(context);
        if (context instanceof NavListener) {
            listener = (NavListener) context;
        }
//        AndroidSupportInjection.inject(this);
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

    public void viewClickListeners() {
        resultsButtonCard.setOnClickListener(v -> {
//            pronunciationUtil.textToSpeechAnnouncer("Showing progress", textToSpeech);
            listener.moveToResultsFragment();
        });

        homeButtonCard.setOnClickListener(v -> {
//            pronunciationUtil.textToSpeechAnnouncer("Lets go home", textToSpeech);
            listener.moveToListFragment();
        });

        playAgainButtonCard.setOnClickListener(v -> {
//            pronunciationUtil.textToSpeechAnnouncer("Lets play again!", textToSpeech);
            listener.moveToGameFragment();
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        textToSpeech.shutdown();
//        pronunciationUtil = null;
//        listener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getFragmentManager().findFragmentByTag("result_fragment") != null) {
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag("result_fragment")).commit();
        }
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().clear().apply();
    }
}
