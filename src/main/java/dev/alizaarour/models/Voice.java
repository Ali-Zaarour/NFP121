package dev.alizaarour.models;

import java.io.Serial;

public class Voice extends CourseContent {

    @Serial
    private static final long serialVersionUID = 1L;

    public Voice(String content) {
        super(content);
    }

    @Override
    public String getContent() {
        return "Voice: " + content;
    }
}