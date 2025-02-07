package dev.alizaarour.models;


import java.io.Serial;

public class SubChapter extends ChapterComponent {
    @Serial
    private static final long serialVersionUID = 1L;

    public SubChapter(String title) {
        super(title);
    }

    @Override
    public void display() {
        System.out.println("Sub-Chapter: " + title);
        for (CourseContent content : contents) {
            System.out.println(content.getContent());
        }
    }
}
