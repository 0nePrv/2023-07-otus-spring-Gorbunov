package ru.otus.homework.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.exception.dataConsistency.GenreRelatedBookExistException;
import ru.otus.homework.exception.notExist.GenreNotExistException;
import ru.otus.homework.repository.BookRepository;
import ru.otus.homework.repository.GenreRepository;

@Service
public class GenreServiceImpl implements GenreService {

  private final GenreRepository genreRepository;

  private final BookRepository bookRepository;

  private final ConversionService conversionService;

  @Autowired
  public GenreServiceImpl(GenreRepository genreRepository, BookRepository bookRepository,
      ConversionService conversionService) {
    this.genreRepository = genreRepository;
    this.bookRepository = bookRepository;
    this.conversionService = conversionService;
  }

  @Override
  public Mono<GenreDto> add(Mono<GenreDto> genreDtoMono) {
    return genreDtoMono
        .mapNotNull(GenreDto::getName)
        .mapNotNull(Genre::new)
        .mapNotNull(genreRepository::save)
        .mapNotNull(g -> conversionService.convert(g, GenreDto.class));
  }

  @Override
  public Flux<GenreDto> getAll() {
    return genreRepository.findAll()
        .mapNotNull(g -> conversionService.convert(g, GenreDto.class));
  }

  @Override
  public Mono<GenreDto> get(String id) {
    return getGenreByIdOrCreateError(id)
        .mapNotNull(g -> conversionService.convert(g, GenreDto.class));
  }

  @Override
  public Mono<GenreDto> update(String id, Mono<GenreDto> genreDtoMono) {
    return genreDtoMono.flatMap(genreDto -> getGenreByIdOrCreateError(id)
        .doOnSuccess(existingGenre -> existingGenre.setName(genreDto.getName()))
        .flatMap(genreRepository::updateWithBooks)
        .mapNotNull(updatedGenre -> conversionService.convert(updatedGenre, GenreDto.class)));
  }

  @Override
  public Mono<Void> remove(String id) {
    return bookRepository.existsByGenreId(id)
        .flatMap(exists -> exists ?
            Mono.error(() ->
                new GenreRelatedBookExistException("Books exist for genre with id " + id))
            : genreRepository.deleteById(id));
  }

  private Mono<Genre> getGenreByIdOrCreateError(String id) {
    return genreRepository.findById(id)
        .switchIfEmpty(
            Mono.error(() ->
                new GenreNotExistException("Genre with id " + id + " does not exist")));
  }
}
