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
import com.capstone.aryoulearning.ui.main.MainViewModel;
import com.capstone.aryoulearning.ui.main.list.rv.ListAdapter;
import com.capstone.aryoulearning.viewmodel.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;

public class ListFragment extends DaggerFragment {
    public static final String TAG = "ListFragmentX";

    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;

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
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.category_rv);
        mainViewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MainViewModel.class);
        mainViewModel.loadCategories();
        initRecyclerView();
        mainViewModel.getCatLiveData().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                listAdapter.setLists(categories);
            }
        });

    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        recyclerView.HORIZONTAL,
                        false));
        recyclerView.setAdapter(listAdapter);
    }
}
