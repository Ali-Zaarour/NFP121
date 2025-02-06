package dev.alizaarour.utils;

import dev.alizaarour.models.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
public class DataSchema implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<User> users;

    public DataSchema() {
        this.users = new ArrayList<>();  // Initialize with an empty list
    }

    public <T extends User> List<T> getUsersByType(Class<T> type) {
        return users.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    public Optional<User> findUserWithCondition(Predicate<User> condition) {
        return users.stream()
                .filter(condition)
                .findFirst();
    }
}
