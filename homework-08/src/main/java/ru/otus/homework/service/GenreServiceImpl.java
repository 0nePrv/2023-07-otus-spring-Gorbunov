package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.exceptions.ObjectNotFoundException;
import ru.otus.homework.repository.GenreRepository;

@Service
public class GenreServiceImpl implements GenreService {

  private final GenreRepository genreRepository;

  private final BookService bookService;

  @Autowired
  public GenreServiceImpl(GenreRepository genreRepository, @Lazy BookService bookService) {
    this.genreRepository = genreRepository;
    this.bookService = bookService;
  }

  @Override
  public Genre add(String name) {
    Genre genre = new Genre().setName(name);
    return genreRepository.save(genre);
  }

  @Override
  public List<Genre> getAll() {
    return genreRepository.findAll();
  }

  @Override
  public Genre get(String id) {
    return genreRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
  }

  @Override
  public Genre update(String id, String name) {
    Genre genre = new Genre().setId(id).setName(name);
    return genreRepository.save(genre);
  }

  @Override
  public void remove(String id) {
    bookService.removeAllByGenreId(id);
    genreRepository.deleteById(id);
  }
}
