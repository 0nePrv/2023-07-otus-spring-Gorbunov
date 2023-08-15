package ru.otus.homework.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class Question {
    @JsonProperty("Question")
    private String text;

    @JsonProperty("Answers")
    private List<Answer> answerList;

    public Question() {
    }

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
