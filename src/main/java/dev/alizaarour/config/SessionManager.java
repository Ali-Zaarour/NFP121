package dev.alizaarour.config;

import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.models.User;
import dev.alizaarour.services.LoginRedirectContext;
import dev.alizaarour.views.BaseFrame;
import dev.alizaarour.views.Login;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
public class SessionManager {

    private static SessionManager currentSessionManager;
    private User user;

    private SessionManager() {
    }

    public static synchronized SessionManager getInstance() {
        if (currentSessionManager == null) currentSessionManager = new SessionManager();
        return currentSessionManager;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public boolean isAuthenticate() {
        return this.user != null;
    }

    public void login(BaseFrame logInFrame, String email, String psw) {
        var currentUser = ApplicationInitializer.dataSchema.findUserWithCondition(u -> u.getEmail().equals(email));
        if (currentUser.isPresent()) {
            if (currentUser.get().getPsw().equals(psw)) {
                this.user = currentUser.get();
                logInFrame.dispose();
                new LoginRedirectContext().redirectUser(this.user);
            } else
                JOptionPane.showMessageDialog(logInFrame, "Password is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
        } else
            JOptionPane.showMessageDialog(logInFrame, "Email is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void logout(BaseFrame currentFrame) {
        this.user = null;
        currentFrame.dispose();
        new Login();
    }


}
