package ru.otus.homework.service.user;

import ru.otus.homework.domain.User;

public interface UserService {
    void registerUser();

    void validateUser(User user);

    User getCurrentUser();
}
