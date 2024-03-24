package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.exception.AuthorNotExistException;
import ru.otus.homework.repository.AuthorRepository;

@Service
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository authorDao;

  private final ConversionService conversionService;

  @Autowired
  public AuthorServiceImpl(AuthorRepository authorRepository,
      @Qualifier("mvcConversionService") ConversionService conversionService) {
    this.authorDao = authorRepository;
    this.conversionService = conversionService;
  }

  @Override
  @Transactional
  public void add(String name) {
    Author author = new Author().setName(name);
    authorDao.save(author);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AuthorDto> getAll() {
    return authorDao.findAll().stream().map(a -> conversionService.convert(a, AuthorDto.class))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public AuthorDto get(long id) {
    Author author = authorDao.findById(id).orElseThrow(
        () -> new AuthorNotExistException("Author with id " + id + " does not exist"));
    return conversionService.convert(author, AuthorDto.class);
  }

  @Override
  @Transactional
  public void update(long id, String name) {
    Author author = new Author().setId(id).setName(name);
    authorDao.save(author);
  }

  @Override
  @Transactional
  public void remove(long id) {
    authorDao.deleteById(id);
  }
}
