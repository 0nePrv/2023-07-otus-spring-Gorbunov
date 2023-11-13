package ru.otus.homework.service;

import java.util.List;
import ru.otus.homework.dto.GenreDto;

public interface GenreService {

    GenreDto add(String name);

    GenreDto update(String id, String name);

    List<GenreDto> getAll();

    GenreDto get(String id);

    void remove(String id);
}
