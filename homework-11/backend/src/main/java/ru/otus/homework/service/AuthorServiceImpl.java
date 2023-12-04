package ru.otus.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Author;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.exception.dataConsistency.AuthorRelatedBookExistException;
import ru.otus.homework.exception.notExist.AuthorNotExistException;
import ru.otus.homework.repository.AuthorRepository;
import ru.otus.homework.repository.BookRepository;

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
  public Mono<AuthorDto> add(Mono<AuthorDto> authorDtoMono) {
    return authorDtoMono.flatMap(authorDto ->
        authorRepository.save(new Author(authorDto.getName()))
            .mapNotNull(a -> conversionService.convert(a, AuthorDto.class)));
  }

  @Override
  public Flux<AuthorDto> getAll() {
    return authorRepository.findAll()
        .mapNotNull(a -> conversionService.convert(a, AuthorDto.class));
  }

  @Override
  public Mono<AuthorDto> get(String id) {
    return getAuthorByIdOrCreateError(id).mapNotNull(
        (author) -> conversionService.convert(author, AuthorDto.class));
  }

  @Override
  public Mono<AuthorDto> update(String id, Mono<AuthorDto> authorDtoMono) {
    return authorDtoMono.flatMap(author ->
        getAuthorByIdOrCreateError(id)
            .doOnSuccess(existingAuthor -> existingAuthor.setName(author.getName()))
            .flatMap(authorRepository::updateWithBooks)
            .mapNotNull(
                updatedAuthor -> conversionService.convert(updatedAuthor, AuthorDto.class)));
  }

  @Override
  public Mono<Void> remove(String id) {
    return bookRepository.existsByAuthorId(id)
        .flatMap(exists -> exists ?
            Mono.error(
                () -> new AuthorRelatedBookExistException("Books exist for author with id " + id))
            : authorRepository.deleteById(id));
  }

  private Mono<Author> getAuthorByIdOrCreateError(String id) {
    return authorRepository.findById(id).switchIfEmpty(
        Mono.error(() -> new AuthorNotExistException("Author with id " + id + " does not exist")));
  }
}
