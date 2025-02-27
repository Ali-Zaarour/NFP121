package dev.alizaarour.models;

import lombok.Getter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
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

    public List<CourseContent> getAllContents() {
        List<CourseContent> allContents = new ArrayList<>(this.contents);
        for (ChapterComponent subChapter : subChapters) {
            if (subChapter instanceof Chapter) {
                allContents.addAll(((Chapter) subChapter).getAllContents());
            }
        }
        return allContents;
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
