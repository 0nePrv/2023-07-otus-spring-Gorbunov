package ru.otus.homework.service.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.fatal.InvalidApplicationModeStateException;
import ru.otus.homework.exceptions.UserValidationException;
import ru.otus.homework.exceptions.fatal.UnsupportedValidationTypeException;
import ru.otus.homework.service.ApplicationModeService;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.test.TestService;
import ru.otus.homework.service.user.UserService;

@Component
public class RegistrationProcessor {

    private final ApplicationModeService applicationModeService;

    private final UserService userService;

    private final TestService testService;

    private final IOService ioService;

    @Autowired
    public RegistrationProcessor(ApplicationModeService applicationModeService,
                                 UserService userService,
                                 TestService testService,
                                 IOService ioService) {
        this.applicationModeService = applicationModeService;
        this.userService = userService;
        this.testService = testService;
        this.ioService = ioService;
    }

    public User processRegistration() {
        if (applicationModeService.isRegistrationRunning()) {
            User user = null;
            while (applicationModeService.isRegistrationRunning()) {
                user = getNewUser();
                try {
                    userService.validateUser(user);
                    applicationModeService.stopRegistrationProcessing();
                } catch (UserValidationException exception) {
                    processException(exception, false);
                } catch (UnsupportedValidationTypeException exception) {
                    processException(exception, true);
                }
            }
            return user;
        } else {
            throw new InvalidApplicationModeStateException
                    ("Attempt to register user without registration mode running");
        }
    }

    private void processException(RuntimeException exception, boolean isFatal) {
        var exceptionStr = testService.getLineInExceptionFormat(exception.getMessage(), isFatal);
        ioService.outputStringLine(exceptionStr);
        if (isFatal) {
            applicationModeService.stopApplication();
        }
    }

    private User getNewUser() {
        var name = ioService.readStringWithPrompt("Enter your name: ").trim();
        var surname = ioService.readStringWithPrompt("Enter your surname: ").trim();
        return new User(name, surname);
    }
}
