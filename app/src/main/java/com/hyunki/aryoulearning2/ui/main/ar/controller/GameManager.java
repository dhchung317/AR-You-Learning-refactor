package com.hyunki.aryoulearning2.ui.main.ar.controller;

import com.hyunki.aryoulearning2.ui.main.ar.util.CurrentWord;
import com.hyunki.aryoulearning2.ui.main.controller.NavListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class GameManager {
    private static Random r = new Random();
    private GameCommandListener gameCommands;
    private NavListener navListener;
    private CurrentWord currentWord;
    private int roundLimit = 3;
    private Stack<String> keyStack = new Stack<>();
    private List<CurrentWord> wordHistoryList = new ArrayList<>();
    private String attempt = "";

    public GameManager(List<String> modelMapKeys, GameCommandListener gameCommands, NavListener navListener) {
        this.gameCommands = gameCommands;
        this.navListener = navListener;
        while (keyStack.size() < roundLimit) {
            int ran = getRandom(modelMapKeys.size(), 0);
            if (!keyStack.contains(modelMapKeys.get(ran))) {
                keyStack.add(modelMapKeys.get(ran));
            }
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


        if (attempt.length() == getCurrentWordAnswer().length()) {
            if (!attempt.toLowerCase().equals(getCurrentWordAnswer().toLowerCase())) {
                recordWrongAnswer(attempt);
                startNextGame(currentWord.getAnswer());
            } else {
                if (keyStack.size() > 0) {
                    wordHistoryList.add(currentWord);
                    startNextGame(keyStack.pop());
                } else {
                    wordHistoryList.add(currentWord);
                    navListener.setWordHistoryFromGameFragment(wordHistoryList);
                    navListener.moveToReplayFragment();
                }
            }
        }
    }

    public void recordWrongAnswer(String wrongAnswer) {
        currentWord.addWrongAnswerToSet(wrongAnswer);
    }

    public void startNextGame(String key) {
        refreshManager(key);
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
            letter = attempt.substring(attempt.length() - 1);
            attempt = attempt.substring(0, attempt.length() - 1);
        }
        return letter;
    }

    public void refreshManager(String key) {
        if (!currentWord.getAnswer().equals(key)) {
            setCurrentWord(new CurrentWord(key));
        }
        attempt = "";
    }

    private static int getRandom(int max, int min) {
        return r.nextInt((max - min)) + min;
    }
}