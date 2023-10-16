package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.domain.Genre;

public interface GenreService {

    Genre add(Genre genre);

    void update(Genre genre);

    List<Genre> getAll();

    Genre get(long id);

    void remove(long id);
}
