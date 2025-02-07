package dev.alizaarour.models;

import java.io.Serial;

public class Video extends CourseContent {
    @Serial
    private static final long serialVersionUID = 1L;

    public Video(String content) {
        super(content);
    }

    @Override
    public String getContent() {
        return "Video: " + content;
    }
}
