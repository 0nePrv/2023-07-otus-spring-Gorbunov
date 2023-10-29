package ru.otus.homework.repository.base;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.custom.GenreRepositoryCustom;

public interface GenreRepository extends MongoRepository<Genre, String>, GenreRepositoryCustom {

}
