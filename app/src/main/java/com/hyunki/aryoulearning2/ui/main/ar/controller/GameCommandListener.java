package com.hyunki.aryoulearning2.ui.main.ar.controller;

public interface GameCommandListener {
    void startNextGame(String modelKey);

    void addLetterToWordBox(String letter);

    String undoLastLetter();
}