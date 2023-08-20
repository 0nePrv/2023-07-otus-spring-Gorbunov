package ru.otus.homework.domain;

import java.util.List;


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

}