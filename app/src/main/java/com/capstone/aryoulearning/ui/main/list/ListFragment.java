package com.capstone.aryoulearning.ui.main.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.aryoulearning.R;
import com.capstone.aryoulearning.db.model.Category;
import com.capstone.aryoulearning.ui.main.list.rv.ListAdapter;
import com.capstone.aryoulearning.model.Model;
import com.capstone.aryoulearning.model.ModelResponse;
import com.capstone.aryoulearning.network.main.MainResource;
import com.capstone.aryoulearning.ui.main.MainViewModel;
import com.capstone.aryoulearning.viewmodel.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;

public class ListFragment extends DaggerFragment {
    public static final String TAG = "ListFragmentX";

    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;

//    private List<ModelResponse> data = new ArrayList<>();
//    private List<List<Model>> categoryModelList = new ArrayList<>();
//    private List<String> categoryName = new ArrayList<>();
//    private List<String> categoryImages = new ArrayList<>();

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    ListAdapter listAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.category_rv);
        mainViewModel = ViewModelProviders.of(this,viewModelProviderFactory).get(MainViewModel.class);
//        mainViewModel.loadModelInfoByCat(mainViewModel.getCurrentCategory());
        mainViewModel.loadCategories();
        initRecyclerView();
        mainViewModel.getCatLiveData().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                listAdapter.setLists(categories);
            }
        });


//        mainViewModel.observeModelResponses().observe(getViewLifecycleOwner(), new Observer<MainResource<List<ModelResponse>>>() {
//            @Override
//            public void onChanged(MainResource<List<ModelResponse>> listMainResource) {
//
////                    if(mainViewModel.getModelResponsesData().getValue().status == MainResource.Status.FINISHED ) {
//
//                        if(data.size() < 2) {
//                            data = mainViewModel.getModelResponses();
//                            for (int i = 0; i < data.size(); i++) {
//                                categoryName.add(data.get(i).getCategory());
//                                categoryImages.add(data.get(i).getBackground());
//                                categoryModelList.add(data.get(i).getList());
//
//                            }
//                        }
//                initRecyclerView();
//                listAdapter.setLists(categoryModelList,categoryName,categoryImages);
//
//            }
//        });



    }
//
//    public static ListFragment newInstance() {
//        return new ListFragment();
//    }

    private void initRecyclerView(){
        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        recyclerView.HORIZONTAL,
                        false));
        recyclerView.setAdapter(listAdapter);
    }

//    private List<ModelResponse> getData(){
//        mainViewModel.observeModelResponses()
//    }

}
