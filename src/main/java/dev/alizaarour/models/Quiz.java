package dev.alizaarour.models;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class Quiz implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Question[] questions;

    public Quiz() {
        this.questions = new Question[10];
    }

    public void addQuestion(Question question) {
        for (int i = 0; i < questions.length; i++) {
            if (questions[i] == null) {
                questions[i] = question;
                return;
            }
        }
    }
}
