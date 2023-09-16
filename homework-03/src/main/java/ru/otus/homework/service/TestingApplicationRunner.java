package ru.otus.homework.service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.InvalidTestConfigurationException;
import ru.otus.homework.exceptions.QuestionDataReadingException;
import ru.otus.homework.service.greeting.GreetingService;
import ru.otus.homework.service.io.OutputService;
import ru.otus.homework.service.localization.LocalizationService;
import ru.otus.homework.service.result.ResultPresentingService;
import ru.otus.homework.service.testing.TestService;

@Component
public class TestingApplicationRunner implements ApplicationRunner {

    private static final Logger LOGGER = LogManager.getLogger(TestingApplicationRunner.class);

    private final GreetingService greetingService;

    private final TestService testService;

    private final ResultPresentingService resultPresentingService;

    private final OutputService outputService;

    private final LocalizationService localizationService;

    @Autowired
    public TestingApplicationRunner(GreetingService greetingService,
                                    TestService testService,
                                    ResultPresentingService resultPresentingService,
                                    OutputService outputService,
                                    LocalizationService localizationService) {
        this.greetingService = greetingService;
        this.testService = testService;
        this.resultPresentingService = resultPresentingService;
        this.outputService = outputService;
        this.localizationService = localizationService;
    }

    @Override
    public void run(ApplicationArguments arguments) {
        try {
            User user = greetingService.runRegistration();
            TestResult testResult = testService.runTest(user);
            resultPresentingService.outputResult(testResult);
        } catch (InvalidTestConfigurationException exception) {
            processException(exception, "testing.invalid.configuration");
        } catch (QuestionDataReadingException exception) {
            processException(exception, "reading.invalid");
        }
    }

    private void processException(Exception exception, String messageKey) {
        String message = localizationService.getMessage(messageKey);
        outputService.outputStringLine(message);
        LOGGER.catching(Level.ERROR, exception);
    }
}