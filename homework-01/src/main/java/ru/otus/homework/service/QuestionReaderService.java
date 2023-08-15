package ru.otus.homework.service;

import ru.otus.homework.domain.Question;

import java.util.List;

public interface QuestionReaderService {
    List<Question> getQuestions();
}
