package ru.otus.homework.repository;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.homework.domain.Author;
import ru.otus.homework.repository.custom.AuthorRepositoryCustom;


public interface AuthorRepository extends ReactiveMongoRepository<Author, String>, AuthorRepositoryCustom {

}
