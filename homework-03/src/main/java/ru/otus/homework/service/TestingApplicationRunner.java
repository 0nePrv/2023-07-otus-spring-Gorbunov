package ru.otus.homework.service;

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

    public void run(ApplicationArguments arguments) {
        try {
            User user = greetingService.runRegistration();
            TestResult testResult = testService.runTest(user);
            resultPresentingService.outputResult(testResult);
        } catch (InvalidTestConfigurationException exception) {
            String invalidTestConfigMessage = localizationService.getMessage("testing.invalid.configuration");
            outputService.outputString(invalidTestConfigMessage);
        } catch (QuestionDataReadingException exception) {
            String readingErrorMessage = localizationService.getMessage("reading.invalid");
            outputService.outputString(readingErrorMessage);
        }
    }
}