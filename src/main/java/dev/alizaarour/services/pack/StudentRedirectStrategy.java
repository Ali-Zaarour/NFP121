package dev.alizaarour.services.pack;

import dev.alizaarour.views.StudentHome;

public class StudentRedirectStrategy implements LoginRedirectionStrategy {

    @Override
    public void redirect() {
        new StudentHome();
    }
}
