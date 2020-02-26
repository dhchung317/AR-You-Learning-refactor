package com.hyunki.aryoulearning2.model;

import java.util.HashSet;
import java.util.Set;

public class CurrentWord {
    private final String answer;
    private String attempt = "";
    private Set<String> attempts = new HashSet<>();

    public CurrentWord(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAttempt() {
        return attempt;
    }

    public void addLetterToAttempt(String letter){
        this.attempt += letter;
    }

    public void subtractLetterFromAttempt(){
        if(!attempt.isEmpty()){
            attempt = attempt.substring(0,attempt.length() - 1);
        }
    }

    public Set<String> getAttempts() {
        return attempts;
    }

    public void addWrongAnswerToSet(String incorrectAttempt){
        attempts.add(incorrectAttempt);
    }
}
