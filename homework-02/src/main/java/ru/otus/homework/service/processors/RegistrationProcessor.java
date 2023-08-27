package ru.otus.homework.service.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.exceptions.UnsupportedValidationTypeException;
import ru.otus.homework.exceptions.UserValidationException;
import ru.otus.homework.service.ApplicationModeService;
import ru.otus.homework.service.io.OutputService;
import ru.otus.homework.service.user.UserService;

@Service
public class RegistrationProcessor implements ApplicationModeProcessor {

    private final ApplicationModeService applicationModeService;

    private final OutputService outputService;

    private final UserService userService;

    @Autowired
    public RegistrationProcessor(ApplicationModeService applicationModeService,
                                 OutputService outputService, UserService registerUser) {
        this.applicationModeService = applicationModeService;
        this.outputService = outputService;
        this.userService = registerUser;
    }

    @Override
    public void processApplicationMode() {
        while (applicationModeService.isRegistrationRunning()) {
            try {
                userService.registerUser();
                applicationModeService.stopRegistrationProcessing();
            } catch (UserValidationException exception) {
                outputService.outputExceptionLine(exception.getMessage());
            } catch (UnsupportedValidationTypeException exception) {
                outputService.outputExceptionLine(exception.getMessage());
                applicationModeService.stopApplication();
            }
        }
    }
}
