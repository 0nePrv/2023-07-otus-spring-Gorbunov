package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Author;

@Component
public class AuthorConverter implements Converter<Author, String> {
    @Override
    public String convert(@NonNull Author author) {
        return author.getId() + ": " + author.getName();
    }
}
