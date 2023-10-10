package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Book;

@Component
public class BookConverter implements Converter<Book, String> {
    @Override
    public String convert(@NonNull Book book) {
        return book.getId() + ": " + book.getName() + " written by " + book.getAuthor().getName() +
               " of genre " + book.getGenre().getName();
    }
}
