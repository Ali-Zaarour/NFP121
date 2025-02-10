package dev.alizaarour.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Level implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int num; //level number
    private int fees;
    private List<Chapter> chapters;
    private Quiz quiz;


    public Level(int num, int fees) {
        this.num = num;
        this.fees = fees;
        this.chapters = new ArrayList<>();
    }


    public void display() {
        System.out.println("Level details { num: " + num + ", fees: " + fees + " $ }");
        for (Chapter chapter : chapters) {
            chapter.display();
        }
    }
}
