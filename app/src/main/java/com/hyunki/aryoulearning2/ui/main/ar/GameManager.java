package com.hyunki.aryoulearning2.ui.main.ar;

import android.util.Log;

import java.util.List;
import java.util.Stack;

public class GameManager {
    private GameCommandListener gameCommands;
    private CurrentWord currentWord;
    //will dictate the number of rounds
    private int roundLimit = 5;
    private Stack<String> keyStack = new Stack<>();
    private String attempt = "";

    //TODO - logic to rerun round when answer is incorrect

    public GameManager(List<String> modelMapKeys, GameCommandListener gameCommands) {
        this.gameCommands = gameCommands;
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
                if (keyStack.size() > 1) {
                    startNextGame(keyStack.pop());
                } else {
                    //TODO - move to next fragment
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

    public void subtractLetterFromAttempt() {
        if (!attempt.isEmpty()) {
            attempt = attempt.substring(0, attempt.length() - 1);
        }
    }

    public void refreshManager(String key) {
        attempt = "";
        setCurrentWord(new CurrentWord(key));
    }
}