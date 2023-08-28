package ru.otus.homework.service.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.InvalidCorrectAnswerException;
import ru.otus.homework.exceptions.fatal.InvalidApplicationModeStateException;
import ru.otus.homework.exceptions.fatal.InvalidTestConfigurationException;
import ru.otus.homework.exceptions.fatal.QuestionDataReadingException;
import ru.otus.homework.exceptions.fatal.QuestionFormatException;
import ru.otus.homework.service.ApplicationModeService;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.question.QuestionService;
import ru.otus.homework.service.test.TestService;

@Component
public class TestProcessor {

    private final TestService testService;

    private final QuestionService questionService;

    private final ApplicationModeService applicationModeService;

    private final IOService ioService;

    @Autowired
    public TestProcessor(TestService testService,
                         ApplicationModeService applicationModeService,
                         IOService ioService,
                         QuestionService questionService) {
        this.testService = testService;
        this.applicationModeService = applicationModeService;
        this.ioService = ioService;
        this.questionService = questionService;
    }

    public TestResult processTesting(User user) {
        if (applicationModeService.isTestProcessingRunning()) {
            var testResult = checkTestConfiguration(user);
            int currentQuestionIndex = 0;
            while (applicationModeService.isTestProcessingRunning()) {
                try {
                    currentQuestionIndex = processQuestion(testResult, currentQuestionIndex);
                } catch (InvalidCorrectAnswerException exception) {
                    processException(exception.getMessage(), false);
                } catch (NumberFormatException exception) {
                    processException("Invalid number entered", false);
                } catch (QuestionFormatException | QuestionDataReadingException exception) {
                    processException(exception.getMessage(), true);
                }
            }
            return testResult;
        } else {
            throw new InvalidApplicationModeStateException("Attempt to test user without test mode running");
        }
    }

    private TestResult checkTestConfiguration(User user) {
        try {
            int totalQuestionQuantity = questionService.getQuantity();
            testService.validateTestConfiguration(totalQuestionQuantity);
        } catch (QuestionFormatException | QuestionDataReadingException |
                 InvalidTestConfigurationException exception) {
            processException(exception.getMessage(), true);
        }
        return new TestResult(testService.getTotalQuestionsNumber(),
                testService.getPassingScoreNumber(), user);
    }

    private void processException(String exceptionMsg, boolean isFatal) {
        var exceptionStr = testService.getLineInExceptionFormat(exceptionMsg, isFatal);
        ioService.outputStringLine(exceptionStr);
        if (isFatal) {
            applicationModeService.stopApplication();
        }
    }

    private int processQuestion(TestResult testResult, int currentQuestionIndex) {
        if (currentQuestionIndex < testService.getTotalQuestionsNumber()) {
            var currentQuestion = questionService.getQuestion(currentQuestionIndex);
            var questionRepresentation = testService.getQuestionRepresentation(currentQuestionIndex, currentQuestion);
            ioService.outputString(questionRepresentation);
            var answerIdx = ioService.readIntWithPrompt("Enter answer: ") - 1;
            boolean isUserAnswerCorrect = testService.validateAnswer(currentQuestion, answerIdx);
            testResult.addUserAnswer(currentQuestion, isUserAnswerCorrect);
            currentQuestionIndex++;
        } else {
            applicationModeService.stopTestProcessing();
        }
        return currentQuestionIndex;
    }
}
