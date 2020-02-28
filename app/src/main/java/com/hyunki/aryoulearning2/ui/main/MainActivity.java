package com.hyunki.aryoulearning2.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hyunki.aryoulearning2.R;
import com.hyunki.aryoulearning2.model.Model;
import com.hyunki.aryoulearning2.model.ModelResponse;
import com.hyunki.aryoulearning2.network.main.MainResource;
import com.hyunki.aryoulearning2.ui.main.ar.ArHostFragment;
import com.hyunki.aryoulearning2.ui.main.controller.NavListener;
import com.hyunki.aryoulearning2.ui.main.hint.HintFragment;
import com.hyunki.aryoulearning2.ui.main.list.ListFragment;
import com.hyunki.aryoulearning2.util.audio.PronunciationUtil;
import com.hyunki.aryoulearning2.viewmodel.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements NavListener {
    public static final String TAG = "MainActivity";
    private MainViewModel viewModel;
    private ProgressBar progressBar;

    @Inject
    PronunciationUtil pronunciationUtil;

    @Inject
    ArHostFragment arHostFragment;

    @Inject
    ListFragment listFragment;

    @Inject
    HintFragment hintFragment;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);
        Log.d(TAG, "onCreate");

        viewModel = new ViewModelProvider(getViewModelStore(), providerFactory).get(MainViewModel.class);
        subscribeObservers();
    }

    private void subscribeObservers() {
        Log.d(TAG, "onChanged: subscribe method call");
        viewModel.observeModelResponses().observe(this, new Observer<MainResource<List<ModelResponse>>>() {
            @Override
            public void onChanged(MainResource<List<ModelResponse>> listResource) {

                if (listResource != null) {
                    switch (listResource.status) {

                        case LOADING: {
                            Log.d(TAG, "onChanged: loading");
                            showProgressBar(true);
                            break;
                        }

                        case SUCCESS: {
                            Log.d(TAG, "onChanged: success");
                            showProgressBar(false);
                            Log.d(TAG, "onChanged: " + listResource.data.size());
                            break;
                        }

                        case ERROR: {
                            showProgressBar(false);
                            Log.e(TAG, "onChanged: error: " + listResource.message);
                            break;
                        }

                        case FINISHED: {
                            showProgressBar(false);
                            moveToListFragment();
                        }

                    }
                }
            }
        });
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
                .replace(R.id.fragment_container, arHostFragment, "ar_fragment")
//                    .addToBackStack(null)
                .commit();

    }

    @Override
    public void moveToResultsFragment(List<Model> categoryList) {

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
    public void moveToReplayFragment(List<Model> modelList, boolean wasPreviousGameTypeAR) {

    }

    @Override
    public void backToHintFragment(List<Model> animalResponseList) {

    }

    @Override
    public void moveToTutorialScreen(List<Model> modelList) {

    }

    @Override
    public void setCategoryFromFragment(String category) {
        viewModel.setCurrentCategory(category);
    }
}
