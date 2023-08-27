package ru.otus.homework.service.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.InvalidCorrectAnswerException;
import ru.otus.homework.exceptions.InvalidTestConfigurationException;
import ru.otus.homework.service.question.QuestionService;
import ru.otus.homework.service.user.UserService;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    private final QuestionService questionService;

    private final UserService userService;

    private final ConversionService conversionService;

    private final TestResult testResult;

    public TestServiceImpl(QuestionService questionService,
                           UserService userService,
                           ConversionService conversionService,
                           @Value("${total.test.number}") int totalQuestionsNumber,
                           @Value("${passing.score.number}") int passingScoreNumber) {
        this.questionService = questionService;
        this.userService = userService;
        this.conversionService = conversionService;
        testResult = new TestResult(totalQuestionsNumber, passingScoreNumber);
    }

    @Override
    public void validateAnswer(int questionIndex, int answerIndex) {
        Question targetQuestion = questionService.getQuestion(questionIndex);
        List<Answer> answerList = targetQuestion.getAnswerList();
        if (answerIndex >= answerList.size() || answerIndex < 0) {
            throw new InvalidCorrectAnswerException("Answer index is out of range");
        }
        testResult.addUserAnswer(targetQuestion, answerList.get(answerIndex).isCorrect());
    }

    @Override
    public String getQuestionRepresentation(int questionIndex) {
        Question question = questionService.getQuestion(questionIndex);
        return "\n#" + ++questionIndex + " " + conversionService.convert(question, String.class);
    }

    @Override
    public String getTestResultRepresentation() {
        User currentUser = userService.getCurrentUser();
        testResult.setUser(currentUser);
        return conversionService.convert(testResult, String.class);
    }

    @Override
    public int getTotalNumberOfQuestions() {
        return testResult.getTotalQuestionsNumber();
    }

    @Override
    public void restartTest() {
        questionService.refreshQuestions();
        testResult.clearTestMap();
    }

    @Override
    public void validateTestConfiguration() {
        int total = testResult.getTotalQuestionsNumber();
        int quantity = questionService.getQuantity();
        int passing = testResult.getPassingScoreNumber();
        if (total > quantity) {
            throw new InvalidTestConfigurationException("Too many test questions: " + total + " > " + quantity);
        }
        if (total <= passing) {
            throw new InvalidTestConfigurationException("Invalid total and passing questions number ratio");
        }
    }
}
