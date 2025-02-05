package dev.alizaarour.models;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;
    private List<Course> courses;

}
