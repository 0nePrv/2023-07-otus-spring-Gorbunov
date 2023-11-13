package ru.otus.homework.repository.custom;

import ru.otus.homework.domain.Genre;

public interface GenreRepositoryCustom {

  Genre updateWithBooks(Genre genre);
}
