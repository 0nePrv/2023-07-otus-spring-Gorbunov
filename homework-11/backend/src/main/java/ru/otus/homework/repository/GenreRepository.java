package ru.otus.homework.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.custom.GenreRepositoryCustom;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String>,
    GenreRepositoryCustom {

}
