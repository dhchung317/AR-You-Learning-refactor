package com.capstone.aryoulearning.controller;

import com.capstone.aryoulearning.model.Model;

import java.util.List;

public interface NavListenerX {
    void moveToListFragment();

    void moveToGameOrARFragment(final List<Model> animalResponseList, final boolean isAR_on);

    void moveToResultsFragment(final List<Model> categoryList);

    void moveToHintFragment(final List<Model> animalResponseList);

    void moveToReplayFragment(final List<Model> modelList, final boolean wasPreviousGameTypeAR);

    void backToHintFragment(final List<Model> animalResponseList);

    void moveToTutorialScreen(final List<Model> modelList);
}