package ru.otus.homework.service;

import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Question;

import java.util.List;

public class QuestionReaderServiceImpl implements QuestionReaderService {

    private final QuestionDao questionDao;

    public QuestionReaderServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<Question> getQuestions() {
        return questionDao.readAllQuestions();
    }
}
