package com.hyunki.aryoulearning2.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.hyunki.aryoulearning2.BaseApplication;
import com.hyunki.aryoulearning2.R;
import com.hyunki.aryoulearning2.db.model.Category;
import com.hyunki.aryoulearning2.di.AppComponent;
import com.hyunki.aryoulearning2.di.DaggerAppComponent;
import com.hyunki.aryoulearning2.ui.main.ar.ArHostFragment;
import com.hyunki.aryoulearning2.ui.main.ar.util.CurrentWord;
import com.hyunki.aryoulearning2.ui.main.controller.NavListener;
import com.hyunki.aryoulearning2.ui.main.hint.HintFragment;
import com.hyunki.aryoulearning2.ui.main.list.ListFragment;
import com.hyunki.aryoulearning2.ui.main.replay.ReplayFragment;
import com.hyunki.aryoulearning2.ui.main.results.ResultsFragment;
import com.hyunki.aryoulearning2.ui.main.tutorial.TutorialFragment;
import com.hyunki.aryoulearning2.util.audio.PronunciationUtil;
import com.hyunki.aryoulearning2.viewmodel.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements NavListener {
    public static final String TAG = "MainActivity";
    public static final String NETWORK_CALL_COMPLETED = "network_call_completed";
    private MainViewModel viewModel;
    private ProgressBar progressBar;
    SharedPreferences prefs;

    @Inject
    PronunciationUtil pronunciationUtil;

    @Inject
    ArHostFragment arHostFragment;

    @Inject
    ListFragment listFragment;

    @Inject
    HintFragment hintFragment;

    @Inject
    ReplayFragment replayFragment;

    @Inject
    ResultsFragment resultsFragment;

    @Inject
    TutorialFragment tutorialFragment;

    @Inject
    int resId;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();

        if (hasFocus) {
            decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(resId);
        ((BaseApplication) getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        viewModel = ViewModelProviders.of(this, providerFactory).get(MainViewModel.class);

        progressBar = findViewById(R.id.progress_bar);
        Log.d(TAG, "onCreate");
        if(prefs.contains(NETWORK_CALL_COMPLETED)){
            Log.d(TAG, "onCreate: " + prefs.contains(NETWORK_CALL_COMPLETED));
            moveToListFragment();
        }else {
            viewModel.loadModelResponses();
            viewModel.getModelResponsesData().observe(this, this::renderModelResponses);
        }
    }

    private void renderModelResponses(State state) {
        if (state == State.Loading.INSTANCE) {
            showProgressBar(true);
        } else if (state == State.Error.INSTANCE) {
            showProgressBar(false);
        } else if (state.getClass() == State.Success.OnModelResponsesLoaded.class) {
            prefs.edit().putString(NETWORK_CALL_COMPLETED,"success").apply();
            moveToListFragment();
        }
    }

    private void showProgressBar(boolean isVisible) {
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void moveToListFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, listFragment)
                .commit();
    }

    @Override
    public void moveToGameFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, arHostFragment)
                .commit();
    }

    @Override
    public void moveToResultsFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, resultsFragment)
                .commit();
    }

    @Override
    public void moveToHintFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, hintFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void moveToReplayFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, replayFragment)
                .commit();
    }

    @Override
    public void moveToTutorialFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, tutorialFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setCategoryFromListFragment(Category category) {
        viewModel.setCurrentCategory(category);
    }

    @Override
    public void setWordHistoryFromGameFragment(List<CurrentWord> wordHistory) {
        viewModel.setWordHistory(wordHistory);
    }
}
