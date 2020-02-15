package com.capstone.aryoulearning.ui.main.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.aryoulearning.R;
import com.capstone.aryoulearning.controller.CategoryAdapter;
import com.capstone.aryoulearning.controller.NavListener;
import com.capstone.aryoulearning.model.Model;
import com.capstone.aryoulearning.model.ModelResponse;
import com.capstone.aryoulearning.network.main.MainResource;
import com.capstone.aryoulearning.ui.main.MainViewModel;
import com.capstone.aryoulearning.viewmodel.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ListFragment extends DaggerFragment {
    public static final String TAG = "ListFragment";

    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;

    private List<ModelResponse> data = new ArrayList<>();
    private List<List<Model>> categoryModelList = new ArrayList<>();
    private List<String> categoryName = new ArrayList<>();
    private List<String> categoryImages = new ArrayList<>();

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.category_rv);
        mainViewModel = ViewModelProviders.of(this,viewModelProviderFactory).get(MainViewModel.class);
        mainViewModel.observeModelResponses().observe(getViewLifecycleOwner(), new Observer<MainResource<List<ModelResponse>>>() {
            @Override
            public void onChanged(MainResource<List<ModelResponse>> listMainResource) {

                    if(mainViewModel.modelResponsesData.getValue().status == MainResource.Status.FINISHED ) {
                        data = mainViewModel.getModelResponses();
                        for (int i = 0; i < data.size(); i++) {
                            categoryName.add(data.get(i).getCategory());
                            categoryImages.add(data.get(i).getBackground());

                            categoryModelList.add(data.get(i).getList());

                        }


                        initRecyclerView();
                        categoryAdapter.setLists(categoryModelList,categoryName,categoryImages);
                    }

            }
        });


    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), recyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(categoryAdapter);
    }

//    private List<ModelResponse> getData(){
//        mainViewModel.observeModelResponses()
//    }

}
