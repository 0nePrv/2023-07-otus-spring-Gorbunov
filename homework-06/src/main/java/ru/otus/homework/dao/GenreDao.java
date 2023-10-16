package ru.otus.homework.dao;

import java.util.List;
import ru.otus.homework.domain.Genre;

public interface GenreDao {

    Genre insert(Genre genre);

    Genre getById(long id);

    List<Genre> getAll();

    void update(Genre genre);

    void deleteById(long id);
}
