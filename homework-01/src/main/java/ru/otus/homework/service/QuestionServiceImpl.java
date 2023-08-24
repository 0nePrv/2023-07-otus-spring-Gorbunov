package ru.otus.homework.service;

import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.exceptions.InvalidCorrectAnswerIndexException;
import ru.otus.homework.exceptions.QuestionDataReadingException;

public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;

    private final QuestionConverter converterToString;

    private final OutputService outputService;

    public QuestionServiceImpl(QuestionDao questionDao, QuestionConverter converterToString,
                               OutputService outputService) {
        this.questionDao = questionDao;
        this.converterToString = converterToString;
        this.outputService = outputService;
    }

    @Override
    public void printQuestions() {
        try {
            questionDao.readAllQuestions()
                    .forEach(question -> outputService.outputString(converterToString.convert(question)));
        } catch (InvalidCorrectAnswerIndexException | QuestionDataReadingException exception) {
            outputService.outputString(exception.getMessage());
        }
    }
}