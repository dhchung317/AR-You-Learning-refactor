package com.capstone.aryoulearning.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.capstone.aryoulearning.R;

import com.capstone.aryoulearning.model.Model;
import com.capstone.aryoulearning.model.ModelResponse;
import com.capstone.aryoulearning.network.main.MainResource;

import com.capstone.aryoulearning.ui.main.ar.ARHostFragmentX;
import com.capstone.aryoulearning.ui.main.list.ListFragment;
import com.capstone.aryoulearning.ui.main.controller.*;
import com.capstone.aryoulearning.ui.main.hint.HintFragment;
import com.capstone.aryoulearning.viewmodel.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements NavListener {
    public static final String TAG = "MainActivity";
    private MainViewModel viewModel;
    private ProgressBar progressBar;
//    public static String currentCategory;


    @Inject
    int resId;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(resId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        Log.d(TAG, "onCreate");

//        viewModel = ViewModelProviders.of(this,providerFactory).get(MainViewModel.class);
        viewModel = new ViewModelProvider(getViewModelStore(),providerFactory).get(MainViewModel.class);
        subscribeObservers();
//        viewModel.convertStream();
    }

    private void subscribeObservers(){
        Log.d(TAG, "onChanged: subscribe method call");
//        viewModel.observeModelResponses().removeObservers(this);
        viewModel.observeModelResponses().observe(this, new Observer<MainResource<List<ModelResponse>>>() {
            @Override
            public void onChanged(MainResource<List<ModelResponse>> listResource) {

                if(listResource != null){
                    switch (listResource.status){

                        case LOADING:{
                            Log.d(TAG, "onChanged: loading");
                            showProgressBar(true);
                            break;
                        }

                        case SUCCESS:{
                            Log.d(TAG, "onChanged: success");
                            showProgressBar(false);
                            Log.d(TAG, "onChanged: " + listResource.data.size());



//                            for (Model m:listResource.data.get(0).getList()) {
//                                Log.d(TAG, "observeModelResponses: " + m.getName());
//                            }
//                            adapter.setPosts(listResource.data);
                            break;
                        }

                        case ERROR:{
                            showProgressBar(false);
                            Log.e(TAG, "onChanged: error: " + listResource.message );
                            break;
                        }

                        case FINISHED:{
                            showProgressBar(false);
                            moveToListFragment();
                        }

                    }
                }
            }
        });
    }
    private void showProgressBar(boolean isVisible){

        if(isVisible){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void moveToListFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ListFragment())
                .commit();

    }

    @Override
    public void moveToGameFragment() {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ARHostFragmentX(), "ar_fragment")
//                    .addToBackStack(null)
                .commit();

    }

    @Override
    public void moveToResultsFragment(List<Model> categoryList) {

    }

    @Override
    public void moveToHintFragment(String category) {
        viewModel.setCurrentCategory(category);
//        viewModel.setModelList(modelList);
        Log.d(TAG, "moveToHintFragment: " + category);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HintFragment())
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
}
