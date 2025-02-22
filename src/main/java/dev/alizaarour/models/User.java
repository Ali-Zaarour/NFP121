package dev.alizaarour.models;

import dev.alizaarour.models.helper.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public abstract class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected int userId;
    protected String name;
    protected String email;
    protected String psw;
    protected Role role;

}
