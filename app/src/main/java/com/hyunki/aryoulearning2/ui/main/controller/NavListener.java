package com.hyunki.aryoulearning2.ui.main.controller;

import com.hyunki.aryoulearning2.db.model.Category;
import com.hyunki.aryoulearning2.model.Model;

import java.util.List;

public interface NavListener {
    void moveToListFragment();

    void moveToGameFragment();

    void moveToResultsFragment();

    void moveToHintFragment();

    void moveToReplayFragment();

    void moveToTutorialFragment();

    void setCategoryFromFragment(Category category);
}
