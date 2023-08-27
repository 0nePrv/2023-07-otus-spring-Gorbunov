package ru.otus.homework.service.question;

import ru.otus.homework.domain.Question;

public interface QuestionService {

    void refreshQuestions();

    Question getQuestion(int index);

    int getQuantity();
}