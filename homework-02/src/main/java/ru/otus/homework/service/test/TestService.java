package ru.otus.homework.service.test;

public interface TestService {

    void validateAnswer(int questionIndex, int answerIndex);

    int getTotalNumberOfQuestions();

    String getQuestionRepresentation(int questionIndex);

    String getTestResultRepresentation();

    void restartTest();

    void validateTestConfiguration();
}
