package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.GenreNotExistException;
import ru.otus.homework.repository.base.GenreRepository;

@Service
public class GenreServiceImpl implements GenreService {

  private final GenreRepository genreRepository;

  @Autowired
  public GenreServiceImpl(GenreRepository genreRepository) {
    this.genreRepository = genreRepository;
  }

  @Override
  public Genre add(String name) {
    return genreRepository.save(new Genre(name));
  }

  @Override
  public List<Genre> getAll() {
    return genreRepository.findAll();
  }

  @Override
  public Genre get(String id) {
    return getGenreByIdOrThrowException(id);
  }

  @Override
  public Genre update(String id, String name) {
    Genre genre = getGenreByIdOrThrowException(id);
    genre.setName(name);
    return genreRepository.updateGenreWithBooksAndComments(genre);
  }

  @Override
  public void remove(String id) {
    genreRepository.cascadeDeleteById(id);
  }

  private Genre getGenreByIdOrThrowException(String id) {
    return genreRepository.findById(id).orElseThrow(
        () -> new GenreNotExistException("Genre with id " + id + " does not exist"));
  }
}
