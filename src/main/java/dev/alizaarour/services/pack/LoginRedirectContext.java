package dev.alizaarour.services.pack;

import dev.alizaarour.models.helper.Role;
import dev.alizaarour.models.User;

import java.util.HashMap;
import java.util.Map;

public class LoginRedirectContext {
    private final Map<Role, LoginRedirectionStrategy> strategies = new HashMap<>();

    public LoginRedirectContext() {
        strategies.put(Role.STUDENT, new StudentRedirectStrategy());
        strategies.put(Role.TEACHER, new TeacherRedirectStrategy());
    }

    public void redirectUser(User user) {
        strategies.get(user.getRole()).redirect();
    }
}
