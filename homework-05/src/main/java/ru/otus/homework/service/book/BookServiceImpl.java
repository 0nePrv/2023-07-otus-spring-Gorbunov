package ru.otus.homework.service.book;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.domain.Book;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    @Autowired
    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Book add(Book book) {
        return bookDao.insert(book);
    }

    @Override
    public Book get(long id) {
        return bookDao.getById(id);
    }

    @Override
    public List<Book> getAll() {
        return bookDao.getAll();
    }

    @Override
    public void update(Book book) {
        bookDao.update(book);
    }

    @Override
    public void remove(long id) {
        bookDao.deleteById(id);
    }
}
