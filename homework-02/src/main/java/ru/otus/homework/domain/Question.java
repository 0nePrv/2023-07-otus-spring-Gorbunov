package ru.otus.homework.domain;

import java.util.List;
import java.util.Objects;


public class Question {

    private final String text;

    private final List<Answer> answerList;

    public Question(String text, List<Answer> answerList) {
        this.text = text;
        this.answerList = answerList;
    }

    public String getText() {
        return text;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Question question = (Question) other;
        return text.equals(question.text) && Objects.equals(answerList, question.answerList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, answerList);
    }
}