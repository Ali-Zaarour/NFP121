package dev.alizaarour.models;

import java.io.Serial;

public class Document extends CourseContent {

    @Serial
    private static final long serialVersionUID = 1L;

    public Document(String content) {
        super(content);
    }

    @Override
    public String getContent() {
        return "Document: " + content;
    }
}
