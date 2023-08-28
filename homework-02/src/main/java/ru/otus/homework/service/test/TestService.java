package ru.otus.homework.service.test;

import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.TestResult;

public interface TestService {

    boolean validateAnswer(Question question, int answerIndex);

    String getQuestionRepresentation(int questionIndex, Question question);

    String getTestResultRepresentation(TestResult testResult);

    String getLineInExceptionFormat(String s, boolean fatal);

    int getTotalQuestionsNumber();

    int getPassingScoreNumber();

    void validateTestConfiguration(int quantity);
}
