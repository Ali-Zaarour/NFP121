package dev.alizaarour.models;


import java.io.Serial;

public class Image extends CourseContent {
    @Serial
    private static final long serialVersionUID = 1L;

    public Image(String content) {
        super(content);
    }

    @Override
    public String getContent() {
        return "Image: " + content;
    }
}
