package ru.otus.homework.service;

import org.springframework.core.convert.converter.Converter;
import ru.otus.homework.domain.Question;

import java.util.Map;

public interface QuestionFromSourceConverter extends Converter<Map<String, String>, Question> {

    @Override
    Question convert(Map<String, String> source);
}
