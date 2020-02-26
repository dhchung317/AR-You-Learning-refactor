package com.hyunki.aryoulearning2.ui.main.controller;

import com.hyunki.aryoulearning2.model.Model;

import java.util.List;

public interface NavListener {
    void moveToListFragment();

    void moveToGameFragment();

    void moveToResultsFragment(final List<Model> categoryList);

    void moveToHintFragment();

    void moveToReplayFragment(final List<Model> modelList, final boolean wasPreviousGameTypeAR);

    void backToHintFragment(final List<Model> animalResponseList);

    void moveToTutorialScreen(final List<Model> modelList);

    void setCategoryFromFragment(String category);
}
