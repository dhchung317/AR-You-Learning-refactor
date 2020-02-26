package com.hyunki.aryoulearning2.ui.main.ar.util;

import com.hyunki.aryoulearning2.model.CurrentWord;

import java.util.List;

//Class to keep track of the current game state,
// needs to track the current word, the answer and attempts, the next games model, and maybe wrong answers
public class GameManager {
    private final List<String> modelMapKeys;
    private CurrentWord currentWord;

    public GameManager(List<String> modelMapKeys, CurrentWord currentwWord) {
        this.modelMapKeys = modelMapKeys;
        this.currentWord = currentwWord;
    }


}
