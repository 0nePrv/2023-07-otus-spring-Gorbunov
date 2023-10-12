package ru.otus.homework.dao;


import java.util.List;
import ru.otus.homework.domain.Author;

public interface AuthorDao {

    Author insert(Author author);

    Author getById(long id);

    List<Author> getAll();

    void update(Author author);

    void deleteById(long id);
}
