package ru.otus.homework.service.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.service.ApplicationModeService;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.test.TestService;

@Service
public class ResultProcessor implements CommandProcessor {

    private final TestService testService;

    private final IOService ioService;

    private final ApplicationModeService applicationModeService;


    @Autowired
    public ResultProcessor(TestService testService, IOService ioService,
                           ApplicationModeService applicationModeService) {
        this.testService = testService;
        this.ioService = ioService;
        this.applicationModeService = applicationModeService;
    }

    @Override
    public void processCommand() {
        if (applicationModeService.isResultProcessingRunning()) {
            String testResult = testService.getTestResultRepresentation();
            ioService.outputStringLine(testResult);
            suggestToRestart();
        }
    }

    private void suggestToRestart() {
        while (applicationModeService.isResultProcessingRunning()) {
            String again = ioService.readStringWithPrompt("Try again? [Y/N] ");
            if (again.equalsIgnoreCase("n")) {
                applicationModeService.stopApplication();
            }
            if (again.equalsIgnoreCase("y")) {
                applicationModeService.restartTestProcessing();
                testService.restartTest();
            }
        }
    }
}
