package dev.alizaarour.models;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDateTime date;
    private Teacher instructor;
    private List<Student> students = new ArrayList<>();
}
