package ru.otus.homework.service.testing;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.homework.config.ApplicationPropertiesHolder;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.InvalidAnswerException;
import ru.otus.homework.exceptions.InvalidTestConfigurationException;
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

    private final int totalQuestionsNumber;

    private final int passingScoreNumber;

    public TestServiceImpl(ConversionService conversionService,
                           IOService ioService,
                           QuestionService questionService,
                           LocalizationService localizationService,
                           ApplicationPropertiesHolder applicationPropertiesHolder) {
        this.conversionService = conversionService;
        this.ioService = ioService;
        this.questionService = questionService;
        this.localizationService = localizationService;
        this.totalQuestionsNumber = applicationPropertiesHolder.getTotalScore();
        this.passingScoreNumber = applicationPropertiesHolder.getPassingScore();
    }

    @Override
    public TestResult runTest(User user) {
        List<Question> questions = questionService.getQuestions();
        checkTestConfiguration(questions.size());
        showTestDescription(user);
        TestResult testResult = new TestResult(totalQuestionsNumber, passingScoreNumber, user);
        int currentQuestionIndex = 0;
        while (currentQuestionIndex < totalQuestionsNumber) {
            try {
                Question currentQuestion = questions.get(currentQuestionIndex);
                showQuestion(currentQuestionIndex, currentQuestion);
                boolean isUserAnswerCorrect = checkAnswer(currentQuestion);
                testResult.addUserAnswer(currentQuestion, isUserAnswerCorrect);
                currentQuestionIndex++;
            } catch (InvalidAnswerException exception) {
                String invalidAnswerValueMessage = localizationService.getMessage("testing.invalid.answer.value");
                ioService.outputStringLine(invalidAnswerValueMessage);
            } catch (NumberFormatException exception) {
                String invalidAnswerFormatMessage = localizationService.getMessage("testing.invalid.answer.format");
                ioService.outputStringLine(invalidAnswerFormatMessage);
            }
        }
        return testResult;
    }

    private void checkTestConfiguration(int size) {
        if (totalQuestionsNumber > size) {
            throw new InvalidTestConfigurationException("Too many test questions: " + totalQuestionsNumber +
                                                        " > " + size);
        }
        if (totalQuestionsNumber <= passingScoreNumber) {
            throw new InvalidTestConfigurationException("Invalid total and passing questions number ratio: " +
                                                        totalQuestionsNumber + " <= " + passingScoreNumber);
        }
    }

    private void showTestDescription(User user) {
        String testDescription = localizationService.getMessage("testing.description", user.getName(),
                totalQuestionsNumber, passingScoreNumber);
        ioService.readStringWithPrompt(testDescription);
    }

    private void showQuestion(int index, Question question) {
        var questionRepresentation = "#" + (index + 1) + " " + conversionService.convert(question, String.class);
        ioService.outputString(questionRepresentation);
    }

    private boolean checkAnswer(Question currentQuestion) {
        String enterMessage = localizationService.getMessage("testing.enter") + " ";
        var answerIndex = ioService.readIntWithPrompt(enterMessage) - 1;
        var answerList = currentQuestion.getAnswerList();
        if (answerIndex >= answerList.size() || answerIndex < 0) {
            throw new InvalidAnswerException("Answer index is out of range: " + answerIndex);
        }
        return answerList.get(answerIndex).isCorrect();
    }
}