package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dao.BookDao;
import ru.otus.homework.domain.Book;
import ru.otus.homework.dto.BookDto;

@Service
public class BookServiceImpl implements BookService {

  private final BookDao bookDao;

  private final ConversionService conversionService;

  @Autowired
  public BookServiceImpl(BookDao bookDao, ConversionService conversionService) {
    this.bookDao = bookDao;
    this.conversionService = conversionService;
  }

  @Override
  @Transactional
  public BookDto add(Book book) {
    book = bookDao.insert(book);
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public BookDto get(long id) {
    Book book = bookDao.getById(id);
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BookDto> getAll() {
    return bookDao.getAll().stream().map(b -> conversionService.convert(b, BookDto.class)).toList();
  }

  @Override
  @Transactional
  public BookDto update(Book book) {
    book = bookDao.update(book);
    return conversionService.convert(book, BookDto.class);
  }

  @Override
  @Transactional
  public void remove(long id) {
    bookDao.deleteById(id);
  }
}
