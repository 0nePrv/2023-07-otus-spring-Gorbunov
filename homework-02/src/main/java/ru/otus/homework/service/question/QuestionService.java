package ru.otus.homework.service.question;

import ru.otus.homework.domain.Question;

public interface QuestionService {
    Question getQuestion(int index);

    int getQuantity();
}
