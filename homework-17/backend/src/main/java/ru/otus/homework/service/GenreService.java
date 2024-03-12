package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.dto.GenreDto;

public interface GenreService {

    GenreDto add(String name);

    GenreDto update(long id, String name);

    List<GenreDto> getAll();

    GenreDto get(long id);

    void remove(long id);
}
