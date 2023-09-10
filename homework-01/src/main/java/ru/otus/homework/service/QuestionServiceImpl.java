package ru.otus.homework.service;

import org.springframework.core.convert.ConversionService;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.exceptions.InvalidCorrectAnswerIndexException;
import ru.otus.homework.exceptions.QuestionDataReadingException;

public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;

    private final ConversionService conversionService;

    private final OutputService outputService;

    public QuestionServiceImpl(QuestionDao questionDao,
                               ConversionService conversionService,
                               OutputService outputService) {
        this.questionDao = questionDao;
        this.conversionService = conversionService;
        this.outputService = outputService;
    }

    @Override
    public void printQuestions() {
        try {
            questionDao.readAllQuestions()
                    .stream().map(question -> conversionService.convert(question, String.class))
                    .forEach(outputService::outputString);
        } catch (InvalidCorrectAnswerIndexException | QuestionDataReadingException exception) {
            outputService.outputString(exception.getMessage());
        }
    }
}