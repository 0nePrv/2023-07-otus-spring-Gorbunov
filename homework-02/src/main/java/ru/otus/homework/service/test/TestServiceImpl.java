package ru.otus.homework.service.test;

import org.springframework.core.convert.ConversionService;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.exceptions.InvalidCorrectAnswerException;
import ru.otus.homework.exceptions.InvalidTestConfigurationException;
import ru.otus.homework.service.user.UserService;


public class TestServiceImpl implements TestService {

    private final QuestionDao questionDao;

    private final UserService userService;

    private final ConversionService conversionService;

    private final TestResult testResult;

    public TestServiceImpl(QuestionDao questionDao, UserService userService,
                           ConversionService conversionService,
                           int totalQuestionsNumber, int passingScoreNumber) {
        this.questionDao = questionDao;
        this.userService = userService;
        this.conversionService = conversionService;
        testResult = new TestResult(totalQuestionsNumber, passingScoreNumber);
    }

    @Override
    public void validateAnswer(int questionIndex, int answerIndex) {
        var targetQuestion = questionDao.getQuestion(questionIndex);
        var answerList = targetQuestion.getAnswerList();
        if (answerIndex >= answerList.size() || answerIndex < 0) {
            throw new InvalidCorrectAnswerException("Answer index is out of range");
        }
        testResult.addUserAnswer(targetQuestion, answerList.get(answerIndex).isCorrect());
    }

    @Override
    public String getQuestionRepresentation(int questionIndex) {
        var question = questionDao.getQuestion(questionIndex);
        return "\n#" + ++questionIndex + " " + conversionService.convert(question, String.class);
    }

    @Override
    public String getTestResultRepresentation() {
        var currentUser = userService.getCurrentUser();
        testResult.setUser(currentUser);
        return conversionService.convert(testResult, String.class);
    }

    @Override
    public int getTotalNumberOfQuestions() {
        return testResult.getTotalQuestionsNumber();
    }

    @Override
    public void restartTest() {
        questionDao.refreshQuestions();
        testResult.clearTestMap();
    }

    @Override
    public void validateTestConfiguration() {
        var total = testResult.getTotalQuestionsNumber();
        var quantity = questionDao.getQuantity();
        var passing = testResult.getPassingScoreNumber();
        if (total > quantity) {
            throw new InvalidTestConfigurationException("Too many test questions: " + total + " > " + quantity);
        }
        if (total <= passing) {
            throw new InvalidTestConfigurationException("Invalid total and passing questions number ratio");
        }
    }
}
