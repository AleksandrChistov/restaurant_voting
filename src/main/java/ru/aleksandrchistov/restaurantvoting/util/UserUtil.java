package ru.aleksandrchistov.restaurantvoting.util;

import lombok.experimental.UtilityClass;
import ru.aleksandrchistov.restaurantvoting.model.Role;
import ru.aleksandrchistov.restaurantvoting.model.User;
import ru.aleksandrchistov.restaurantvoting.to.UserTo;

import static ru.aleksandrchistov.restaurantvoting.config.SecurityConfiguration.PASSWORD_ENCODER;

@UtilityClass
public class UserUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static User prepareToSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}