package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.domain.Genre;

public interface GenreService {

    Genre add(String name);

    Genre update(long id, String name);

    List<Genre> getAll();

    Genre get(long id);

    void remove(long id);
}
