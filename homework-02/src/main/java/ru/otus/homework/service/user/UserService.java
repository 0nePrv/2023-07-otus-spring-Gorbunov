package ru.otus.homework.service.user;

import ru.otus.homework.domain.User;

public interface UserService {
    void validateUser(User user);

    String getNewUserGreeting();
}
