package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.domain.Author;

public interface AuthorService {

    Author add(Author author);

    void update(Author author);

    List<Author> getAll();

    Author get(long id);

    void remove(long id);
}
