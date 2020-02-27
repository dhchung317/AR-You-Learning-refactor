package com.hyunki.aryoulearning2.ui.main.ar.util;

import android.util.Log;

import java.util.List;
import java.util.Stack;

//Class to keep track of the current game state,
// needs to track the current word, the answer and attempts, the next games model, and maybe wrong answers
public class GameManager {
    private GameCommandListener gameCommands;
    private CurrentWord currentWord;
    //will dictate the number of rounds
    private int roundLimit = 5;
    private Stack<String> keyStack = new Stack<>();
    private String attempt = "";

    //TODO - logic to rerun round when answer is incorrect

    public GameManager(List<String> modelMapKeys,GameCommandListener gameCommands) {
        this.gameCommands = gameCommands;
        for (int i = 0; i < roundLimit; i++) {
            keyStack.add(modelMapKeys.get(i));
        }
        this.currentWord = new CurrentWord(keyStack.peek());

    }

    public void setCurrentWord(CurrentWord currentWord) {
        this.currentWord = new CurrentWord(keyStack.pop());
    }

    public String getCurrentWordAnswer() {
        return currentWord.getAnswer();
    }

    public void addTappedLetterToCurrentWordAttempt(String letter) {
        addLetterToAttempt(letter);
        if (attempt.length() == getCurrentWordAnswer().length()) {
            if (!attempt.toLowerCase().equals(getCurrentWordAnswer().toLowerCase())) {
                recordWrongAnswer(attempt);
            } else {
                gameCommands.startNextGame(keyStack.pop());
            }
        }
    }

    public void undoLastLetterAdded() {
        subtractLetterFromAttempt();
    }

    public void recordWrongAnswer(String wrongAnswer) {
        currentWord.addWrongAnswerToSet(wrongAnswer);
    }

//    public void startNextGame() {
//        gameCommands.startNextGame();
//    }

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
}
