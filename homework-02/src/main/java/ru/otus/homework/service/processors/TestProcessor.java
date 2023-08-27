package ru.otus.homework.service.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.exceptions.InvalidCorrectAnswerException;
import ru.otus.homework.exceptions.InvalidTestConfigurationException;
import ru.otus.homework.exceptions.QuestionDataReadingException;
import ru.otus.homework.exceptions.QuestionFormatException;
import ru.otus.homework.service.ApplicationModeService;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.test.TestService;

@Service
public class TestProcessor implements CommandProcessor {

    private final TestService testService;

    private final ApplicationModeService applicationModeService;

    private final IOService ioService;

    private int currentQuestionIndex;

    @Autowired
    public TestProcessor(TestService testService,
                         ApplicationModeService applicationModeService,
                         IOService ioService) {
        this.testService = testService;
        this.applicationModeService = applicationModeService;
        this.ioService = ioService;
    }

    @Override
    public void processCommand() {
        currentQuestionIndex = 0;
        while (applicationModeService.isTestProcessingRunning()) {
            try {
                testService.validateTestConfiguration();
                processQuestion();
            } catch (InvalidCorrectAnswerException exception) {
                ioService.outputExceptionLine(exception.getMessage());
            } catch (NumberFormatException exception) {
                ioService.outputExceptionLine("Invalid number entered");
            } catch (QuestionFormatException | QuestionDataReadingException |
                     InvalidTestConfigurationException exception) {
                ioService.outputExceptionLine(exception.getMessage());
                applicationModeService.stopApplication();
            }
        }
    }

    private void processQuestion() {
        if (currentQuestionIndex < testService.getTotalNumberOfQuestions()) {
            String questionRepresentation =
                    testService.getQuestionRepresentation(currentQuestionIndex);
            ioService.outputStringLine(questionRepresentation);
            int answerIdx = ioService.readIntWithPrompt("Enter answer: ") - 1;
            testService.validateAnswer(currentQuestionIndex, answerIdx);
            currentQuestionIndex++;
        } else {
            applicationModeService.stopTestProcessing();
        }
    }
}
