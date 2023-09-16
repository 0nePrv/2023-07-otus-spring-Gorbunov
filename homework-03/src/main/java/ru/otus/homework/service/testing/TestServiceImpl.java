package ru.otus.homework.service.testing;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.InvalidAnswerException;
import ru.otus.homework.exceptions.InvalidTestConfigurationException;
import ru.otus.homework.provider.TestConfigurationProvider;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.localization.LocalizationService;
import ru.otus.homework.service.question.QuestionService;

import java.util.List;


@Service
public class TestServiceImpl implements TestService {

    private final ConversionService conversionService;

    private final IOService ioService;

    private final QuestionService questionService;

    private final LocalizationService localizationService;

    private final TestConfigurationProvider testConfigurationProvider;

    public TestServiceImpl(ConversionService conversionService,
                           IOService ioService,
                           QuestionService questionService,
                           LocalizationService localizationService,
                           TestConfigurationProvider testConfigurationProvider) {
        this.conversionService = conversionService;
        this.ioService = ioService;
        this.questionService = questionService;
        this.localizationService = localizationService;
        this.testConfigurationProvider = testConfigurationProvider;
    }

    @Override
    public TestResult runTest(User user) {
        var totalQuestionsNumber = testConfigurationProvider.getTotalScore();
        var passingScoreNumber = testConfigurationProvider.getPassingScore();
        var questions = questionService.getQuestions();
        checkTestConfiguration(questions.size(), totalQuestionsNumber, passingScoreNumber);
        showTestDescription(user, totalQuestionsNumber, passingScoreNumber);
        var testResult = new TestResult(totalQuestionsNumber, passingScoreNumber, user);
        return completeTest(totalQuestionsNumber, questions, testResult);
    }

    private TestResult completeTest(int totalQuestionsNumber, List<Question> questions, TestResult testResult) {
        int currentQuestionIndex = 0;
        while (currentQuestionIndex < totalQuestionsNumber) {
            try {
                var currentQuestion = questions.get(currentQuestionIndex);
                processQuestion(currentQuestion, currentQuestionIndex, testResult);
                currentQuestionIndex++;
            } catch (InvalidAnswerException exception) {
                printLocalizedMessage("testing.invalid.answer.value");
            } catch (NumberFormatException exception) {
                printLocalizedMessage("testing.invalid.answer.format");
            }
        }
        return testResult;
    }

    private void processQuestion(Question currentQuestion, int currentQuestionIndex, TestResult testResult) {
        showQuestion(currentQuestionIndex, currentQuestion);
        boolean isUserAnswerCorrect = checkAnswer(currentQuestion);
        testResult.addUserAnswer(currentQuestion, isUserAnswerCorrect);
    }

    private void printLocalizedMessage(String messageKey) {
        var message = localizationService.getMessage(messageKey);
        ioService.outputStringLine(message);
    }

    private void checkTestConfiguration(int size, int totalQuestionsNumber, int passingScoreNumber) {
        if (totalQuestionsNumber > size) {
            throw new InvalidTestConfigurationException("Too many test questions: " + totalQuestionsNumber +
                                                        " > " + size);
        }
        if (totalQuestionsNumber <= passingScoreNumber) {
            throw new InvalidTestConfigurationException("Invalid total and passing questions number ratio: " +
                                                        totalQuestionsNumber + " <= " + passingScoreNumber);
        }
    }

    private void showTestDescription(User user, int totalQuestionsNumber, int passingScoreNumber) {
        var testDescription = localizationService.getMessage("testing.description", user.getName(),
                totalQuestionsNumber, passingScoreNumber);
        ioService.readStringWithPrompt(testDescription);
    }

    private void showQuestion(int index, Question question) {
        var questionRepresentation = "#" + (index + 1) + " " + conversionService.convert(question, String.class);
        ioService.outputString(questionRepresentation);
    }

    private boolean checkAnswer(Question currentQuestion) {
        var enterMessage = localizationService.getMessage("testing.enter") + " ";
        var answerIndex = ioService.readIntWithPrompt(enterMessage) - 1;
        var answerList = currentQuestion.getAnswerList();
        if (answerIndex >= answerList.size() || answerIndex < 0) {
            throw new InvalidAnswerException("Answer index is out of range: " + answerIndex);
        }
        return answerList.get(answerIndex).isCorrect();
    }
}