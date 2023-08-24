package ru.otus.homework.service;

import org.springframework.core.convert.converter.Converter;
import ru.otus.homework.domain.Question;

public interface QuestionConverter extends Converter<Question, String> {
    @Override
    String convert(Question question);
}