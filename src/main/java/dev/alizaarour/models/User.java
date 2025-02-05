package dev.alizaarour.models;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public abstract class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String name;
    protected String email;
    protected String psw;

    public boolean authenticate(String email, String psw){
        return this.email.equals(email) && this.psw.equals(psw);
    }

    public abstract String displayRole();
}
