package com.hyunki.aryoulearning2.ui.main.list;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunki.aryoulearning2.R;
import com.hyunki.aryoulearning2.ui.main.MainViewModel;
import com.hyunki.aryoulearning2.ui.main.ar.State;
import com.hyunki.aryoulearning2.ui.main.list.rv.ListAdapter;
import com.hyunki.aryoulearning2.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;

public class ListFragment extends DaggerFragment {
    public static final String TAG = "ListFragmentX";

    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;

    private ViewModelProviderFactory viewModelProviderFactory;

    private ListAdapter listAdapter;

    @Inject
    public ListFragment(ViewModelProviderFactory viewModelProviderFactory, ListAdapter listAdapter) {
        this.viewModelProviderFactory = viewModelProviderFactory;
        this.listAdapter = listAdapter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: on attach ran");
        AndroidSupportInjection.inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onAttach: on create ran");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onAttach: on viewcreated ran");

        recyclerView = view.findViewById(R.id.category_rv);
        mainViewModel = ViewModelProviders.of(getActivity(), viewModelProviderFactory).get(MainViewModel.class);
        mainViewModel.loadCategories();
        initRecyclerView();

        mainViewModel.getCatLiveData().observe(getViewLifecycleOwner(), categories -> {
//            listAdapter.setLists(categories);

        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        recyclerView.HORIZONTAL,
                        false));
        recyclerView.setAdapter(listAdapter);
    }

    private void renderCategories(State state){
        //TODO if else logic to render categories/set adapter list
    }
}
