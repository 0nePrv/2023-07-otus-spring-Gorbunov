package ru.otus.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.fatal.InvalidApplicationModeStateException;
import ru.otus.homework.service.io.OutputService;
import ru.otus.homework.service.processors.RegistrationProcessor;
import ru.otus.homework.service.processors.ResultProcessor;
import ru.otus.homework.service.processors.TestProcessor;

@Service
public class ApplicationRunner {
    private final RegistrationProcessor registrationProcessor;

    private final TestProcessor testProcessor;

    private final ResultProcessor resultProcessor;

    private final OutputService outputService;

    private final ApplicationModeService applicationModeService;

    @Autowired
    public ApplicationRunner(RegistrationProcessor registrationProcessor,
                             TestProcessor testProcessor,
                             ResultProcessor resultProcessor,
                             OutputService outputService,
                             ApplicationModeService applicationModeService) {
        this.registrationProcessor = registrationProcessor;
        this.testProcessor = testProcessor;
        this.resultProcessor = resultProcessor;
        this.outputService = outputService;
        this.applicationModeService = applicationModeService;
    }

    public void run() {
        try {
            User user = registrationProcessor.processRegistration();
            TestResult testResult = testProcessor.processTesting(user);
            resultProcessor.processResult(testResult);
        } catch (InvalidApplicationModeStateException exception) {
            if (applicationModeService.isApplicationRunning()) {
                outputService.outputString("Application mode state error: " + exception.getMessage());
            }
        }
    }
}
