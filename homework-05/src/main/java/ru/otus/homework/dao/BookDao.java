package ru.otus.homework.dao;

import java.util.List;
import ru.otus.homework.domain.Book;

public interface BookDao {

    Book insert(Book book);

    Book getById(long id);

    List<Book> getAll();

    void update(Book book);

    void deleteById(long id);
}
