package ru.otus.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.InvalidTestConfigurationException;
import ru.otus.homework.exceptions.QuestionDataReadingException;
import ru.otus.homework.service.greeting.GreetingService;
import ru.otus.homework.service.io.OutputService;
import ru.otus.homework.service.result.ResultPresentingService;
import ru.otus.homework.service.testing.TestService;

@Service
public class ApplicationRunner {
    private final GreetingService greetingService;

    private final TestService testService;

    private final ResultPresentingService resultPresentingService;

    private final OutputService outputService;

    @Autowired
    public ApplicationRunner(GreetingService greetingService,
                             TestService testService,
                             ResultPresentingService resultPresentingService,
                             OutputService outputService) {
        this.greetingService = greetingService;
        this.testService = testService;
        this.resultPresentingService = resultPresentingService;
        this.outputService = outputService;
    }

    public void run() {
        try {
            testService.checkTestConfiguration();
            User user = greetingService.runRegistration();
            TestResult testResult = testService.runTest(user);
            resultPresentingService.outputResult(testResult);
        } catch (InvalidTestConfigurationException exception) {
            outputService.outputString("Invalid test configuration");
        } catch (QuestionDataReadingException exception) {
            outputService.outputString("Error occurred while reading questions");
        }
    }
}
