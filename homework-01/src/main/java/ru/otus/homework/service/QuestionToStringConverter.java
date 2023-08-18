package ru.otus.homework.service;

import org.springframework.core.convert.converter.Converter;
import ru.otus.homework.domain.Question;

public interface QuestionToStringConverter extends Converter<Question, String> {
    @Override
    String convert(Question source);
}
