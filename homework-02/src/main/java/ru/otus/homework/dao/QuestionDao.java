package ru.otus.homework.dao;

import ru.otus.homework.domain.Question;

public interface QuestionDao {

    void refreshQuestions();

    Question getQuestion(int index);

    int getQuantity();
}