package ru.otus.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.homework.service.processors.CommandProcessor;

@Service
public class ApplicationRunner {

    private final ApplicationModeService applicationModeService;

    private final CommandProcessor registrationProcessor;

    private final CommandProcessor testProcessor;

    private final CommandProcessor resultProcessor;

    @Autowired
    public ApplicationRunner(@Qualifier("registrationProcessor") CommandProcessor registrationProcessor,
                             @Qualifier("testProcessor") CommandProcessor testProcessor,
                             @Qualifier("resultProcessor") CommandProcessor resultProcessor,
                             ApplicationModeService applicationModeService) {
        this.registrationProcessor = registrationProcessor;
        this.testProcessor = testProcessor;
        this.resultProcessor = resultProcessor;
        this.applicationModeService = applicationModeService;
    }

    public void run() {
        while (applicationModeService.isApplicationRunning()) {
            registrationProcessor.processCommand();
            testProcessor.processCommand();
            resultProcessor.processCommand();
        }
    }
}
