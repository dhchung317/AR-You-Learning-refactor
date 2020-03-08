package com.hyunki.aryoulearning2.ui.main.tutorial;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.hyunki.aryoulearning2.R;
import com.hyunki.aryoulearning2.model.Model;
import com.hyunki.aryoulearning2.ui.main.controller.NavListener;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class TutorialFragment extends Fragment {
    public static final String MODEL_LIST = "MODEL_LIST";
    private Button backButton, playVideoButton, startGameButton;
    private NavListener listener;
    private VideoView tutorialVideoView;

    @Inject
    public TutorialFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NavListener) {
            listener = (NavListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            modelList = getArguments().getParcelableArrayList(MODEL_LIST);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        viewClickListeners();
        playTutorial();
    }

    public void viewClickListeners(){
        startGameButton.setOnClickListener(v -> {
            if (isVideoViewPlaying()) {
                tutorialVideoView.pause();
            }
            listener.moveToGameFragment();
        });
        backButton.setOnClickListener(v -> {
            if (isVideoViewPlaying()) {
                tutorialVideoView.pause();
            }
            getActivity().onBackPressed();
        });
        playVideoButton.setOnClickListener(v -> {
            if(isVideoViewPlaying()){
                tutorialVideoView.pause();
                playVideoButton.setBackgroundResource(R.drawable.play_button_paused);
            } else {
                tutorialVideoView.start();
                playVideoButton.setBackgroundResource(R.drawable.play_button_playing);
            }
        });
    }

    private boolean isVideoViewPlaying() {
        return tutorialVideoView.isPlaying();
    }

    private void playTutorial() {
        MediaController mediaController = new MediaController(requireContext());
        tutorialVideoView.setMediaController(mediaController);
        String pathToTutorial = "android.resource://" + Objects.requireNonNull(getActivity()).getPackageName() + "/" + R.raw.ar_tutorial;
        Uri tutorialUri = Uri.parse(pathToTutorial);
        tutorialVideoView.setVideoURI(tutorialUri);
    }

    private void initializeViews(@NonNull final View view) {
        tutorialVideoView = view.findViewById(R.id.tutorial_videoView);
        backButton = view.findViewById(R.id.tutorial_frag_back_to_hint_button);
        startGameButton = view.findViewById(R.id.tutorial_frag_start_game_button);
        playVideoButton = view.findViewById(R.id.tutorial_frag_play_video_button);
    }
}
