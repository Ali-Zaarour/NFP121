package dev.alizaarour.services;

import dev.alizaarour.config.SessionManager;
import dev.alizaarour.config.pack.ApplicationInitializer;
import dev.alizaarour.models.User;
import dev.alizaarour.utils.Observable;
import lombok.Getter;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public class UserService extends Observable {
    private static final UserService instance = new UserService();

    private UserService() {
    }

    public static synchronized UserService getInstance() {
        return instance;
    }

    public User getActiveUser() {
        return findUserWithCondition(
                u -> u.getEmail().equals(SessionManager.getInstance().getUser().getEmail())
        ).orElse(null);
    }

    public Optional<User> findUserWithCondition(Predicate<User> condition) {
        return ApplicationInitializer.dataSchema.getUsers().stream()
                .filter(condition)
                .findFirst();
    }

    public boolean updateUserField(String field, String newValue) {
        User activeUser = getActiveUser();
        if (activeUser == null) return false;

        if (field.equals("name")) {
            if (UserService.getInstance().findUserWithCondition(u -> u.getName().equals(newValue)).isEmpty())
                activeUser.setName(newValue);
            else {
                JOptionPane.showMessageDialog(null, "Name already exist!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        } else if (field.equals("password")) {
            activeUser.setPsw(newValue);
        }

        notifyObservers();
        return true;
    }

    public <T extends User> List<T> getUsersByType(Class<T> type) {
        return ApplicationInitializer.dataSchema.getUsers().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }
}
