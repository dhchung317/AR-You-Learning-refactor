package com.hyunki.aryoulearning2.ui.main.ar;

import java.util.HashSet;
import java.util.Set;

public class CurrentWord {
    private final String answer;
    private Set<String> attempts = new HashSet<>();

    public CurrentWord(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public Set<String> getAttempts() {
        return attempts;
    }

    public void addWrongAnswerToSet(String incorrectAttempt){
        attempts.add(incorrectAttempt);
    }
}