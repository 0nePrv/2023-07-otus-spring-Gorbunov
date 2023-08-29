package ru.otus.homework.service.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.exceptions.fatal.IllegalApplicationStateException;
import ru.otus.homework.service.ApplicationModeService;
import ru.otus.homework.service.io.OutputService;
import ru.otus.homework.service.test.TestService;

@Service
public class ResultProcessor {

    private final TestService testService;

    private final OutputService outputService;

    private final ApplicationModeService applicationModeService;


    @Autowired
    public ResultProcessor(TestService testService,
                           OutputService outputService,
                           ApplicationModeService applicationModeService) {
        this.testService = testService;
        this.outputService = outputService;
        this.applicationModeService = applicationModeService;
    }

    public void processResult(TestResult testResult) {
        if (applicationModeService.isResultProcessingRunning()) {
            String testResultRepresentation = testService.getTestResultRepresentation(testResult);
            outputService.outputStringLine(testResultRepresentation);
            applicationModeService.stopApplication();
        } else {
            applicationModeService.stopApplication();
            throw new IllegalApplicationStateException
                    ("Inner error occurred: attempt to get result without result mode running");
        }
    }
}
