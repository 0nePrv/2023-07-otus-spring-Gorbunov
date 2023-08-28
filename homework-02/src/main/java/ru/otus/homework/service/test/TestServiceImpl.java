package ru.otus.homework.service.test;

import org.springframework.core.convert.ConversionService;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.exceptions.InvalidCorrectAnswerException;
import ru.otus.homework.exceptions.fatal.InvalidTestConfigurationException;


public class TestServiceImpl implements TestService {

    private final ConversionService conversionService;

    private final int totalQuestionsNumber;

    private final int passingScoreNumber;

    public TestServiceImpl(ConversionService conversionService, int totalQuestionsNumber, int passingScoreNumber) {
        this.conversionService = conversionService;
        this.passingScoreNumber = passingScoreNumber;
        this.totalQuestionsNumber = totalQuestionsNumber;
    }

    @Override
    public boolean validateAnswer(Question currentQuestion, int answerIndex) {
        var answerList = currentQuestion.getAnswerList();
        if (answerIndex >= answerList.size() || answerIndex < 0) {
            throw new InvalidCorrectAnswerException("Answer index is out of range");
        }
        return answerList.get(answerIndex).isCorrect();
    }

    @Override
    public String getQuestionRepresentation(int questionIndex, Question question) {
        return "#" + ++questionIndex + " " + conversionService.convert(question, String.class);
    }

    @Override
    public String getTestResultRepresentation(TestResult testResult) {
        return conversionService.convert(testResult, String.class);
    }

    @Override
    public String getLineInExceptionFormat(String s, boolean fatal) {
        String type = fatal ? "Fatal " : "An ";
        return String.format(type + "error occurred: %s", s);
    }

    @Override
    public int getTotalQuestionsNumber() {
        return totalQuestionsNumber;
    }

    @Override
    public int getPassingScoreNumber() {
        return passingScoreNumber;
    }

    @Override
    public void validateTestConfiguration(int quantity) {
        if (totalQuestionsNumber > quantity) {
            throw new InvalidTestConfigurationException("Too many test questions: " + totalQuestionsNumber +
                    " > " + quantity);
        }
        if (totalQuestionsNumber <= passingScoreNumber) {
            throw new InvalidTestConfigurationException("Invalid total and passing questions number ratio");
        }
    }
}
