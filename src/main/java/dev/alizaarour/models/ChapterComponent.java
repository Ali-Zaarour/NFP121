package dev.alizaarour.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ChapterComponent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected String title;
    protected List<CourseContent> contents;

    public ChapterComponent(String title) {
        this.title = title;
        this.contents = new ArrayList<>();
    }

    public void addContent(CourseContent content) {
        contents.add(content);
    }

    public abstract void display();
}
