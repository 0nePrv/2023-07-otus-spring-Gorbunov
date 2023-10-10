package ru.otus.homework.service.book;

import java.util.List;
import ru.otus.homework.domain.Book;


public interface BookService {

    Book add(Book book);

    void update(Book book);

    List<Book> getAll();

    Book get(long id);

    void remove(long id);
}
