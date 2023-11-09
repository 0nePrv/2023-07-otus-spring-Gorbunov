package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.exceptions.not_exist.GenreNotExistException;
import ru.otus.homework.exceptions.relation.GenreRelatedBookExistException;
import ru.otus.homework.repository.base.BookRepository;
import ru.otus.homework.repository.base.GenreRepository;

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
  public GenreDto add(String name) {
    Genre genre = genreRepository.save(new Genre(name));
    return conversionService.convert(genre, GenreDto.class);
  }

  @Override
  public List<GenreDto> getAll() {
    return genreRepository.findAll().stream().map(g -> conversionService.convert(g, GenreDto.class))
        .toList();
  }

  @Override
  public GenreDto get(String id) {
    Genre genre = getGenreByIdOrThrowException(id);
    return conversionService.convert(genre, GenreDto.class);
  }

  @Override
  public GenreDto update(String id, String name) {
    Genre genre = getGenreByIdOrThrowException(id);
    genre.setName(name);
    Genre updatedGenre = genreRepository.updateWithBooks(genre);
    return conversionService.convert(updatedGenre, GenreDto.class);
  }

  @Override
  public void remove(String id) {
    genreRepository.findById(id).ifPresent(genre -> {
          if (bookRepository.existsByGenreId(genre.getId())) {
            throw new GenreRelatedBookExistException("Books exist for genre " + genre.getName());
          }
          genreRepository.deleteById(id);
        }
    );
  }

  private Genre getGenreByIdOrThrowException(String id) {
    return genreRepository.findById(id).orElseThrow(
        () -> new GenreNotExistException("Genre with id " + id + " does not exist"));
  }
}
