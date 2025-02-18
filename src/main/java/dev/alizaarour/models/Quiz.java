package dev.alizaarour.models;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

public class Quiz implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private Question[] questions;

    public Quiz() {
        this.questions = new Question[10];
    }

    public boolean addQuestion(Question question) {
        for (int i = 0; i < questions.length; i++) {
            if (questions[i] == null) {
                questions[i] = question;
                return true;
            }
        }
        return false;
    }

    public boolean removeQuestion(int index) {
        if (index < 0 || index >= questions.length || questions[index] == null) {
            return false;
        }

        // Remove question and shift elements left
        for (int i = index; i < questions.length - 1; i++) {
            questions[i] = questions[i + 1];
        }
        questions[questions.length - 1] = null;
        return true;
    }
}
