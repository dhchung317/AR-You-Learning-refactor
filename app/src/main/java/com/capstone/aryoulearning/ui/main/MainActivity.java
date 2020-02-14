package com.capstone.aryoulearning.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.capstone.aryoulearning.R;
import com.capstone.aryoulearning.model.Model;
import com.capstone.aryoulearning.model.ModelResponse;
import com.capstone.aryoulearning.network.main.MainResource;
import com.capstone.aryoulearning.viewmodel.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {
    public static final String TAG = "MainActivity";

    private MainViewModel viewModel;
    private ProgressBar progressBar;
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
                            for (Model m:listResource.data.get(0).getList()) {
                                Log.d(TAG, "observeModelResponses: " + m.getName());
                            }
//                            adapter.setPosts(listResource.data);
                            break;
                        }

                        case ERROR:{
                            showProgressBar(false);
                            Log.e(TAG, "onChanged: error: " + listResource.message );
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
}
