package com.hyunki.aryoulearning2.ui.main.list;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunki.aryoulearning2.R;
import com.hyunki.aryoulearning2.db.model.Category;
import com.hyunki.aryoulearning2.ui.main.MainViewModel;
import com.hyunki.aryoulearning2.ui.main.State;
import com.hyunki.aryoulearning2.ui.main.list.rv.ListAdapter;
import com.hyunki.aryoulearning2.viewmodel.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;

public class ListFragment extends DaggerFragment {
    public static final String TAG = "ListFragmentX";

    private MainViewModel mainViewModel;

    private RecyclerView recyclerView;

    private ViewModelProviderFactory viewModelProviderFactory;

    private ProgressBar progressBar;

    private ListAdapter listAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: on attach ran");
        AndroidSupportInjection.inject(this);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = getActivity().findViewById(R.id.progress_bar);

    }

    @Inject
    public ListFragment(ViewModelProviderFactory viewModelProviderFactory, ListAdapter listAdapter) {
        this.viewModelProviderFactory = viewModelProviderFactory;
        this.listAdapter = listAdapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, " on create ran");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, " on viewcreated ran");

        recyclerView = view.findViewById(R.id.category_rv);
        mainViewModel = ViewModelProviders.of(getActivity(), viewModelProviderFactory).get(MainViewModel.class);
        mainViewModel.loadCategories();

        mainViewModel.getCatLiveData().observe(getViewLifecycleOwner(), categories -> {
//            listAdapter.setLists(categories);
            renderCategories(categories);
            Log.d(TAG, "onViewCreated: " + mainViewModel.getCatLiveData().getValue().getClass());

        });


        initRecyclerView();
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        recyclerView.HORIZONTAL,
                        false));
        recyclerView.setAdapter(listAdapter);
    }

    private void renderCategories(State state) {
        if (state == State.Loading.INSTANCE) {
            progressBar.bringToFront();
            showProgressBar(true);

        } else if (state == State.Error.INSTANCE) {
            showProgressBar(false);

        } else if (state.getClass() == State.Success.OnCategoriesLoaded.class) {
            showProgressBar(false);
            State.Success.OnCategoriesLoaded s = (State.Success.OnCategoriesLoaded) state;
            List<Category> categories = s.getCategories();
            listAdapter.setLists(categories);

            Log.d(TAG, "renderCategories: " + categories.size());
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
    public void onResume() {
        super.onResume();
        renderCategories(mainViewModel.getCatLiveData().getValue());
    }
}
