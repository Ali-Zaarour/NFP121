package dev.alizaarour.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected static String id;
    protected String name;
    protected String email;
    protected String psw;

    public boolean authenticate(String email, String psw) {
        return this.email.equals(email) && this.psw.equals(psw);
    }

    public abstract String displayRole();
}
