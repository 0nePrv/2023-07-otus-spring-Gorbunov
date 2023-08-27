package ru.otus.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.homework.service.processors.ApplicationModeProcessor;

@Service
public class ApplicationRunner {

    private final ApplicationModeService applicationModeService;

    private final ApplicationModeProcessor registrationProcessor;

    private final ApplicationModeProcessor testProcessor;

    private final ApplicationModeProcessor resultProcessor;

    @Autowired
    public ApplicationRunner(@Qualifier("registrationProcessor") ApplicationModeProcessor registrationProcessor,
                             @Qualifier("testProcessor") ApplicationModeProcessor testProcessor,
                             @Qualifier("resultProcessor") ApplicationModeProcessor resultProcessor,
                             ApplicationModeService applicationModeService) {
        this.registrationProcessor = registrationProcessor;
        this.testProcessor = testProcessor;
        this.resultProcessor = resultProcessor;
        this.applicationModeService = applicationModeService;
    }

    public void run() {
        while (applicationModeService.isApplicationRunning()) {
            registrationProcessor.processApplicationMode();
            testProcessor.processApplicationMode();
            resultProcessor.processApplicationMode();
        }
    }
}
