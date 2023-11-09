package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Author;
import ru.otus.homework.dto.AuthorDto;

@Component
public class AuthorToDtoConverter implements Converter<Author, AuthorDto> {
    @Override
    public AuthorDto convert(@NonNull Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }
}
