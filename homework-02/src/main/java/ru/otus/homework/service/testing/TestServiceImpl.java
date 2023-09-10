package ru.otus.homework.service.testing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.InvalidAnswerException;
import ru.otus.homework.exceptions.InvalidTestConfigurationException;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.question.QuestionService;

import java.util.List;


@Service
public class TestServiceImpl implements TestService {

    private static final String TEST_DESCRIPTION_FORMAT = """
            Hi, %s!
            Before you get started, here's some important information:
               • You'll be tackling a test that consists of %d questions.
               • To successfully pass it you should gain %d correct answers.
               • Good luck on your testing adventure ☺
            To start press Enter
            """;

    private final ConversionService conversionService;

    private final IOService ioService;

    private final QuestionService questionService;

    private final int totalQuestionsNumber;

    private final int passingScoreNumber;

    public TestServiceImpl(ConversionService conversionService,
                           IOService ioService,
                           QuestionService questionService,
                           @Value("${test.total}") int totalQuestionsNumber,
                           @Value("${test.passing}") int passingScoreNumber) {
        this.conversionService = conversionService;
        this.ioService = ioService;
        this.questionService = questionService;
        this.totalQuestionsNumber = totalQuestionsNumber;
        this.passingScoreNumber = passingScoreNumber;
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
                ioService.outputStringLine("Answer index is out of range");
            } catch (NumberFormatException exception) {
                ioService.outputStringLine("Invalid number entered");
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
            throw new InvalidTestConfigurationException("Invalid total and passing questions number ratio");
        }
    }

    private void showTestDescription(User user) {
        String testDescription = String.format(TEST_DESCRIPTION_FORMAT, user.getName(),
                totalQuestionsNumber, passingScoreNumber);
        ioService.readStringWithPrompt(testDescription);
    }

    private void showQuestion(int index, Question question) {
        var questionRepresentation = "#" + (index + 1) + " " + conversionService.convert(question, String.class);
        ioService.outputString(questionRepresentation);
    }

    private boolean checkAnswer(Question currentQuestion) {
        var answerIndex = ioService.readIntWithPrompt("Enter answer: ") - 1;
        var answerList = currentQuestion.getAnswerList();
        if (answerIndex >= answerList.size() || answerIndex < 0) {
            throw new InvalidAnswerException("Answer index is out of range");
        }
        return answerList.get(answerIndex).isCorrect();
    }
}