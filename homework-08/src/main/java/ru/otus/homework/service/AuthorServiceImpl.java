package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Author;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.exceptions.not_exist.AuthorNotExistException;
import ru.otus.homework.exceptions.relation.AuthorRelatedBookExistException;
import ru.otus.homework.repository.base.AuthorRepository;
import ru.otus.homework.repository.base.BookRepository;

@Service
public class AuthorServiceImpl implements AuthorService {

  private final AuthorRepository authorRepository;

  private final BookRepository bookRepository;

  private final ConversionService conversionService;

  @Autowired
  public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository,
      ConversionService conversionService) {
    this.authorRepository = authorRepository;
    this.bookRepository = bookRepository;
    this.conversionService = conversionService;
  }

  @Override
  public AuthorDto add(String name) {
    Author author = authorRepository.save(new Author(name));
    return conversionService.convert(author, AuthorDto.class);
  }

  @Override
  public List<AuthorDto> getAll() {
    return authorRepository.findAll().stream()
        .map(a -> conversionService.convert(a, AuthorDto.class)).toList();
  }

  @Override
  public AuthorDto get(String id) {
    Author author = getAuthorByIdOrThrowException(id);
    return conversionService.convert(author, AuthorDto.class);
  }

  @Override
  public AuthorDto update(String id, String name) {
    Author author = getAuthorByIdOrThrowException(id);
    author.setName(name);
    Author updatedAuthor = authorRepository.updateWithBooks(author);
    return conversionService.convert(updatedAuthor, AuthorDto.class);
  }

  @Override
  public void remove(String id) {
    if (bookRepository.existsByAuthorId(id)) {
      throw new AuthorRelatedBookExistException("Books exist for author with id" + id);
    }
    authorRepository.deleteById(id);
  }

  private Author getAuthorByIdOrThrowException(String id) {
    return authorRepository.findById(id).orElseThrow(
        () -> new AuthorNotExistException("Author with id " + id + " does not exist"));
  }
}
