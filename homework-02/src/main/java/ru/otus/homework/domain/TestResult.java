package ru.otus.homework.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class TestResult {

    private final User user;

    private final Map<Question, Boolean> testMap;

    private final int passingScoreNumber;

    private final int totalQuestionsNumber;

    public TestResult(int totalQuestionsNumber, int passingScoreNumber, User user) {
        this.passingScoreNumber = passingScoreNumber;
        this.totalQuestionsNumber = totalQuestionsNumber;
        this.user = user;
        testMap = new LinkedHashMap<>(totalQuestionsNumber);
    }

    public User getUser() {
        return user;
    }

    public Map<Question, Boolean> getTestMap() {
        return testMap;
    }

    public int getTotalQuestionsNumber() {
        return totalQuestionsNumber;
    }

    public void addUserAnswer(Question question, boolean isCorrect) {
        testMap.put(question, isCorrect);
    }

    public int getActualScore() {
        return (int) testMap.values().stream().filter(Predicate.isEqual(true)).count();
    }

    public boolean isPassed() {
        return getActualScore() >= passingScoreNumber;
    }
}
