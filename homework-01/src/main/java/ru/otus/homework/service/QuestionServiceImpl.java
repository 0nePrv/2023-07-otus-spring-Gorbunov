package ru.otus.homework.service;

import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.exceptions.InvalidCorrectAnswerIndexException;
import ru.otus.homework.exceptions.QuestionDataReadingException;

import java.util.Objects;

public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;

    private final QuestionFromSourceConverter converterFromSource;

    private final QuestionToStringConverter converterToString;

    private final OutputService outputService;

    public QuestionServiceImpl(QuestionDao questionDao, QuestionFromSourceConverter converterFromSource,
                               QuestionToStringConverter converterToString, OutputService outputService) {
        this.questionDao = questionDao;
        this.converterFromSource = converterFromSource;
        this.converterToString = converterToString;
        this.outputService = outputService;
    }

    @Override
    public void printQuestions() {
        try {
            questionDao.readAllQuestions().stream().map(converterFromSource::convert).filter(Objects::nonNull)
                    .forEach(question -> outputService.outputString(converterToString.convert(question)));
        } catch (InvalidCorrectAnswerIndexException | QuestionDataReadingException exception) {
            outputService.outputString(exception.getMessage());
        }
    }
}