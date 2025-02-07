package dev.alizaarour.models;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class Chapter extends ChapterComponent {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<ChapterComponent> subChapters;

    public Chapter(String title) {
        super(title);
        this.subChapters = new ArrayList<>();
    }

    public void addSubChapter(ChapterComponent chapter) {
        subChapters.add(chapter);
    }

    @Override
    public void display() {
        System.out.println("Chapter: " + title);
        for (CourseContent content : contents) {
            System.out.println(content.getContent());
        }
        for (ChapterComponent chapter : subChapters) {
            chapter.display();
        }
    }
}
