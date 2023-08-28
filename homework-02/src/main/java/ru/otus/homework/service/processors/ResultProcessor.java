package ru.otus.homework.service.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.exceptions.fatal.InvalidApplicationModeStateException;
import ru.otus.homework.service.ApplicationModeService;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.test.TestService;

@Component
public class ResultProcessor {

    private final TestService testService;

    private final IOService ioService;

    private final ApplicationModeService applicationModeService;


    @Autowired
    public ResultProcessor(TestService testService,
                           IOService ioService,
                           ApplicationModeService applicationModeService) {
        this.testService = testService;
        this.ioService = ioService;
        this.applicationModeService = applicationModeService;
    }

    public void processResult(TestResult testResult) {
        if (applicationModeService.isResultProcessingRunning()) {
            String testResultRepresentation = testService.getTestResultRepresentation(testResult);
            ioService.outputStringLine(testResultRepresentation);
            applicationModeService.stopApplication();
        } else {
            throw new InvalidApplicationModeStateException("Attempt to get result without result mode running");
        }
    }
}
