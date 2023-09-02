package ru.otus.homework.service.testing;

import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;

public interface TestService {

    TestResult runTest(User user);

    void checkTestConfiguration();
}
