package dev.alizaarour.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String questionText;
    private List<String> choices;
    private final int correctChoiceIndex;

    public Question(String questionText, List<String> choices, int correctChoiceIndex) {
        this.questionText = questionText;
        this.choices = choices;
        this.correctChoiceIndex = correctChoiceIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getChoices() {
        return choices;
    }

    public int getCorrectChoiceIndex() {
        return correctChoiceIndex;
    }

    public String getCorrectAnswer() {
        return choices.get(correctChoiceIndex);
    }
}
