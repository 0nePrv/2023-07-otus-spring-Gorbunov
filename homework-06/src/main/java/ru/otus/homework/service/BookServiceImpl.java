package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.service.mapping.Mapper;

@Service
public class BookServiceImpl implements BookService {

  private final BookDao bookDao;

  private final Mapper<Book, BookDto> mapper;

  @Autowired
  public BookServiceImpl(BookDao bookDao, Mapper<Book, BookDto> mapper) {
    this.bookDao = bookDao;
    this.mapper = mapper;
  }

  @Override
  @Transactional
  public BookDto add(Book book) {
    Book insertedBook = bookDao.insert(book);
    return mapper.map(insertedBook);
  }

  @Override
  @Transactional(readOnly = true)
  public BookDto get(long id) {
    Book book = bookDao.getById(id);
    return mapper.map(book);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BookDto> getAll() {
    return bookDao.getAll().stream().map(mapper::map).toList();
  }

  @Override
  @Transactional
  public BookDto update(Book book) {
    bookDao.update(book);
    return mapper.map(book);
  }

  @Override
  @Transactional
  public void remove(long id) {
    bookDao.deleteById(id);
  }
}
