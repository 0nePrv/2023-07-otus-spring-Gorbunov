package ru.otus.homework.service.test;

import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;

public interface TestService {

    boolean validateAnswer(Question question, int answerIndex);

    String getQuestionRepresentation(int questionIndex, Question question);

    String getTestResultRepresentation(TestResult testResult);

    String formatException(String s, boolean fatal);

    int getTotalQuestionsNumber();

    int getPassingScoreNumber();

    void validateTestConfiguration(int quantity);

    String getTestDescription(User user, int totalQuestionsNumber, int passingScoreNumber);
}
