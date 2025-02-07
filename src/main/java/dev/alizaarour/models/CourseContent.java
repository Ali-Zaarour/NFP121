package dev.alizaarour.models;


import java.io.Serial;
import java.io.Serializable;

public abstract class CourseContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected String content;

    public CourseContent(String content) {
        this.content = content;
    }

    public abstract String getContent();
}
