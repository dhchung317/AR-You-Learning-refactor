package com.hyunki.aryoulearning2.ui.main.ar.controller;

import com.hyunki.aryoulearning2.ui.main.ar.util.CurrentWord;
import com.hyunki.aryoulearning2.ui.main.controller.NavListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class GameManager {
    private GameCommandListener gameCommands;
    private NavListener navListener;
    private CurrentWord currentWord;
    //will dictate the number of rounds
    private int roundLimit = 1;
    private Stack<String> keyStack = new Stack<>();
    private Map<String, Set<String>> answerMap = new HashMap<>();
    private List<CurrentWord> wordHistoryList = new ArrayList<>();
    private String attempt = "";

    //TODO - logic to rerun round when answer is incorrect

    public GameManager(List<String> modelMapKeys, GameCommandListener gameCommands, NavListener navListener) {
        this.gameCommands = gameCommands;
        this.navListener = navListener;
        for (int i = 0; i < roundLimit; i++) {
            keyStack.add(modelMapKeys.get(i));
        }
        this.currentWord = new CurrentWord(keyStack.pop());
    }

    public void setCurrentWord(CurrentWord currentWord) {
        this.currentWord = currentWord;
    }

    public String getCurrentWordAnswer() {
        return currentWord.getAnswer();
    }

    public void addTappedLetterToCurrentWordAttempt(String letter) {
        addLetterToAttempt(letter);
        if (attempt.length() == getCurrentWordAnswer().length()) {
            if (!attempt.toLowerCase().equals(getCurrentWordAnswer().toLowerCase())) {
                recordWrongAnswer(attempt);
                startNextGame(currentWord.getAnswer());
            } else {
                if (keyStack.size() > 0) {
                    wordHistoryList.add(currentWord);
//                    answerMap.put(currentWord.getAnswer(),currentWord.getAttempts());
                    startNextGame(keyStack.pop());
                } else {
//                    answerMap.put(currentWord.getAnswer(),currentWord.getAttempts());
                    wordHistoryList.add(currentWord);
                    navListener.setWordHistoryFromGameFragment(wordHistoryList);
                    navListener.moveToReplayFragment();
                }
            }
        }
    }

    public void undoLastLetterAdded() {
        subtractLetterFromAttempt();
    }

    public void recordWrongAnswer(String wrongAnswer) {
        attempt = "";
        currentWord.addWrongAnswerToSet(wrongAnswer);
    }

    public void startNextGame(String key) {
        refreshManager(key);
        //TODO - record wronganswers into a map of retrievable data
        gameCommands.startNextGame(key);
    }

    public String getAttempt() {
        return attempt;
    }

    public void addLetterToAttempt(String letter) {
        this.attempt += letter;
    }

    public String subtractLetterFromAttempt() {
        String letter = "";
        if (!attempt.isEmpty()) {
            letter = attempt.substring(attempt.length());
            attempt = attempt.substring(0, attempt.length() - 1);
        }
        return letter;
    }

    public void refreshManager(String key) {
        attempt = "";
        if(!currentWord.getAnswer().equals(key)) {
            setCurrentWord(new CurrentWord(key));
        }
    }
}