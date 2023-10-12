package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Genre;

@Component
public class GenreConverter implements Converter<Genre, String> {
    @Override
    public String convert(@NonNull Genre genre) {
        return genre.getId() + ": " + genre.getName();
    }
}
