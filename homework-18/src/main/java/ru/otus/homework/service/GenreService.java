package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.dto.GenreDto;

public interface GenreService {

    void add(String name);

    GenreDto update(long id, String name);

    GenreDto get(long id);

    List<GenreDto> getAll();

    void remove(long id);
}
