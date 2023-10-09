package ru.otus.homework.service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.InvalidTestConfigurationException;
import ru.otus.homework.exceptions.QuestionDataReadingException;
import ru.otus.homework.service.greeting.GreetingService;
import ru.otus.homework.service.localization.LocalizationService;
import ru.otus.homework.service.result.ResultPresentingService;
import ru.otus.homework.service.testing.TestService;

@ShellComponent
public class ShellCommandsController {

    private static final Logger LOGGER = LogManager.getLogger(ShellCommandsController.class);

    private final GreetingService greetingService;

    private final TestService testService;

    private final ResultPresentingService resultPresentingService;

    private final LocalizationService localizationService;

    private User user;

    @Autowired
    public ShellCommandsController(GreetingService greetingService, TestService testService,
                                   ResultPresentingService resultPresentingService,
                                   LocalizationService localizationService) {
        this.greetingService = greetingService;
        this.testService = testService;
        this.resultPresentingService = resultPresentingService;
        this.localizationService = localizationService;
    }

    @ShellMethod(value = "Registration starting command", key = {"l", "login"})
    @ShellMethodAvailability("isRegistrationInProgress")
    public String startRegistration() {
        this.user = greetingService.runRegistration();
        return localizationService.getMessage("greeting.finished");
    }

    @ShellMethod(value = "Test starting command", key = {"t", "test"})
    @ShellMethodAvailability("isRegistered")
    public String startTest() {
        try {
            TestResult testResult = testService.runTest(user);
            resultPresentingService.outputResult(testResult);
        } catch (InvalidTestConfigurationException exception) {
            return processException(exception, "testing.invalid.configuration");
        } catch (QuestionDataReadingException exception) {
            return processException(exception, "reading.invalid");
        }
        return localizationService.getMessage("testing.finished");
    }

    private String processException(Exception exception, String key) {
        LOGGER.catching(Level.ERROR, exception);
        return localizationService.getMessage(key);
    }

    private Availability isRegistered() {
        String reason = localizationService.getMessage("greeting.login");
        return user == null ? Availability.unavailable(reason) : Availability.available();
    }

    private Availability isRegistrationInProgress() {
        String reason = localizationService.getMessage("greeting.finished");
        return user == null ? Availability.available() : Availability.unavailable(reason);
    }
}