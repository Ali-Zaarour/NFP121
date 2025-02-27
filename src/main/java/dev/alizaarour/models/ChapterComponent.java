package dev.alizaarour.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class ChapterComponent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    protected String title;
    protected List<CourseContent> contents;

    public ChapterComponent(String title) {
        this.title = title;
        this.contents = new ArrayList<>();
    }

    public abstract void display();
}
