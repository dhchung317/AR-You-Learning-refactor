package com.capstone.aryoulearning.ui.main.controller;

import com.capstone.aryoulearning.model.Model;

import java.util.List;

public interface NavListener {
    void moveToListFragment();

    void moveToGameFragment();

    void moveToResultsFragment(final List<Model> categoryList);

    void moveToHintFragment(String category);

    void moveToReplayFragment(final List<Model> modelList, final boolean wasPreviousGameTypeAR);

    void backToHintFragment(final List<Model> animalResponseList);

    void moveToTutorialScreen(final List<Model> modelList);
}
