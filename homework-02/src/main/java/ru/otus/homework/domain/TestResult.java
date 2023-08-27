package ru.otus.homework.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class TestResult {

    private User user;

    private final Map<Question, Boolean> testMap;

    private final int passingScoreNumber;

    private final int totalQuestionsNumber;

    public TestResult(int totalQuestionsNumber, int passingScoreNumber) {
        this.passingScoreNumber = passingScoreNumber;
        this.totalQuestionsNumber = totalQuestionsNumber;
        testMap = new LinkedHashMap<>(totalQuestionsNumber);
    }

    public void addUserAnswer(Question question, boolean isCorrect) {
        testMap.put(question, isCorrect);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getTotalQuestionsNumber() {
        return totalQuestionsNumber;
    }

    public int getPassingScoreNumber() {
        return passingScoreNumber;
    }

    public Map<Question, Boolean> getTestMap() {
        return testMap;
    }

    public int getActualScore() {
        return (int) testMap.values().stream().filter(Predicate.isEqual(true)).count();
    }

    public boolean isPassed() {
        return getActualScore() >= passingScoreNumber;
    }

    public void clearTestMap() {
        testMap.clear();
    }
}
