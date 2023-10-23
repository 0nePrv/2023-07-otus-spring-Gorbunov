package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.domain.Genre;

public interface GenreService {

    Genre add(String name);

    Genre update(String id, String name);

    List<Genre> getAll();

    Genre get(String id);

    void remove(String id);
}
