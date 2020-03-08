package com.hyunki.aryoulearning2.ui.main.controller;

import com.hyunki.aryoulearning2.db.model.Category;
import com.hyunki.aryoulearning2.model.Model;
import com.hyunki.aryoulearning2.ui.main.ar.util.CurrentWord;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NavListener {
    void moveToListFragment();

    void moveToGameFragment();

    void moveToResultsFragment();

    void moveToHintFragment();

    void moveToReplayFragment();

    void setWordHistoryFromGameFragment(List<CurrentWord> wordHistory);

    void moveToTutorialFragment();

    void setCategoryFromListFragment(Category category);
}
